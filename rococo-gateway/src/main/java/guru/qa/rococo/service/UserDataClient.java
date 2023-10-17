package guru.qa.rococo.service;

import guru.qa.rococo.model.SessionJson;
import guru.qa.rococo.model.UserJson;
import jakarta.annotation.Nonnull;

public interface UserDataClient {
    @Nonnull
    UserJson updateUserInfo(@Nonnull UserJson user);

    @Nonnull
    UserJson user(@Nonnull String username);

    @Nonnull
    SessionJson getSessionInfo(@Nonnull String username);

}
