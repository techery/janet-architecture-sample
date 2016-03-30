package io.techery.sample.di;

import com.github.scribejava.core.oauth.OAuth20Service;

import dagger.Module;
import dagger.Provides;
import io.techery.janet.Janet;
import io.techery.presenta.di.ApplicationScope;
import io.techery.sample.service.manager.ApiManager;
import io.techery.sample.service.manager.AuthManager;
import io.techery.sample.storage.PreferenceWrapper;

@Module(
        includes = {
                JanetModule.class,
                ApiModule.class
        })
public class ServiceModule {

    @ApplicationScope
    @Provides AuthManager provideAuthManager(Janet janet, OAuth20Service oAuthService, PreferenceWrapper preferenceWrapper) {
        return new AuthManager(janet, oAuthService, preferenceWrapper);
    }

    @ApplicationScope
    @Provides ApiManager provideApiManager(Janet janet) {
        return new ApiManager(janet);
    }
}
