package io.techery.sample.ui.screen;

import android.content.Context;
import android.view.View;

import flow.path.Path;

public abstract class Screen extends Path {

    public String getTitle() {
        return null;
    }

    public abstract View createView(Context context);
}
