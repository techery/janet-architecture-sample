package io.techery.sample.ui.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.StringRes;

import io.techery.presenta.mortar.PresenterService;
import io.techery.presenta.mortarscreen.presenter.InjectablePresenter;
import io.techery.sample.R;
import trikita.anvil.RenderableView;

public abstract class ScreenView<T extends InjectablePresenter> extends RenderableView {

    protected T presenter;

    private final ProgressDialog progress;

    public ScreenView(Context context) {
        super(context);
        if (!isInEditMode()) {
            presenter = PresenterService.getPresenter(context);
        }
        progress = new ProgressDialog(context);
        progress.setMessage(getString(R.string.progress_loading));
        progress.setIndeterminate(true);
        progress.setCancelable(false);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            presenter.takeView(this);
        }
    }

    @Override
    public void onDetachedFromWindow() {
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
