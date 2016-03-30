package io.techery.sample.service.manager;

import io.techery.janet.ActionPipe;
import io.techery.janet.Janet;
import io.techery.janet.ReadActionPipe;
import io.techery.sample.model.Repository;
import io.techery.sample.model.User;
import io.techery.sample.service.api.ContributorsAction;
import io.techery.sample.service.api.ReposAction;
import rx.schedulers.Schedulers;

public class ApiManager {

    private final ActionPipe<ReposAction> reposActionPipe;
    private final ActionPipe<ContributorsAction> contributorsActionPipe;

    public ApiManager(Janet janet) {
        reposActionPipe = janet.createPipe(ReposAction.class, Schedulers.io());
        contributorsActionPipe = janet.createPipe(ContributorsAction.class, Schedulers.io());
    }

    public ReadActionPipe<ReposAction> reposActionPipe() {
        return reposActionPipe;
    }

    public ReadActionPipe<ContributorsAction> contributorsActionPipe() {
        return contributorsActionPipe;
    }

    public void loadRepos(User user, boolean restoreFromCache) {
        reposActionPipe.send(new ReposAction(user, restoreFromCache));
    }

    public void loadRepos(boolean restoreFromCache) {
        reposActionPipe.send(new ReposAction(restoreFromCache));
    }

    public void loadContributors(Repository repository, boolean restoreFromCache) {
        contributorsActionPipe.send(new ContributorsAction(repository, restoreFromCache));
    }
}
