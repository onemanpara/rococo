package guru.qa.rococo.db.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "museum")
@Getter
@Setter
public class MuseumEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, length = 36)
    private UUID id;
    @Column(nullable = false)
    private String title;
    @Column(name = "description", columnDefinition = "text", nullable = false)
    private String description;
    @Column(nullable = false)
    private String city;
    @Column(name = "photo", columnDefinition = "bytea", nullable = false)
    private byte[] photo;
    @Column(name = "geo_id", nullable = false)
    private UUID geoId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MuseumEntity that = (MuseumEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(title, that.title) && Objects.equals(description, that.description) && Objects.equals(city, that.city) && Arrays.equals(photo, that.photo) && Objects.equals(geoId, that.geoId);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, title, description, city, geoId);
        result = 31 * result + Arrays.hashCode(photo);
        return result;
    }
}
