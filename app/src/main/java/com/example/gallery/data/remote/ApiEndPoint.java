package com.example.gallery.data.remote;

import com.example.gallery.BuildConfig;

/**
 * Created on 16/11/2023
 */

public final class ApiEndPoint {


    public static final String ENDPOINT_FACEBOOK_LOGIN = "/facebook/login";

    public static final String ENDPOINT_GOOGLE_LOGIN = "/google/login";

    public static final String ENDPOINT_LOGOUT = "/logout";


    public static final String ENDPOINT_SERVER_LOGIN = "/login";

    private ApiEndPoint() {
        // This class is not publicly instantiable
    }
}

