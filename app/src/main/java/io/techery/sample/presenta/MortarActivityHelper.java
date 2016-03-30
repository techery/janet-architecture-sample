package io.techery.sample.presenta;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import io.techery.presenta.mortar.DaggerService;
import mortar.MortarScope;
import mortar.bundler.BundleServiceRunner;
import timber.log.Timber;

public class MortarActivityHelper {

    private WeakReference<Activity> activityRef;
    protected Class<?> componentClass;
    private final Object[] dependencies;
    //
    protected MortarScope activityScope;


    public MortarActivityHelper(Activity activity, Class<?> componentClass, Object... dependencies) {
        this.activityRef = new WeakReference<>(activity);
        this.componentClass = componentClass;
        this.dependencies = dependencies;
    }

    public void createScopeWithComponent() {
        Activity activity = activityRef.get();
        if (activity == null) throw new IllegalStateException("Can't create scope for null activity");

        Context application = activity.getApplicationContext();

        String scopeName = activity.getLocalClassName() + "-task-" + activity.getTaskId();
        MortarScope parentScope = MortarScope.getScope(application);
        //
        checkActivityScope(scopeName, parentScope);
        Object component = prepareComponent(application, componentClass, dependencies);
        inject(activity, component);
        //
        if (activityScope == null) {
            activityScope = parentScope.buildChild()
                    .withService(BundleServiceRunner.SERVICE_NAME, new BundleServiceRunner())
                    .withService(DaggerService.SERVICE_NAME, component)
                    .build(scopeName);
        }
    }

    private void checkActivityScope(String scopeName, MortarScope parentScope) {
        activityScope = parentScope.findChild(scopeName);
    }

    private Object prepareComponent(Context applicationContext, Class<?> componentClass, Object[] dependencies) {
        if (activityScope != null) {
            return activityScope.getService(DaggerService.SERVICE_NAME);
        }

        Object appComponent = DaggerService.getDaggerComponent(applicationContext);
        // squash dependencies
        Object[] allDependencies;
        if (dependencies != null && this.dependencies.length > 0) {
            allDependencies = Arrays.copyOf(this.dependencies, this.dependencies.length + 1);
        } else {
            allDependencies = new Object[1];
        }
        allDependencies[allDependencies.length - 1] = appComponent;
        //
        return DaggerService.createComponent(componentClass, allDependencies);
    }

    private void inject(Activity activity, Object component) {
        injection:
        for (Method method : component.getClass().getDeclaredMethods()) {
            for (Class<?> aClass : method.getParameterTypes()) {
                if (aClass.equals(activity.getClass())) {
                    try {
                        method.invoke(component, activity);
                    } catch (IllegalAccessException e) {
                        Timber.e(e, "Can't inject activity into %s", component);
                    } catch (InvocationTargetException e) {
                        Timber.e(e, "Can't inject activity into %s", component);
                    }
                    break injection;
                }
            }
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        if (activityScope == null) createScopeWithComponent();
        BundleServiceRunner.getBundleServiceRunner(activityScope).onCreate(savedInstanceState);
    }

    public void onSaveState(Bundle outState) {
        BundleServiceRunner.getBundleServiceRunner(activityScope).onSaveInstanceState(outState);
    }

    public void destroy() {
        Activity activity = activityRef.get();
        if ((activity == null || activity.isFinishing()) && activityScope != null) {
            activityScope.destroy();
            activityScope = null;
        }
    }

    @Nullable
    public Object getService(String name) {
        if (activityScope != null && activityScope.hasService(name)) {
            return activityScope.getService(name);
        }
        return null;
    }
}
