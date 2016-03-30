package io.techery.sample.utils;

import android.net.Uri;

public class Oauth2Utils {

    public static String parseRequestCode(Uri data) {
        return data.getQueryParameter("code");
    }
}
