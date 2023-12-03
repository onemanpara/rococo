package guru.qa.rococo.db.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "artist")
@Getter
@Setter
public class ArtistEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, length = 36)
    private UUID id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String biography;
    @Column(name = "photo", columnDefinition = "bytea", nullable = false)
    private byte[] photo;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArtistEntity artist = (ArtistEntity) o;
        return Objects.equals(id, artist.id) && Objects.equals(name, artist.name) && Objects.equals(biography, artist.biography) && Arrays.equals(photo, artist.photo);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, name, biography);
        result = 31 * result + Arrays.hashCode(photo);
        return result;
    }
}
