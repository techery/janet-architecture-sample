package io.techery.sample.di;

import dagger.Module;
import dagger.Provides;
import io.techery.presenta.di.ApplicationScope;
import io.techery.sample.presenta.ActivityHolder;
import io.techery.sample.presenta.ToolbarHolder;

@Module
public final class ActivityHolderModule {

    @ApplicationScope
    @Provides ActivityHolder providesActivityHolder() {
        return new ActivityHolder();
    }

    @ApplicationScope
    @Provides ToolbarHolder providesToolbarHolder() {
        return new ToolbarHolder();
    }

}
