package guru.qa.rococo.db.dao;

import guru.qa.rococo.db.model.PaintingEntity;

public interface PaintingDAO {
    void createPainting(PaintingEntity painting);
    void deletePainting(PaintingEntity painting);
}
