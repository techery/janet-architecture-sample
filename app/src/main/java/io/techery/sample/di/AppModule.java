package io.techery.sample.di;

import android.app.Application;
import android.content.Context;

import dagger.Module;
import dagger.Provides;
import io.techery.presenta.di.ApplicationScope;
import io.techery.sample.App;

@Module
public final class AppModule {

    private final App app;

    public AppModule(App app) {
        this.app = app;
    }

    @ApplicationScope
    @Provides Application provideApplication() {
        return app;
    }

    @ApplicationScope
    @Provides Context provideContext() {
        return app;
    }
}
