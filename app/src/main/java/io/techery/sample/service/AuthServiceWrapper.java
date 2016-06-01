package io.techery.sample.service;

import android.text.TextUtils;

import io.techery.janet.ActionHolder;
import io.techery.janet.ActionServiceWrapper;
import io.techery.janet.HttpActionService;
import io.techery.janet.JanetException;
import io.techery.sample.service.api.BaseAction;
import io.techery.sample.storage.PreferenceWrapper;

public class AuthServiceWrapper extends ActionServiceWrapper {

    private final PreferenceWrapper prefs;

    public AuthServiceWrapper(HttpActionService actionService, PreferenceWrapper prefs) {
        super(actionService);
        this.prefs = prefs;
    }

    @Override protected <A> boolean onInterceptSend(ActionHolder<A> holder) {
        if (holder.action() instanceof BaseAction) {
            String authToken = prefs.getToken();
            if (!TextUtils.isEmpty(authToken)) {
                ((BaseAction) holder.action()).setToken(authToken);
            }
        }
        return false;
    }

    @Override protected <A> void onInterceptCancel(ActionHolder<A> holder) {}

    @Override protected <A> void onInterceptStart(ActionHolder<A> holder) {}

    @Override protected <A> void onInterceptProgress(ActionHolder<A> holder, int progress) {}

    @Override protected <A> void onInterceptSuccess(ActionHolder<A> holder) {}

    @Override protected <A> boolean onInterceptFail(ActionHolder<A> holder, JanetException e) {
        return false;
    }
}
