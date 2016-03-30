package io.techery.sample.utils.okcurl;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

public class CurlInterceptor implements Interceptor {

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        final Request request = chain.request();

        final Request copy = request.newBuilder().build();
        final String curl = new CurlBuilder(copy).build();

        Timber.d(curl);

        return chain.proceed(request);
    }
}
