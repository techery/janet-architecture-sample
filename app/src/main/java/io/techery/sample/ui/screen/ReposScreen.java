package io.techery.sample.ui.screen;

import android.os.Bundle;
import android.widget.Toast;

import com.innahema.collections.query.queriables.Queryable;

import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import flow.Flow;
import io.techery.janet.ReadActionPipe;
import io.techery.presenta.addition.flow.path.Layout;
import io.techery.presenta.mortarscreen.presenter.WithPresenter;
import io.techery.sample.R;
import io.techery.sample.model.Repository;
import io.techery.sample.model.User;
import io.techery.sample.service.api.ReposAction;
import io.techery.sample.service.manager.ApiManager;
import io.techery.sample.ui.ScreenPresenter;
import io.techery.sample.ui.view.ReposView;

@Layout(R.layout.screen_repos)
@WithPresenter(ReposScreen.Presenter.class)
public class ReposScreen extends Screen {

    private final static Comparator<Repository> COMPARATOR = (lhs, rhs) -> lhs.name().compareTo(rhs.name());

    private User user;

    public ReposScreen(User user) {
        this.user = user;
    }

    public ReposScreen() {}

    @Override public String getTitle() {
        if (user != null) {
            return user.login();
        }
        return null;
    }

    public class Presenter extends ScreenPresenter<ReposView> {

        @Inject ApiManager apiManager;

        private final ReadActionPipe<ReposAction> actionPipe;

        public Presenter(PresenterInjector injector) {
            super(injector);
            actionPipe = apiManager.reposActionPipe()
                    .filter(action -> {
                        if (user != null) {
                            return action.isOwn(user);
                        } else {
                            return action.isMine();
                        }
                    });
        }

        @Override protected void onLoad(Bundle savedInstanceState) {
            bindPipeCached(actionPipe)
                    .onStart((action) -> {
                        List<Repository> repositories = action.getData();
                        if (repositories != null && !repositories.isEmpty()) {
                            onReceived(action);
                        } else {
                            getView().setRefreshing(true);
                        }
                    })
                    .onFail((action, throwable) -> {
                        getView().setRefreshing(false);
                        Toast.makeText(activityHolder.activity(), throwable.getLocalizedMessage(), Toast.LENGTH_SHORT)
                                .show();
                    })
                    .onSuccess(this::onReceived);

            loadRepos(true);
        }

        private void onReceived(ReposAction action) {
            getView().setRefreshing(false);
            getView().updateItems(Queryable.from(action.getData())
                    .sort(COMPARATOR)
                    .toList());
        }

        private void loadRepos(boolean restoreFromCache) {
            if (user != null) {
                apiManager.loadRepos(user, restoreFromCache);
            } else {
                apiManager.loadRepos(restoreFromCache);
            }
        }

        public void loadRepos() {
            loadRepos(false);
        }

        public void selectRepository(Repository repository) {
            Flow.get(getView()).set(new ContributorsScreen(repository));
        }

    }
}
