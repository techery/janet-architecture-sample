package io.techery.sample.ui.view;

import android.content.Context;
import android.util.AttributeSet;

import butterknife.OnClick;
import io.techery.sample.R;
import io.techery.sample.ui.screen.LoginScreen;

public class LoginView extends ScreenView<LoginScreen.Presenter> {

    public LoginView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @OnClick(R.id.login)
    public void login() {
        presenter.login();
    }
}
