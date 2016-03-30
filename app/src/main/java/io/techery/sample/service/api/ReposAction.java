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

@HttpAction("/user{path}/repos")
public class ReposAction extends BaseAction implements CachedAction<Repository> {

    @Path(value = "path", encode = false)
    final String idStr;

    @Response
    List<Repository> repositories;

    private final User user;
    private final boolean restoreFromCache;

    public ReposAction(User user, boolean restoreFromCache) {
        this.user = user;
        this.restoreFromCache = restoreFromCache;
        this.idStr = "/" + user.id();
    }

    public ReposAction(boolean restoreFromCache) {
        this.restoreFromCache = restoreFromCache;
        this.idStr = "";
        this.user = null;
    }


    public boolean isOwn(User user) {
        return user != null && user.id().equals(this.user.id());
    }

    public boolean isMine() {
        return user == null;
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
