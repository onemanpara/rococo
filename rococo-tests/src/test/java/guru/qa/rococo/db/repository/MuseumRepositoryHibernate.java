package guru.qa.rococo.db.repository;

import guru.qa.rococo.db.dao.MuseumDAO;
import guru.qa.rococo.db.dao.impl.hibernate.MuseumDAOHibernate;
import guru.qa.rococo.db.model.MuseumEntity;

public class MuseumRepositoryHibernate implements MuseumRepository {

    private final MuseumDAO museumDAO = new MuseumDAOHibernate();

    @Override
    public void createMuseumForTest(MuseumEntity museum) {
        this.museumDAO.createMuseum(museum);
    }
}
