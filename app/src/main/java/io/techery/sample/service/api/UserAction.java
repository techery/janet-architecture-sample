package io.techery.sample.service.api;

import java.util.ArrayList;
import java.util.List;

import io.techery.janet.http.annotations.HttpAction;
import io.techery.janet.http.annotations.Response;
import io.techery.sample.model.ImmutableUser;
import io.techery.sample.model.User;
import io.techery.sample.storage.CacheOptions;
import io.techery.sample.storage.CachedAction;
import io.techery.sample.storage.ImmutableCacheOptions;

@HttpAction("/user")
public class UserAction extends BaseAction implements CachedAction<User> {

    @Response User user;

    public User getUser() {
        return user;
    }

    @Override public CacheOptions getOptions() {
        return ImmutableCacheOptions.builder()
                .key("current")
                .type(ImmutableUser.class)
                .sendAfterRestore(false)
                .build();
    }

    @Override public List<User> getData() {
        List<User> data = new ArrayList<>();
        if (user != null) {
            data.add(user);
        }
        return data;
    }

    @Override public void onRestore(List<User> data) {
        if (!data.isEmpty()) {
            user = data.get(0);
        }
    }
}
