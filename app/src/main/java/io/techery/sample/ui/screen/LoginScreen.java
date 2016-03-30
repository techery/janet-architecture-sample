package io.techery.sample.ui.screen;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.widget.Toast;

import javax.inject.Inject;

import flow.Flow;
import flow.History;
import io.techery.presenta.addition.flow.path.Layout;
import io.techery.presenta.mortarscreen.presenter.WithPresenter;
import io.techery.sample.R;
import io.techery.sample.service.manager.AuthManager;
import io.techery.sample.ui.ScreenPresenter;
import io.techery.sample.ui.view.LoginView;

@Layout(R.layout.screen_login)
@WithPresenter(LoginScreen.Presenter.class)
public class LoginScreen extends Screen {

    public static class Presenter extends ScreenPresenter<LoginView> {

        @Inject AuthManager authManager;

        public Presenter(PresenterInjector injector) {
            super(injector);
        }

        @Override protected void onLoad(Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);
            bindPipeCached(authManager.accessTokenCommandPipe())
                    .onStart((command) -> getView().showProgress())
                    .onFail((command, throwable) ->
                            Toast.makeText(activityHolder.activity(), throwable.getLocalizedMessage(), Toast.LENGTH_SHORT)
                                    .show())
                    .onSuccess(command -> logined());
        }

        private void logined() {
            getView().hideProgress();
            History history = History.single(new ReposScreen());
            Flow.get(getView()).setHistory(history, Flow.Direction.REPLACE);
        }

        public void login() {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(authManager.getAuthorizationUrl()));
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activityHolder.activity().startActivity(intent);
        }
    }
}
