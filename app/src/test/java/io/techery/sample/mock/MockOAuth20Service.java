package io.techery.sample.mock;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.oauth.OAuth20Service;
import java.util.Map;

public class MockOAuth20Service extends OAuth20Service {

  private final String authorizationUrl;
  private final String token;

  public MockOAuth20Service(DefaultApi20 api, OAuthConfig config,
      String authorizationUrl, String token) {
    super(api, config);
    this.authorizationUrl = authorizationUrl;
    this.token = token;
  }

  @Override public String getAuthorizationUrl(Map<String, String> additionalParams) {
    return authorizationUrl;
  }

  @Override protected OAuth2AccessToken sendAccessTokenRequestSync(OAuthRequest request) {
    return new OAuth2AccessToken(token);
  }
}