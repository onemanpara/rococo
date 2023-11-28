package guru.qa.rococo.db.repository;

import guru.qa.rococo.db.model.MuseumEntity;

public interface MuseumRepository {
    void createMuseumForTest(MuseumEntity museum);
}
