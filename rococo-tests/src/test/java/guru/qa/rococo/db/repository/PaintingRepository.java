package guru.qa.rococo.db.repository;

import guru.qa.rococo.db.model.PaintingEntity;

public interface PaintingRepository {
    void createPaintingForTest(PaintingEntity painting);
    void deletePainting(PaintingEntity painting);
}
