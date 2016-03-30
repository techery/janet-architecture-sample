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
import io.techery.sample.service.api.ContributorsAction;
import io.techery.sample.service.manager.ApiManager;
import io.techery.sample.ui.ScreenPresenter;
import io.techery.sample.ui.view.ContributorsView;


@Layout(R.layout.screen_contributors)
@WithPresenter(ContributorsScreen.Presenter.class)
public class ContributorsScreen extends Screen {

    private static final Comparator<User> COMPARATOR = (lhs, rhs) -> lhs.login().compareTo(rhs.login());

    private final Repository repository;

    public ContributorsScreen(Repository repository) {this.repository = repository;}

    @Override public String getTitle() {
        return repository.name();
    }

    public class Presenter extends ScreenPresenter<ContributorsView> {

        @Inject ApiManager apiManager;

        private final ReadActionPipe<ContributorsAction> actionPipe;

        public Presenter(PresenterInjector injector) {
            super(injector);
            actionPipe = apiManager.contributorsActionPipe()
                    .filter(action -> action.getRepository().equals(repository));
        }

        @Override protected void onLoad(Bundle savedInstanceState) {
            bindPipeCached(actionPipe)
                    .onStart((action) -> {
                        List<User> data = action.getData();
                        if (data != null && !data.isEmpty()) {
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

            loadContributors(true);
        }

        private void onReceived(ContributorsAction action) {
            getView().setRefreshing(false);
            getView().updateItems(Queryable.from(action.getData())
                    .sort(COMPARATOR)
                    .toList());
        }

        public void loadContributors(boolean restoreFromCache) {
            apiManager.loadContributors(repository, restoreFromCache);
        }

        public void loadContributors() {
            loadContributors(false);
        }

        public void selectContributor(User user) {
            Flow.get(getView()).set(new ReposScreen(user));
        }
    }

}
