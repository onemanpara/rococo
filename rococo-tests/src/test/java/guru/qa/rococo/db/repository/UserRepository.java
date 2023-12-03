package guru.qa.rococo.db.repository;

import guru.qa.rococo.db.model.user.AuthUserEntity;

public interface UserRepository {
    void createUserForTest(AuthUserEntity user);
}
