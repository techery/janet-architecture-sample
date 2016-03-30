package io.techery.sample.di;

import android.content.Context;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import io.techery.janet.ActionService;
import io.techery.janet.HttpActionService;
import io.techery.janet.gson.GsonConverter;
import io.techery.janet.http.HttpClient;
import io.techery.janet.okhttp3.OkClient;
import io.techery.presenta.di.ApplicationScope;
import io.techery.sample.service.AuthServiceWrapper;
import io.techery.sample.storage.StorageActionServiceWrapper;
import io.techery.sample.service.oauth.GitHubApi;
import io.techery.sample.storage.PreferenceWrapper;
import io.techery.sample.utils.okcurl.CurlInterceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

@Module
public class ApiModule {

    private static final String API_URL = "https://api.github.com";

    @ApplicationScope
    @Provides OAuth20Service provideOauthService() {
        return new ServiceBuilder()
                .apiKey("9a1966ec5bc86f5bc222")
                .apiSecret("f329d199e75a8d7d6ae182d8d7d41c5e155fe305")
                .callback("janet://callback")
                .build(GitHubApi.instance());
    }


    @Provides
    @ApplicationScope OkHttpClient provideOkHttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(60L, TimeUnit.SECONDS)
                .readTimeout(60L, TimeUnit.SECONDS)
                .addInterceptor(new CurlInterceptor())
                .addInterceptor(chain -> { // logging interceptor
                    Request request = chain.request();
                    Timber.tag("OkHttpClient").d(request.toString());
                    Response response = chain.proceed(request);
                    Timber.tag("OkHttpClient").d(response.toString());
                    return response;
                });
        return builder.build();
    }

    @ApplicationScope
    @Provides HttpClient provideHttpClient(OkHttpClient okHttpClient) {
        return new OkClient(okHttpClient);
    }

    @ApplicationScope
    @Provides(type = Provides.Type.SET)
    ActionService provideHttpService(HttpClient client, Gson gson, PreferenceWrapper prefs, @ApplicationScope Context context) {
        return new StorageActionServiceWrapper(
                new AuthServiceWrapper(
                        new HttpActionService(API_URL, client, new GsonConverter(gson)), prefs)
                , context);
    }

}
