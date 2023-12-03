package guru.qa.rococo.jupiter.extension;

import guru.qa.rococo.db.model.user.AuthUserEntity;
import guru.qa.rococo.db.model.user.Authority;
import guru.qa.rococo.db.model.user.AuthorityEntity;
import guru.qa.rococo.db.repository.UserRepository;
import guru.qa.rococo.db.repository.UserRepositoryHibernate;
import guru.qa.rococo.jupiter.annotation.GenerateUser;
import guru.qa.rococo.model.UserJson;
import guru.qa.rococo.util.DataUtil;
import io.qameta.allure.Step;

import java.util.ArrayList;
import java.util.Arrays;

public class DBCreateUserExtension extends AbstractCreateUserExtension {

    @Override
    @Step("Create user for test (DB)")
    protected UserJson createUserForTest(GenerateUser annotation) {
        UserRepository userRepository = new UserRepositoryHibernate();
        String username = annotation.username().isEmpty() ? DataUtil.generateRandomUsername() : annotation.username();
        String password = annotation.password().isEmpty() ? DataUtil.generateRandomPassword() : annotation.password();
        AuthUserEntity user = this.fillAuthUserEntity(username, password);
        userRepository.createUserForTest(user);
        return UserJson.fromEntity(user);
    }

    private AuthUserEntity fillAuthUserEntity(String desiredUsername, String desiredPassword) {
        AuthUserEntity user = new AuthUserEntity();

        user.setUsername(desiredUsername);
        user.setPassword(desiredPassword);
        user.setEncodedPassword(desiredPassword);
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setAuthorities(new ArrayList<>(Arrays.stream(Authority.values())
                .map(a -> {
                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setAuthority(a);
                    ae.setUser(user);
                    return ae;
                }).toList()));
        return user;
    }
}
