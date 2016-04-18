package io.techery.sample.di;

import dagger.Module;
import dagger.Provides;
import io.techery.presenta.di.ApplicationScope;
import io.techery.sample.presenta.ActivityHolder;

@Module
public final class ActivityHolderModule {

    @ApplicationScope
    @Provides
    ActivityHolder providesActivityHolder() {
        return new ActivityHolder();
    }
}
