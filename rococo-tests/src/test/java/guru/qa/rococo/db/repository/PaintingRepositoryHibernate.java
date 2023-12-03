package guru.qa.rococo.db.repository;

import guru.qa.rococo.db.dao.PaintingDAO;
import guru.qa.rococo.db.dao.impl.hibernate.PaintingDAOHibernate;
import guru.qa.rococo.db.model.PaintingEntity;

public class PaintingRepositoryHibernate implements PaintingRepository {
    private final PaintingDAO paintingDAO = new PaintingDAOHibernate();

    @Override
    public void createPaintingForTest(PaintingEntity painting) {
        paintingDAO.createPainting(painting);
    }

    @Override
    public void deletePainting(PaintingEntity painting) {
        paintingDAO.deletePainting(painting);
    }
}
