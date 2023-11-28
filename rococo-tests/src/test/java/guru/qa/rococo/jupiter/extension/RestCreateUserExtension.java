package guru.qa.rococo.jupiter.extension;

import guru.qa.rococo.api.register.RegisterServiceClient;
import guru.qa.rococo.jupiter.annotation.GenerateUser;
import guru.qa.rococo.model.UserJson;
import guru.qa.rococo.util.DataUtil;
import io.qameta.allure.Step;

public class RestCreateUserExtension extends AbstractCreateUserExtension {
    private final RegisterServiceClient registerService = new RegisterServiceClient();

    @Override
    @Step("Create user for test (REST)")
    protected UserJson createUserForTest(GenerateUser annotation) {
        String username = annotation.username().isEmpty() ? DataUtil.generateRandomUsername() : annotation.username();
        String password = annotation.password().isEmpty() ? DataUtil.generateRandomPassword() : annotation.password();
        UserJson user = createUserJson(username, password);
        registerService.registerUser(username, password);
        return user;
    }

    private UserJson createUserJson(String desiredUsername, String desiredPassword) {
        UserJson user = new UserJson();
        user.setUsername(desiredUsername);
        user.setPassword(desiredPassword);
        return user;
    }
}
