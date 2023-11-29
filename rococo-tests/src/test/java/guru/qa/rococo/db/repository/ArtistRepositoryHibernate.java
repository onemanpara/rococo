package guru.qa.rococo.db.repository;

import guru.qa.rococo.db.dao.ArtistDAO;
import guru.qa.rococo.db.dao.impl.hibernate.ArtistDAOHibernate;
import guru.qa.rococo.db.model.ArtistEntity;

public class ArtistRepositoryHibernate implements ArtistRepository {

    private final ArtistDAO artistDAO = new ArtistDAOHibernate();

    @Override
    public void createArtistForTest(ArtistEntity artist) {
        artistDAO.createArtist(artist);
    }

    @Override
    public void deleteArtist(ArtistEntity artist) {
        artistDAO.deleteArtist(artist);
    }
}
