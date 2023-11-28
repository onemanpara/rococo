package guru.qa.rococo.db.repository;

import guru.qa.rococo.db.dao.AuthUserDAO;
import guru.qa.rococo.db.dao.UserDataDAO;
import guru.qa.rococo.db.model.user.AuthUserEntity;
import guru.qa.rococo.db.model.user.UserDataEntity;

public class AbstractUserRepository implements UserRepository {

    private final AuthUserDAO authUserDAO;
    private final UserDataDAO userDataDAO;

    protected AbstractUserRepository(AuthUserDAO authUserDAO, UserDataDAO userDataDAO) {
        this.authUserDAO = authUserDAO;
        this.userDataDAO = userDataDAO;
    }

    @Override
    public void createUserForTest(AuthUserEntity user) {
        this.authUserDAO.createUser(user);
        this.userDataDAO.createUserInUserData(fromAuthUser(user));
    }

    private UserDataEntity fromAuthUser(AuthUserEntity user) {
        UserDataEntity userData = new UserDataEntity();
        userData.setUsername(user.getUsername());
        return userData;
    }
}
