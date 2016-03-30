package io.techery.sample.storage;

import android.support.annotation.Nullable;

import org.immutables.value.Value;

import io.techery.snapper.model.Indexable;

@Value.Immutable
public abstract class CacheOptions {
    @Value.Default
    public boolean restoreFromCache() {
        return true;
    }

    @Value.Default
    public boolean saveToCache() {
        return true;
    }

    @Value.Default
    public boolean sendAfterRestore() {
        return true;
    }

    @Nullable public abstract String key();
    public abstract Class<? extends Indexable> type();
}
