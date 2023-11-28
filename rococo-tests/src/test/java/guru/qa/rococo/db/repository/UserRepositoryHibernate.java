package guru.qa.rococo.db.repository;

import guru.qa.rococo.db.dao.impl.hibernate.AuthUserDAOHibernate;
import guru.qa.rococo.db.dao.impl.hibernate.UserDataDAOHibernate;

public class UserRepositoryHibernate extends AbstractUserRepository {
    public UserRepositoryHibernate() {
        super(new AuthUserDAOHibernate(), new UserDataDAOHibernate());
    }
}
