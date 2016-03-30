package io.techery.sample.presenta;

import android.app.Activity;

import mortar.Presenter;
import mortar.bundler.BundleService;

public class ActivityHolder extends Presenter<Activity> {

    @Override protected BundleService extractBundleService(Activity view) {
        return BundleService.getBundleService(view);
    }

    public Activity activity() {
        return getView();
    }

    @Override public void dropView(Activity view) {
        super.dropView(view);
    }
}
