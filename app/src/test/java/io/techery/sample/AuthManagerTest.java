package io.techery.sample;

import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.oauth.OAuth20Service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.stubbing.answers.DoesNothing;

import java.lang.reflect.Field;

import io.techery.janet.ActionHolder;
import io.techery.janet.ActionServiceWrapper;
import io.techery.janet.ActionState;
import io.techery.janet.CommandActionService;
import io.techery.janet.Janet;
import io.techery.janet.JanetException;
import io.techery.sample.mock.MockOAuth20Service;
import io.techery.sample.service.manager.AuthManager;
import io.techery.sample.service.oauth.AccessTokenCommand;
import io.techery.sample.service.oauth.GitHubApi;
import io.techery.sample.storage.PreferenceWrapper;
import rx.observers.TestSubscriber;

import static io.techery.sample.util.AssertUtil.assertActionSuccess;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AuthManagerTest extends BaseTest {

    private final static String TEST_AUTHORIZATION_URL = "http://example.com";
    private final static String TEST_TOKEN = "test token";

    private AuthManager authManager;
    private PreferenceWrapper preferenceWrapper;

    @Before
    public void setup() throws JanetException {
        OAuth20Service oAuth20Service = mockOAuth20Service();
        preferenceWrapper = mockPreferenceWrapper();
        Janet janet = new Janet.Builder()
                .addService(new InjectCommandActionWrapper(new CommandActionService(), oAuth20Service))
                .build();
        authManager = new AuthManager(janet, oAuth20Service, preferenceWrapper);
    }

    @Test
    public void getAuthorizationUrl() {
        Assert.assertEquals(authManager.getAuthorizationUrl(), TEST_AUTHORIZATION_URL);
    }

    @Test
    public void loadAccessToken() {
        TestSubscriber<ActionState<AccessTokenCommand>> subscriber = new TestSubscriber<>();
        authManager.accessTokenCommandPipe()
                .observe()
                .subscribe(subscriber);
        authManager.loadAccessToken("");
        assertActionSuccess(subscriber, action -> action.getResult().getAccessToken().equals(TEST_TOKEN));
        verify(preferenceWrapper, times(1)).setToken(TEST_TOKEN);
    }

    @Test
    public void isAuthorized() {
        Assert.assertTrue(authManager.isAuthorized());
        verify(preferenceWrapper, times(1)).getToken();
    }

    ///////////////////////////////////////////////////////////////////////////
    // Mocking helpers
    ///////////////////////////////////////////////////////////////////////////

    private static OAuth20Service mockOAuth20Service() {
        return new MockOAuth20Service(GitHubApi.instance(),
                new OAuthConfig("test", "test"), TEST_AUTHORIZATION_URL, TEST_TOKEN);
    }

    private static PreferenceWrapper mockPreferenceWrapper() {
        PreferenceWrapper preferenceWrapper = mock(PreferenceWrapper.class);
        doAnswer(new DoesNothing()).when(preferenceWrapper).setToken(anyString());
        when(preferenceWrapper.getToken()).thenReturn(TEST_TOKEN);
        return preferenceWrapper;
    }

    private final static class InjectCommandActionWrapper extends ActionServiceWrapper {
        private final OAuth20Service oAuth20Service;

        public InjectCommandActionWrapper(CommandActionService actionService, OAuth20Service oAuth20Service) {
            super(actionService);
            this.oAuth20Service = oAuth20Service;
        }

        @Override
        protected <A> boolean onInterceptSend(ActionHolder<A> holder) throws JanetException {
            if (holder.action() instanceof AccessTokenCommand) {
                try {
                    Field field = AccessTokenCommand.class.getDeclaredField("oAuthService");
                    field.setAccessible(true);
                    field.set(holder.action(), oAuth20Service);
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }

        @Override
        protected <A> void onInterceptCancel(ActionHolder<A> holder) {
        }

        @Override
        protected <A> void onInterceptStart(ActionHolder<A> holder) {
        }

        @Override
        protected <A> void onInterceptProgress(ActionHolder<A> holder, int progress) {
        }

        @Override
        protected <A> void onInterceptSuccess(ActionHolder<A> holder) {
        }

        @Override
        protected <A> void onInterceptFail(ActionHolder<A> holder, JanetException e) {
        }
    }
}
