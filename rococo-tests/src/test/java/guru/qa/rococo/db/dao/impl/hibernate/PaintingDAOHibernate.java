package guru.qa.rococo.db.dao.impl.hibernate;

import guru.qa.rococo.db.ServiceDB;
import guru.qa.rococo.db.dao.PaintingDAO;
import guru.qa.rococo.db.jpa.EntityManagerFactoryProvider;
import guru.qa.rococo.db.jpa.JpaService;
import guru.qa.rococo.db.model.PaintingEntity;

public class PaintingDAOHibernate extends JpaService implements PaintingDAO {
    public PaintingDAOHibernate() {
        super(EntityManagerFactoryProvider.INSTANCE.getDataSource(ServiceDB.PAINTING).createEntityManager());
    }

    @Override
    public void createPainting(PaintingEntity painting) {
        persist(painting);
    }

    @Override
    public void deletePainting(PaintingEntity painting) {
        remove(painting);
    }
}
