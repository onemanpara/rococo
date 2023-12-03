package guru.qa.rococo.db.dao;

import guru.qa.rococo.db.model.user.AuthUserEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

public interface AuthUserDAO {
    PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    void createUser(AuthUserEntity user);

    AuthUserEntity getUserFromAuthUserById(UUID user);

    AuthUserEntity updateUser(AuthUserEntity user);

    void deleteUser(AuthUserEntity user);
}
