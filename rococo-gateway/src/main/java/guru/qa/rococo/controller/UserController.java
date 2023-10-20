package guru.qa.rococo.controller;

import guru.qa.rococo.model.SessionJson;
import guru.qa.rococo.model.UserJson;
import guru.qa.rococo.service.api.UserDataClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    private final UserDataClient userDataClient;

    @Autowired
    public UserController(@Qualifier("rest") UserDataClient userDataClient) {
        this.userDataClient = userDataClient;
    }

    @PatchMapping("/api/user")
    public UserJson updateUserInfo(@AuthenticationPrincipal Jwt principal,
                                   @Validated @RequestBody UserJson user) {
        String username = principal.getClaim("sub");
        user.setUsername(username);
        return userDataClient.updateUserInfo(user);
    }

    @GetMapping("/api/session")
    public SessionJson getSessionUser(@AuthenticationPrincipal Jwt principal) {
        String username = principal.getClaim("sub");
        SessionJson sessionJson = new SessionJson();
        sessionJson.setUsername(username);
        sessionJson.setExpiresAt(principal.getExpiresAt());
        sessionJson.setIssuedAt(principal.getIssuedAt());
        return sessionJson;
    }

    @GetMapping("/api/user")
    public UserJson getUserInfo(@AuthenticationPrincipal Jwt principal) {
        String username = principal.getClaim("sub");
        return userDataClient.user(username);
    }

}
