package io.techery.sample.service.api;

import java.util.List;

import io.techery.janet.http.annotations.HttpAction;
import io.techery.janet.http.annotations.Path;
import io.techery.janet.http.annotations.Response;
import io.techery.sample.model.ImmutableRepository;
import io.techery.sample.model.Repository;
import io.techery.sample.model.User;
import io.techery.sample.storage.CacheOptions;
import io.techery.sample.storage.CachedAction;
import io.techery.sample.storage.ImmutableCacheOptions;

@HttpAction("/user/{user_id}/repos")
public class ReposAction extends BaseAction implements CachedAction<Repository> {

    @Path(value = "user_id", encode = false)
    final String userId;

    @Response
    List<Repository> repositories;

    private final User user;
    private final boolean restoreFromCache;

    public ReposAction(User user, boolean restoreFromCache) {
        this.user = user;
        this.restoreFromCache = restoreFromCache;
        this.userId = user.id().toString();
    }

    public boolean isOwn(User user) {
        return user != null && user.id().equals(this.user.id());
    }

    @Override public List<Repository> getData() {
        return repositories;
    }

    @Override public void onRestore(List<Repository> data) {
        repositories = data;
    }

    @Override public CacheOptions getOptions() {
        String key = user != null ? user.login() : null;
        return ImmutableCacheOptions.builder()
                .type(ImmutableRepository.class)
                .key(key)
                .restoreFromCache(restoreFromCache)
                .build();
    }
}
