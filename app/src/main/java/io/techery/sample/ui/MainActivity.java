package io.techery.sample.ui;

import android.content.Intent;
import android.os.Bundle;

import javax.inject.Inject;

import flow.path.Path;
import io.techery.sample.R;
import io.techery.sample.service.manager.AuthManager;
import io.techery.sample.ui.screen.LoginScreen;
import io.techery.sample.ui.screen.ReposScreen;
import io.techery.sample.ui.screen.Screen;
import io.techery.sample.ui.view.MainView;
import io.techery.sample.utils.Oauth2Utils;

public class MainActivity extends BaseActivity {

    @Inject
    AuthManager authManager;

    private MainView mainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainView = new MainView(this);
        setContentView(mainView);
        initPresenta(mainView.getContainerView(), savedInstanceState, traversal -> {
            Path top = traversal.destination.top();
            if (top instanceof Screen) {
                String title = ((Screen) top).getTitle();
                if (title == null) {
                    title = getString(R.string.app_name);
                }
                mainView.setToolbarTitle(title);
            }
        });
    }

    @Override
    protected Path provideDefaultScreen() {
        if (authManager.isAuthorized()) {
            return new ReposScreen();
        } else {
            return new LoginScreen();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            String code = Oauth2Utils.parseRequestCode(intent.getData());
            if (code != null) {
                authManager.loadAccessToken(code);
            }
        }
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        takeViews();
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onAttachedToWindow() {
        takeViews();
        super.onAttachedToWindow();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        takeViews();
    }

    @Override
    public void onDetachedFromWindow() {
        dropViews();
        super.onDetachedFromWindow();
    }

    private void takeViews() {
        activityHolder.takeView(this);
    }

    private void dropViews() {
        activityHolder.dropView(this);
    }

}
