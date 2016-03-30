package io.techery.sample.di;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import io.techery.presenta.di.ApplicationScope;
import io.techery.sample.storage.PreferenceWrapper;

@Module
public class StorageModule {

    @ApplicationScope
    @Provides PreferenceWrapper providePreferenceWrapper(@ApplicationScope Context context) {
        return new PreferenceWrapper(context);
    }
}
