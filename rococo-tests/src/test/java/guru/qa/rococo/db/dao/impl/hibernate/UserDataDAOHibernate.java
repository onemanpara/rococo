package guru.qa.rococo.db.dao.impl.hibernate;

import guru.qa.rococo.db.ServiceDB;
import guru.qa.rococo.db.dao.UserDataDAO;
import guru.qa.rococo.db.jpa.EntityManagerFactoryProvider;
import guru.qa.rococo.db.jpa.JpaService;
import guru.qa.rococo.db.model.user.UserDataEntity;

public class UserDataDAOHibernate extends JpaService implements UserDataDAO {
    public UserDataDAOHibernate() {
        super(EntityManagerFactoryProvider.INSTANCE.getDataSource(ServiceDB.USERDATA).createEntityManager());
    }

    @Override
    public void createUserInUserData(UserDataEntity user) {
        this.persist(user);
    }

    @Override
    public void deleteUserInUserData(UserDataEntity user) {
        this.remove(user);
    }
}
