package guru.qa.rococo.db.repository;

import guru.qa.rococo.db.model.ArtistEntity;

public interface ArtistRepository {
    void createArtistForTest(ArtistEntity artist);
}
