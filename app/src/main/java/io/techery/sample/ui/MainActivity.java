package io.techery.sample.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import flow.path.Path;
import io.techery.presenta.addition.MortarFramePathContainerView;
import io.techery.sample.R;
import io.techery.sample.service.manager.AuthManager;
import io.techery.sample.ui.screen.LoginScreen;
import io.techery.sample.ui.screen.ReposScreen;
import io.techery.sample.ui.screen.Screen;
import io.techery.sample.utils.Oauth2Utils;

public class MainActivity extends BaseActivity {

    @Inject
    AuthManager authManager;

    @Bind(R.id.root_container) MortarFramePathContainerView container;
    @Bind(R.id.main_toolbar) Toolbar toolbar;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initPresenta(container, savedInstanceState, traversal -> {
            Path top = traversal.destination.top();
            if (top instanceof Screen) {
                String title = ((Screen) top).getTitle();
                if (title == null) {
                    title = getString(R.string.app_name);
                }
                toolbar.setTitle(title);
            }
        });
    }

    @Override protected Path provideDefaultScreen() {
        if (authManager.isAuthorized()) {
            return new ReposScreen();
        } else {
            return new LoginScreen();
        }
    }

    @Override protected void onNewIntent(Intent intent) {
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

    @Override public void onAttachedToWindow() {
        takeViews();
        super.onAttachedToWindow();
    }

    @Override protected void onRestart() {
        super.onRestart();
        takeViews();
    }

    @Override public void onDetachedFromWindow() {
        dropViews();
        super.onDetachedFromWindow();
    }

    private void takeViews() {
        activityHolder.takeView(this);
        toolbarHolder.takeView(toolbar);
    }

    private void dropViews() {
        activityHolder.dropView(this);
        toolbarHolder.dropView(toolbar);
    }

}
