package io.techery.sample.storage;

import android.content.Context;

import java.util.List;

import io.techery.janet.ActionHolder;
import io.techery.janet.ActionService;
import io.techery.janet.ActionServiceWrapper;
import io.techery.janet.JanetException;
import io.techery.snapper.DataCollection;
import io.techery.snapper.DroidSnapper;
import io.techery.snapper.Snapper;
import io.techery.snapper.model.Indexable;

public class StorageActionServiceWrapper extends ActionServiceWrapper {

    private final Context context;
    private Snapper snapper;

    public StorageActionServiceWrapper(ActionService actionService, Context context) {
        super(actionService);
        this.context = context;
    }

    private <T extends Indexable> DataCollection<T> getCollection(Class<T> type, String key) {
        if (snapper == null) {
            snapper = DroidSnapper.with(context);
        }
        DataCollection<T> collection = snapper.collection(type, key);
        while (true) {
            if (collection.isClosed()) {
                return null;
            }
            if (collection.isInitialized()) {
                return collection;
            }
        }
    }

    @Override protected <A> boolean onInterceptSend(ActionHolder<A> holder) throws JanetException {
        if (holder.action() instanceof CachedAction) {
            CachedAction action = (CachedAction) holder.action();
            CacheOptions options = action.getOptions();
            if (options.restoreFromCache()) {
                DataCollection collection = getCollection(options.type(), options.key());
                if (collection == null)
                    return false;
                List data = collection.toList();
                if (!data.isEmpty()) {
                    action.onRestore(data);
                    return !options.sendAfterRestore();
                }
            }
        }
        return false;
    }

    @Override protected <A> void onInterceptCancel(ActionHolder<A> holder) {}

    @Override protected <A> void onInterceptStart(ActionHolder<A> holder) {}

    @Override protected <A> void onInterceptProgress(ActionHolder<A> holder, int progress) {}

    @Override protected <A> void onInterceptSuccess(ActionHolder<A> holder) {
        if (holder.action() instanceof CachedAction) {
            CachedAction action = (CachedAction) holder.action();
            CacheOptions options = action.getOptions();
            if (options.saveToCache()) {
                DataCollection collection = getCollection(options.type(), options.key());
                if (collection == null)
                    return;
                collection.clear();
                collection.insertAll(action.getData());
            }
        }
    }

    @Override protected <A> void onInterceptFail(ActionHolder<A> holder, JanetException e) {}

}
