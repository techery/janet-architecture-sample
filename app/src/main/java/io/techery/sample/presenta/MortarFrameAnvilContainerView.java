package io.techery.sample.presenta;

import android.content.Context;
import android.util.AttributeSet;

import flow.path.Path;
import io.techery.presenta.addition.FramePathContainerView;
import io.techery.presenta.mortarflow.MortarContextFactory;

public class MortarFrameAnvilContainerView extends FramePathContainerView {

    public MortarFrameAnvilContainerView(Context context) {
        this(context, null);
    }

    public MortarFrameAnvilContainerView(Context context, AttributeSet attrs) {
        super(context, attrs, new AnvilPathContainer(io.techery.presenta.addition.R.id.mortar_screen_switcher_tag, Path.contextFactory(new MortarContextFactory())));
    }
}
