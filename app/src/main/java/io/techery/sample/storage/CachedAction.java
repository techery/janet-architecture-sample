package io.techery.sample.storage;

import java.util.List;

import io.techery.snapper.model.Indexable;

public interface CachedAction<T extends Indexable> {

    CacheOptions getOptions();

    List<T> getData();

    void onRestore(List<T> data);
}
