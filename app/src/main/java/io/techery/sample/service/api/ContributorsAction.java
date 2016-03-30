package io.techery.sample.service.api;

import java.util.List;

import io.techery.janet.http.annotations.HttpAction;
import io.techery.janet.http.annotations.Path;
import io.techery.janet.http.annotations.Response;
import io.techery.sample.model.ImmutableUser;
import io.techery.sample.model.Repository;
import io.techery.sample.model.User;
import io.techery.sample.storage.CacheOptions;
import io.techery.sample.storage.CachedAction;
import io.techery.sample.storage.ImmutableCacheOptions;

@HttpAction("/repos/{owner}/{repo}/contributors")
public class ContributorsAction extends BaseAction implements CachedAction<User> {

    @Path("owner")
    final String ownerLogin;

    @Path("repo")
    final String repoName;

    @Response
    List<User> contributors;

    private final Repository repository;

    private final boolean restoreFromCache;

    public ContributorsAction(Repository repository, boolean restoreFromCache) {
        this.repository = repository;
        this.restoreFromCache = restoreFromCache;
        this.ownerLogin = repository.owner().login();
        this.repoName = repository.name();
    }

    public Repository getRepository() {
        return repository;
    }

    @Override public CacheOptions getOptions() {
        return ImmutableCacheOptions.builder()
                .type(ImmutableUser.class)
                .key(repository.id().toString())
                .restoreFromCache(restoreFromCache)
                .build();
    }

    @Override public List<User> getData() {
        return contributors;
    }

    @Override public void onRestore(List<User> data) {
        contributors = data;
    }
}
