package io.techery.sample.service;

import io.techery.sample.service.oauth.AccessTokenCommand;

public interface CommandsComponent {

    void inject(AccessTokenCommand command);

}