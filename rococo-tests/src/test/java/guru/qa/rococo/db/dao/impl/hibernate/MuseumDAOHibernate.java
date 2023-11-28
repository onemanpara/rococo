package guru.qa.rococo.db.dao.impl.hibernate;

import guru.qa.rococo.db.ServiceDB;
import guru.qa.rococo.db.dao.MuseumDAO;
import guru.qa.rococo.db.jpa.EntityManagerFactoryProvider;
import guru.qa.rococo.db.jpa.JpaService;
import guru.qa.rococo.db.model.MuseumEntity;

public class MuseumDAOHibernate extends JpaService implements MuseumDAO {
    public MuseumDAOHibernate() {
        super(EntityManagerFactoryProvider.INSTANCE.getDataSource(ServiceDB.MUSEUM).createEntityManager());
    }

    @Override
    public void createMuseum(MuseumEntity museum) {
        this.persist(museum);
    }
}
