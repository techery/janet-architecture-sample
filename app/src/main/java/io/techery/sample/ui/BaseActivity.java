package io.techery.sample.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;

import javax.inject.Inject;

import flow.Flow;
import flow.History;
import flow.path.Path;
import io.techery.presenta.addition.FramePathContainerView;
import io.techery.presenta.addition.flow.util.GsonParceler;
import io.techery.sample.di.ActivityModule;
import io.techery.sample.presenta.ActivityHolder;
import io.techery.sample.presenta.FlowActivityHelper;
import io.techery.sample.presenta.MortarActivityHelper;
import io.techery.sample.presenta.PresentaActivityHelper;

public abstract class BaseActivity extends AppCompatActivity {

    private PresentaActivityHelper presenta;
    private FramePathContainerView container;

    @Inject
    Gson gson;
    @Inject
    ActivityHolder activityHolder;

    protected void initPresenta(FramePathContainerView container, Bundle savedInstanceState, OnFlowDispatchCallback callback) {
        this.container = container;
        // Init mortar
        MortarActivityHelper mortarHelper = new MortarActivityHelper(this, ActivityComponent.class, new ActivityModule(this));
        mortarHelper.createScopeWithComponent();

        // Init flow
        History defaultBackstack = History.single(provideDefaultScreen());
        FlowActivityHelper flowHelper = new FlowActivityHelper(this, new MainFlowDispatcher(container, callback), defaultBackstack, new GsonParceler(gson));

        getWindow().getDecorView().post(() -> flowHelper.setPathContainerView(container));

        presenta = new PresentaActivityHelper(mortarHelper, flowHelper);
        presenta.onActivityCreated(this, savedInstanceState);
    }

    protected abstract Path provideDefaultScreen();

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        presenta.onActivityPostCreated(this, savedInstanceState);
    }


    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return presenta.provideNonConfigurationInstance();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        presenta.onNewIntent(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenta.onActivitySaveInstanceState(this, outState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenta.onActivityResumed(this);
    }

    @Override
    protected void onPause() {
        presenta.onActivityPaused(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenta.onActivityDestroyed(this);
    }

    @Override
    public void onBackPressed() {
        if (presenta.handleBack() || (container != null && container.onBackPressed())) return;
        super.onBackPressed();
    }

    @Override
    public Object getSystemService(String name) {
        Object systemService = null;
        if (presenta != null) systemService = presenta.getSystemService(name);
        if (systemService == null) systemService = super.getSystemService(name);
        return systemService;
    }

    private static class MainFlowDispatcher implements Flow.Dispatcher {

        private final FramePathContainerView container;
        private OnFlowDispatchCallback dispatchCallback;

        public MainFlowDispatcher(FramePathContainerView container, OnFlowDispatchCallback callback) {
            this.container = container;
            this.dispatchCallback = callback;
        }

        @Override
        public void dispatch(Flow.Traversal traversal, Flow.TraversalCallback callback) {
            container.dispatch(traversal, callback::onTraversalCompleted);
            dispatchCallback.onDispatch(traversal);
        }
    }

    public interface OnFlowDispatchCallback {
        void onDispatch(Flow.Traversal traversal);
    }
}
