package io.techery.sample.service.oauth;

import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.oauth.OAuth20Service;

import javax.inject.Inject;

import io.techery.janet.CommandActionBase;
import io.techery.janet.command.annotations.CommandAction;

@CommandAction
public class AccessTokenCommand extends CommandActionBase<OAuth2AccessToken> {

    @Inject OAuth20Service oAuthService;

    private final String code;

    public AccessTokenCommand(String code) {this.code = code;}

    @Override protected void run(CommandCallback<OAuth2AccessToken> callback) {
        try {
            OAuth2AccessToken token = oAuthService.getAccessToken(code);
            callback.onSuccess(token);
        } catch (Throwable t) {
            callback.onFail(t);
        }
    }
}
