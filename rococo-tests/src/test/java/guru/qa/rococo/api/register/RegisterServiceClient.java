package guru.qa.rococo.api.register;

import guru.qa.rococo.api.RestService;
import guru.qa.rococo.api.context.CookieContext;
import guru.qa.rococo.api.interceptor.ReceivedCodeInterceptor;
import io.qameta.allure.Step;

import java.io.IOException;

public class RegisterServiceClient extends RestService {
    private final RegisterService registerService = retrofit.create(RegisterService.class);

    public RegisterServiceClient() {
        super(CFG.rococoAuthUrl() + "/register/", false, new ReceivedCodeInterceptor());
    }

    @Step("Register user")
    public void registerUser(String username, String password) {
        CookieContext cookieContext = CookieContext.getInstance();
        try {
            registerService.register().execute();
            registerService.registerUser(
                    cookieContext.getXsrfTokenFormattedCookie(),
                    cookieContext.getXsrfTokenCookieValue(),
                    username,
                    password,
                    password
            ).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
