package guru.qa.rococo.db.dao;

import guru.qa.rococo.db.model.user.UserDataEntity;

public interface UserDataDAO {
    void createUserInUserData(UserDataEntity user);

    void deleteUserInUserData(UserDataEntity user);
}
