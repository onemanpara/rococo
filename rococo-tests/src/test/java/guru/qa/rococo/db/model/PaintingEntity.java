package guru.qa.rococo.db.model;

import guru.qa.rococo.model.PaintingJson;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "painting")
public class PaintingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, length = 36)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(columnDefinition = "bytea", nullable = false)
    private byte[] content;

    @Column(name = "museum_id", nullable = false, length = 36)
    private UUID museumId;

    @Column(name = "artist_id", nullable = false, length = 36)
    private UUID artistId;

    public static PaintingEntity fromJson(PaintingJson paintingJson) {
        PaintingEntity paintingEntity = new PaintingEntity();
        paintingEntity.setId(paintingJson.getId());
        paintingEntity.setTitle(paintingJson.getTitle());
        paintingEntity.setDescription(paintingJson.getDescription());
        paintingEntity.setContent(paintingJson.getContent().getBytes());
        paintingEntity.setMuseumId(paintingJson.getMuseum().getId());
        paintingEntity.setArtistId(paintingJson.getArtist().getId());
        return paintingEntity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaintingEntity that = (PaintingEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(title, that.title) && Objects.equals(description, that.description) && Arrays.equals(content, that.content) && Objects.equals(museumId, that.museumId) && Objects.equals(artistId, that.artistId);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, title, description, museumId, artistId);
        result = 31 * result + Arrays.hashCode(content);
        return result;
    }
}
