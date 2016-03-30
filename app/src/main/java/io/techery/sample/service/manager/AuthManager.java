package io.techery.sample.service.manager;

import com.github.scribejava.core.oauth.OAuth20Service;

import io.techery.janet.ActionPipe;
import io.techery.janet.Janet;
import io.techery.janet.ReadActionPipe;
import io.techery.sample.service.api.UserAction;
import io.techery.sample.service.oauth.AccessTokenCommand;
import io.techery.sample.storage.PreferenceWrapper;
import rx.schedulers.Schedulers;

public class AuthManager {

    private final OAuth20Service oAuthService;
    private final PreferenceWrapper preferenceWrapper;
    private final ActionPipe<AccessTokenCommand> accessTokenCommandPipe;

    public AuthManager(Janet janet, OAuth20Service oAuthService, PreferenceWrapper preferenceWrapper) {
        this.oAuthService = oAuthService;
        this.preferenceWrapper = preferenceWrapper;
        accessTokenCommandPipe = janet.createPipe(AccessTokenCommand.class, Schedulers.io());
        accessTokenCommandPipe.observeSuccess()
                .map(command -> command.getResult().getAccessToken())
                .subscribe(preferenceWrapper::setToken);
    }

    public String getAuthorizationUrl() {
        return oAuthService.getAuthorizationUrl();
    }

    public void loadAccessToken(String code) {
        accessTokenCommandPipe.send(new AccessTokenCommand(code));
    }

    public ReadActionPipe<AccessTokenCommand> accessTokenCommandPipe() {
        return accessTokenCommandPipe;
    }

    public boolean isAuthorized() {
        return preferenceWrapper.getToken() != null;
    }
}
