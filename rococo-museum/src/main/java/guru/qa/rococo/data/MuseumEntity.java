package guru.qa.rococo.data;

import jakarta.persistence.*;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "museum")
public class MuseumEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, length = 36)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String title;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(nullable = false)
    private String city;

    @Column(name = "photo", columnDefinition = "bytea")
    private byte[] photo;

    @ManyToOne
    @JoinColumn(name = "geo_id")
    private CountryEntity country;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public CountryEntity getCountry() {
        return country;
    }

    public void setCountry(CountryEntity country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MuseumEntity museum = (MuseumEntity) o;
        return Objects.equals(id, museum.id) && Objects.equals(title, museum.title) && Objects.equals(description, museum.description) && Objects.equals(city, museum.city) && Arrays.equals(photo, museum.photo) && Objects.equals(country, museum.country);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, title, description, city, country);
        result = 31 * result + Arrays.hashCode(photo);
        return result;
    }

}
