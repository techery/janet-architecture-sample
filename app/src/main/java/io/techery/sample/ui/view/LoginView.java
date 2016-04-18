package io.techery.sample.ui.view;

import android.content.Context;

import io.techery.sample.R;
import io.techery.sample.ui.screen.LoginScreen;

import static trikita.anvil.BaseDSL.CENTER;
import static trikita.anvil.BaseDSL.WRAP;
import static trikita.anvil.BaseDSL.layoutGravity;
import static trikita.anvil.BaseDSL.size;
import static trikita.anvil.DSL.button;
import static trikita.anvil.DSL.onClick;
import static trikita.anvil.DSL.text;

public class LoginView extends ScreenView<LoginScreen.Presenter> {

    public LoginView(Context context) {
        super(context);
    }

    @Override
    public void view() {
        button(() -> {
            size(WRAP, WRAP);
            layoutGravity(CENTER);
            text(R.string.btn_login);
            onClick(v -> presenter.login());
        });
    }
}
