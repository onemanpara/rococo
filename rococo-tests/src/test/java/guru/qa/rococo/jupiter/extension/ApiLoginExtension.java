package guru.qa.rococo.jupiter.extension;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import guru.qa.rococo.api.auth.AuthServiceClient;
import guru.qa.rococo.api.context.CookieContext;
import guru.qa.rococo.api.context.LocalStorageContext;
import guru.qa.rococo.config.Config;
import guru.qa.rococo.jupiter.annotation.ApiLogin;
import guru.qa.rococo.jupiter.annotation.GenerateUser;
import guru.qa.rococo.model.UserJson;
import io.qameta.allure.Step;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.Cookie;

import static guru.qa.rococo.jupiter.extension.AbstractCreateUserExtension.API_LOGIN_KEY;
import static io.qameta.allure.Allure.step;

public class ApiLoginExtension implements BeforeEachCallback, AfterTestExecutionCallback {
    private final AuthServiceClient authServiceClient = new AuthServiceClient();

    @Override
    public void beforeEach(ExtensionContext extensionContext) {
        ApiLogin loginAnnotation = extensionContext.getRequiredTestMethod().getAnnotation(ApiLogin.class);
        if (loginAnnotation != null) {
            GenerateUser user = loginAnnotation.user();
            String username;
            String password;
            if (user.handleAnnotation()) {
                UserJson createdUser = extensionContext.getStore(AbstractCreateUserExtension.NAMESPACE).get(
                        extensionContext.getUniqueId() + API_LOGIN_KEY,
                        UserJson.class
                );
                username = createdUser.getUsername();
                password = createdUser.getPassword();
            } else if (!loginAnnotation.username().isEmpty() && !loginAnnotation.password().isEmpty()) {
                username = loginAnnotation.username();
                password = loginAnnotation.password();
            } else throw new IllegalArgumentException("Login data not found");

            doLogin(username, password);
        }
    }

    @Override
    public void afterTestExecution(ExtensionContext extensionContext) {
        LocalStorageContext.getInstance().clearContext();
        CookieContext.getInstance().clearContext();
    }

    @Step("Do api login for selenium")
    private void doLogin(String username, String password) {
        LocalStorageContext localStorageContext = LocalStorageContext.getInstance();
        CookieContext cookieContext = CookieContext.getInstance();

        authServiceClient.doLogin(username, password);

        step("Set auth data in webdriver", () -> {
            Selenide.open(Config.getConfig().rococoFrontendUrl());
            Selenide.localStorage().setItem("codeChallenge", localStorageContext.getCodeChallenge());
            Selenide.localStorage().setItem("id_token", localStorageContext.getToken());
            Selenide.localStorage().setItem("codeVerifier", localStorageContext.getCodeVerifier());
            Cookie jsessionIdCookie = new Cookie("JSESSIONID", cookieContext.getJSessionIdCookieValue());
            WebDriverRunner.getWebDriver().manage().addCookie(jsessionIdCookie);
        });
    }
}
