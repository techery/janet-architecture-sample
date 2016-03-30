package io.techery.sample.storage;

import android.content.Context;

import io.techery.sample.utils.PreferenceHelper;

public class PreferenceWrapper extends PreferenceHelper {

    private final static String TOKEN_ACCESS_TOKEN = "token_access_token";

    public PreferenceWrapper(Context context) {
        super(context);
    }

    public void setToken(String token) {
        storeValue(TOKEN_ACCESS_TOKEN, token, true);
    }

    public String getToken() {
        return getStringValue(TOKEN_ACCESS_TOKEN, true);
    }
}
