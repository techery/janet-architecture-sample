package io.techery.sample.presenta;

import android.content.Context;
import android.view.View;

import flow.path.Path;
import flow.path.PathContextFactory;
import io.techery.sample.ui.screen.Screen;

public class AnvilPathContainer extends PresentaPathContainer {


    protected AnvilPathContainer(int tagKey, PathContextFactory contextFactory) {
        super(tagKey, contextFactory);
    }

    @Override
    protected View getLayout(Context context, Path path) {
        if (path instanceof Screen) {
            return ((Screen) path).createView(context);
        } else {
            throw new IllegalArgumentException(
                    String.format("Path must to extend %s", Screen.class.getSimpleName()));
        }
    }
}
