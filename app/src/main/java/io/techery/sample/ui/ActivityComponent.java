package io.techery.sample.ui;

import android.app.Activity;

import dagger.Component;
import io.techery.presenta.di.ScreenScope;
import io.techery.sample.AppComponent;

@ScreenScope(Activity.class)
@Component(dependencies = AppComponent.class)
public interface ActivityComponent extends AppComponent, ScreenComponent {
    void inject(MainActivity activity);
}
