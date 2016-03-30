package io.techery.sample.service.manager;

import io.techery.janet.ActionPipe;
import io.techery.janet.Janet;
import io.techery.janet.ReadActionPipe;
import io.techery.janet.WriteActionPipe;
import io.techery.sample.model.Repository;
import io.techery.sample.model.User;
import io.techery.sample.service.api.ContributorsAction;
import io.techery.sample.service.api.ReposAction;
import io.techery.sample.service.api.UserAction;
import rx.schedulers.Schedulers;

public class ApiManager {

    private final ActionPipe<ReposAction> reposActionPipe;
    private final ActionPipe<ContributorsAction> contributorsActionPipe;
    private final ActionPipe<UserAction> userActionPipe;

    public ApiManager(Janet janet) {
        reposActionPipe = janet.createPipe(ReposAction.class, Schedulers.io());
        contributorsActionPipe = janet.createPipe(ContributorsAction.class, Schedulers.io());
        userActionPipe = janet.createPipe(UserAction.class, Schedulers.io());
    }

    public ReadActionPipe<ReposAction> reposActionPipe() {
        return reposActionPipe;
    }

    public ReadActionPipe<ContributorsAction> contributorsActionPipe() {
        return contributorsActionPipe;
    }

    public ActionPipe<UserAction> userActionPipe() {
        return userActionPipe;
    }

    public void loadRepos(User user, boolean restoreFromCache) {
        reposActionPipe.send(new ReposAction(user, restoreFromCache));
    }

    public void loadContributors(Repository repository, boolean restoreFromCache) {
        contributorsActionPipe.send(new ContributorsAction(repository, restoreFromCache));
    }
}
