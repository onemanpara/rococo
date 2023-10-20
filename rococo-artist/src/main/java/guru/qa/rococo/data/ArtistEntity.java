package guru.qa.rococo.data;

import jakarta.persistence.*;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "artist")
public class ArtistEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, length = 36)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column()
    private String biography;

    @Column(name = "photo", columnDefinition = "bytea")
    private byte[] photo;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

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
