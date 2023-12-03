package guru.qa.rococo.jupiter.extension;

import guru.qa.rococo.jupiter.annotation.ApiLogin;
import guru.qa.rococo.jupiter.annotation.GenerateUser;
import guru.qa.rococo.jupiter.annotation.GeneratedUser;
import guru.qa.rococo.model.UserJson;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.lang.reflect.Parameter;

public abstract class AbstractCreateUserExtension implements BeforeEachCallback, ParameterResolver {
    public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(AbstractCreateUserExtension.class);
    static final String API_LOGIN_KEY = "loginAnnotation";
    private static final String GENERATE_USER_KEY = "userAnnotation";

    @Override
    public void beforeEach(ExtensionContext extensionContext) {
        GenerateUser generateUserAnnotation = extensionContext.getRequiredTestMethod().getAnnotation(GenerateUser.class);
        if (generateUserAnnotation != null) {
            UserJson user = createUserForTest(generateUserAnnotation);
            extensionContext.getStore(NAMESPACE).put(extensionContext.getUniqueId() + GENERATE_USER_KEY, user);
        }

        ApiLogin loginAnnotation = extensionContext.getRequiredTestMethod().getAnnotation(ApiLogin.class);
        if (loginAnnotation != null && loginAnnotation.user().handleAnnotation()) {
            UserJson user = createUserForTest(loginAnnotation.user());
            extensionContext.getStore(NAMESPACE).put(extensionContext.getUniqueId() + API_LOGIN_KEY, user);
        }

    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        Parameter parameter = parameterContext.getParameter();
        return parameter.getAnnotation(GeneratedUser.class) != null && parameter.getType().isAssignableFrom(UserJson.class);
    }

    @Override
    public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        GeneratedUser annotation = parameterContext.getParameter().getAnnotation(GeneratedUser.class);
        String key = switch (annotation.selector()) {
            case API_LOGIN -> "loginAnnotation";
            case GENERATE_USER -> "userAnnotation";
        };
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId() + key, UserJson.class);
    }

    protected abstract UserJson createUserForTest(GenerateUser annotation);
}
