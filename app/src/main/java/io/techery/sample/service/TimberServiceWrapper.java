package io.techery.sample.service;

import android.text.TextUtils;

import io.techery.janet.ActionHolder;
import io.techery.janet.ActionService;
import io.techery.janet.ActionServiceWrapper;
import io.techery.janet.JanetException;
import timber.log.Timber;

public class TimberServiceWrapper extends ActionServiceWrapper {

    private final String serviceTag;

    public TimberServiceWrapper(ActionService actionService) {
        this(actionService, null);
    }

    public TimberServiceWrapper(ActionService actionService, String tag) {
        super(actionService);
        if (TextUtils.isEmpty(tag)) serviceTag = actionService.getClass().getSimpleName();
        else serviceTag = tag;
    }

    private Timber.Tree timber() {
        return Timber.tag(serviceTag);
    }

    @Override protected <A> boolean onInterceptSend(ActionHolder<A> holder) {
        return false;
    }

    @Override protected <A> void onInterceptStart(ActionHolder<A> holder) {
        timber().i("Action started: %s", holder.action());
    }

    @Override protected <A> void onInterceptCancel(ActionHolder<A> holder) {
        timber().i("Action canceled: %s", holder.action());
    }

    @Override protected <A> void onInterceptProgress(ActionHolder<A> holder, int progress) {
        timber().i("Action progress = %d: %s",progress, holder.action());
    }

    @Override protected <A> void onInterceptSuccess(ActionHolder<A> holder) {
        timber().i("Action succeeded: %s", holder.action());
    }

    @Override protected <A> void onInterceptFail(ActionHolder<A> holder, JanetException e) {
        timber().w(e, "Action failed: %s", holder.action());
    }
}
