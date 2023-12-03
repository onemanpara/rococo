package guru.qa.rococo.db.dao.impl.hibernate;

import guru.qa.rococo.db.ServiceDB;
import guru.qa.rococo.db.dao.ArtistDAO;
import guru.qa.rococo.db.jpa.EntityManagerFactoryProvider;
import guru.qa.rococo.db.jpa.JpaService;
import guru.qa.rococo.db.model.ArtistEntity;

public class ArtistDAOHibernate extends JpaService implements ArtistDAO {
    public ArtistDAOHibernate() {
        super(EntityManagerFactoryProvider.INSTANCE.getDataSource(ServiceDB.ARTIST).createEntityManager());
    }

    @Override
    public void createArtist(ArtistEntity artist) {
        persist(artist);
    }

    @Override
    public void deleteArtist(ArtistEntity artist) {
        remove(artist);
    }
}
