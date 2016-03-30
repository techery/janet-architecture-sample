package io.techery.sample.presenta;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import flow.Flow;

public class PresentaActivityHelper implements Application.ActivityLifecycleCallbacks {

    private FlowActivityHelper flowHelper;
    private MortarActivityHelper mortarHelper;

    public PresentaActivityHelper(MortarActivityHelper mortarHelper, FlowActivityHelper flowHelper) {
        this.mortarHelper = mortarHelper;
        this.flowHelper = flowHelper;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Lifecycle
    ///////////////////////////////////////////////////////////////////////////

    @Override public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (mortarHelper == null) {
            throw new IllegalStateException("Mortar helper must not be null");
        }
        mortarHelper.onCreate(savedInstanceState);
    }

    public void onActivityPostCreated(Activity activity, Bundle savedInstanceState) {
        if (flowHelper == null) {
            throw new IllegalStateException("Flow helper must not be null");
        }
        flowHelper.onCreate(savedInstanceState);
    }

    @Override public void onActivityStarted(Activity activity) {
    }

    @Override public void onActivityResumed(Activity activity) {
        flowHelper.onResume();
    }

    @Override public void onActivityPaused(Activity activity) {
        flowHelper.onPause();
    }

    @Override public void onActivityStopped(Activity activity) {
    }

    @Override public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        flowHelper.onSaveState(outState);
        mortarHelper.onSaveState(outState);
    }

    @Override public void onActivityDestroyed(Activity activity) {
        flowHelper = null;
        mortarHelper.destroy();
        mortarHelper = null;
    }

    public void onNewIntent(Intent intent) {
        flowHelper.onNewIntent(intent);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Activity options
    ///////////////////////////////////////////////////////////////////////////

    public boolean handleBack() {
        return flowHelper.handleBack();
    }

    public void swapDispatcher(Flow.Dispatcher dispatcher) {
        flowHelper.setDispatcher(dispatcher);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Technical mandatory
    ///////////////////////////////////////////////////////////////////////////

    public Object provideNonConfigurationInstance() {
        return flowHelper.provideNonConfigurationInstance();
    }

    @Nullable
    public Object getSystemService(String name) {
        Object service = null;
        if (flowHelper != null) service = flowHelper.getSystemService(name);
        if (service == null && mortarHelper != null) service = mortarHelper.getService(name);
        return service;
    }
}
