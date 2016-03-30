package io.techery.sample.presenta;

import android.support.v7.widget.Toolbar;

import mortar.Presenter;
import mortar.bundler.BundleService;

public class ToolbarHolder extends Presenter<Toolbar> {

    @Override protected BundleService extractBundleService(Toolbar view) {
        return BundleService.getBundleService(view.getContext());
    }

    public Toolbar getToolbar() {
        return getView();
    }
}
