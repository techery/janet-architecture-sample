package io.techery.sample.service.api;

import io.techery.janet.http.annotations.RequestHeader;

public class BaseAction {

    @RequestHeader("Authorization")
    String token;

    public void setToken(String token) {
        this.token = "token " + token;
    }
}
