package guru.qa.rococo.db.repository;

import guru.qa.rococo.db.dao.ArtistDAO;
import guru.qa.rococo.db.dao.impl.hibernate.ArtistDAOHibernate;
import guru.qa.rococo.db.model.ArtistEntity;

public class ArtistRepositoryHibernate implements ArtistRepository {

    private final ArtistDAO artistDAO = new ArtistDAOHibernate();

    @Override
    public void createArtistForTest(ArtistEntity artist) {
        this.artistDAO.createArtist(artist);
    }
}
