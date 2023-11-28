package guru.qa.rococo.api.auth;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.rococo.api.RestService;
import guru.qa.rococo.api.context.CookieContext;
import guru.qa.rococo.api.context.LocalStorageContext;
import guru.qa.rococo.api.interceptor.AddCookieInterceptor;
import guru.qa.rococo.api.interceptor.ReceivedCodeInterceptor;
import guru.qa.rococo.api.interceptor.ReceivedCookieInterceptor;
import io.qameta.allure.Step;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AuthServiceClient extends RestService {
    private final AuthService authService = retrofit.create(AuthService.class);

    public AuthServiceClient() {
        super(CFG.rococoAuthUrl(), true,
                new ReceivedCookieInterceptor(),
                new AddCookieInterceptor(),
                new ReceivedCodeInterceptor()
        );
    }

    @Step("Do api login")
    public void doLogin(String username, String password) {
        LocalStorageContext localStorageContext = LocalStorageContext.getInstance().init();
        CookieContext cookieContext = CookieContext.getInstance().init();

        try {
            authService.authorize(
                    "code",
                    "client",
                    "openid",
                    CFG.rococoFrontendUrl() + "/authorized",
                    localStorageContext.getCodeChallenge(), "S256"
            ).execute();

            authService.login(
                    username,
                    password,
                    cookieContext.getXsrfTokenCookieValue()
            ).execute();

            JsonNode response = authService.token(
                    "Basic " + new String(Base64.getEncoder().encode("client:secret".getBytes(StandardCharsets.UTF_8))),
                    "client",
                    CFG.rococoFrontendUrl() + "/authorized",
                    "authorization_code",
                    localStorageContext.getCode(),
                    localStorageContext.getCodeVerifier()
            ).execute().body();

            localStorageContext.setToken(response.get("id_token").asText());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}