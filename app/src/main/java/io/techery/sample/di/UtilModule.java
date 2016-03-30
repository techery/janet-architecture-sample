package io.techery.sample.di;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dagger.Module;
import dagger.Provides;
import io.techery.presenta.di.ApplicationScope;
import io.techery.sample.model.GsonAdaptersRepository;
import io.techery.sample.model.GsonAdaptersUser;

@Module
public class UtilModule {

    @ApplicationScope
    @Provides Gson provideGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapterFactory(new GsonAdaptersRepository());
        builder.registerTypeAdapterFactory(new GsonAdaptersUser());
        return builder.create();
    }
}
