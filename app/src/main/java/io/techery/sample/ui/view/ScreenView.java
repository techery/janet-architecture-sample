package io.techery.sample.ui.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import butterknife.ButterKnife;
import io.techery.presenta.mortar.PresenterService;
import io.techery.presenta.mortarscreen.presenter.InjectablePresenter;
import io.techery.sample.R;

public class ScreenView<T extends InjectablePresenter> extends FrameLayout {

    protected T presenter;

    private final ProgressDialog progress;

    public ScreenView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            presenter = PresenterService.getPresenter(context);
        }
        progress = new ProgressDialog(context);
        progress.setMessage(getString(R.string.progress_loading));
        progress.setIndeterminate(true);
        progress.setCancelable(false);
    }

    @Override protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    @Override protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            presenter.takeView(this);
        }
    }

    @Override protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (!isInEditMode()) {
            presenter.dropView(this);
        }
    }

    public void showProgress() {
        progress.show();
    }

    public void hideProgress() {
        if (progress.isShowing()) {
            progress.hide();
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Resource helper methods
    ///////////////////////////////////////////////////////////////////////////

    protected String getString(@StringRes int resourceString) {
        return getResources().getString(resourceString);
    }
}
