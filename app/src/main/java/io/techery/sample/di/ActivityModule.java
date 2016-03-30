package io.techery.sample.di;

import android.app.Activity;

import java.lang.ref.WeakReference;

import dagger.Module;
import dagger.Provides;

@Module
public final class ActivityModule {

    private final Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides Activity provideActivity() {
        return activity;
    }

    @Provides WeakReference<Activity> provideWeakActivity() {
        return new WeakReference<>(activity);
    }
}
