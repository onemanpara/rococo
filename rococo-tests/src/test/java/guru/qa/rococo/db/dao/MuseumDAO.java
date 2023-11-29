package guru.qa.rococo.db.dao;

import guru.qa.rococo.db.model.MuseumEntity;

public interface MuseumDAO {
    void createMuseum(MuseumEntity museum);
    void deleteMuseum(MuseumEntity museum);
}
