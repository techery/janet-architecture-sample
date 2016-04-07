package io.techery.sample;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import io.techery.janet.ActionState;
import io.techery.janet.Janet;
import io.techery.janet.JanetException;
import io.techery.sample.mock.MockHttpActionService;
import io.techery.sample.model.ImmutableRepository;
import io.techery.sample.model.ImmutableUser;
import io.techery.sample.model.Repository;
import io.techery.sample.model.User;
import io.techery.sample.service.api.ContributorsAction;
import io.techery.sample.service.api.ReposAction;
import io.techery.sample.service.manager.ApiManager;
import rx.observers.TestSubscriber;

import static io.techery.sample.util.AssertUtil.assertActionSuccess;

public class ApiManagerTest extends BaseTest {

    private static final User testUser;
    private static final Repository testRepo;
    private static final List<User> testUsers;
    private static final List<Repository> testRepos;

    static {
        testUsers = Collections.singletonList(
                testUser = ImmutableUser.builder()
                        .id(1L)
                        .login("test")
                        .url("http://example.com/user")
                        .avatarUrl("http://example.com/avatar.jpg")
                        .build()
        );
    }

    static {
        testRepos = Collections.singletonList(
                testRepo = ImmutableRepository.builder()
                        .id(1L)
                        .name("test")
                        .url("http://example.com/repo")
                        .description("test test test")
                        .owner(testUser)
                        .build()
        );
    }

    private ApiManager apiManager;

    @Before
    public void setup() throws JanetException {
        MockHttpActionService httpActionService = new MockHttpActionService.Builder()
                .bind(new MockHttpActionService.Response(200)
                        .body(testRepos), request -> request.getUrl().endsWith("repos"))
                .bind(new MockHttpActionService.Response(200)
                        .body(testUsers), request -> request.getUrl().endsWith("contributors"))
                .build();
        Janet janet = new Janet.Builder().addService(httpActionService).build();
        apiManager = new ApiManager(janet);
    }

    @Test
    public void loadRepos() {
        TestSubscriber<ActionState<ReposAction>> subscriber = new TestSubscriber<>();
        apiManager.reposActionPipe()
                .filter(action -> action.isOwn(testUser))
                .observe()
                .subscribe(subscriber);
        apiManager.loadRepos(testUser, false);
        assertActionSuccess(subscriber, action -> action.getData().equals(testRepos));
    }

    @Test
    public void loadContributors() {
        TestSubscriber<ActionState<ContributorsAction>> subscriber = new TestSubscriber<>();
        apiManager.contributorsActionPipe()
                .filter(action -> action.getRepository().equals(testRepo))
                .observe()
                .subscribe(subscriber);
        apiManager.loadContributors(testRepo, false);
        assertActionSuccess(subscriber, action -> action.getData().equals(testUsers));
    }


}
