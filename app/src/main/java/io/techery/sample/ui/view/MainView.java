package io.techery.sample.ui.view;

import android.content.Context;
import android.widget.FrameLayout;

import io.techery.sample.R;
import io.techery.sample.presenta.MortarFrameAnvilContainerView;
import trikita.anvil.Anvil;

import static android.widget.LinearLayout.VERTICAL;
import static trikita.anvil.Anvil.render;
import static trikita.anvil.AppCompatv7DSL.title;
import static trikita.anvil.BaseDSL.MATCH;
import static trikita.anvil.BaseDSL.init;
import static trikita.anvil.BaseDSL.size;
import static trikita.anvil.BaseDSL.v;
import static trikita.anvil.BaseDSL.xml;
import static trikita.anvil.DSL.linearLayout;
import static trikita.anvil.DSL.orientation;

public class MainView extends FrameLayout implements Anvil.Renderable {

    private MortarFrameAnvilContainerView containerView;
    private String toolbarTitle;

    public MainView(Context context) {
        super(context);
        this.toolbarTitle = context.getString(R.string.app_name);
        Anvil.mount(this, this);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Anvil.unmount(this);
    }

    public MortarFrameAnvilContainerView getContainerView() {
        return containerView;
    }

    public void setToolbarTitle(String toolbarTitle) {
        this.toolbarTitle = toolbarTitle;
        render();
    }

    @Override
    public void view() {
        linearLayout(() -> {
            orientation(VERTICAL);
            xml(R.layout.toolbar, () -> {
                title(toolbarTitle);
            });
            v(MortarFrameAnvilContainerView.class, () -> {
                size(MATCH, MATCH);
                init(() -> containerView = Anvil.currentView());
            });
        });
    }
}
