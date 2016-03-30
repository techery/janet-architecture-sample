package io.techery.sample.ui;

import io.techery.sample.ui.screen.ContributorsScreen;
import io.techery.sample.ui.screen.LoginScreen;
import io.techery.sample.ui.screen.ReposScreen;

public interface ScreenComponent {

    void inject(LoginScreen.Presenter presenter);
    void inject(ReposScreen.Presenter presenter);
    void inject(ContributorsScreen.Presenter presenter);
}
