package guru.qa.rococo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.rococo.data.MuseumEntity;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;

public class MuseumJson {
    @JsonProperty("id")
    private UUID id;
    @JsonProperty("title")
    private String title;
    @JsonProperty("description")
    private String description;
    @JsonProperty("photo")
    private String photo;

    @JsonProperty("geo")
    private GeoJson geo;

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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public GeoJson getGeo() {
        return geo;
    }

    public void setGeo(GeoJson geo) {
        this.geo = geo;
    }

    public static MuseumJson fromEntity(MuseumEntity entity) {
        MuseumJson museum = new MuseumJson();
        byte[] photo = entity.getPhoto();
        GeoJson geo = new GeoJson();
        geo.setCity(entity.getCity());
        geo.setCountry(CountryJson.fromEntity(entity.getCountry()));

        museum.setId(entity.getId());
        museum.setTitle(entity.getTitle());
        museum.setDescription(entity.getDescription());
        museum.setGeo(geo);
        museum.setPhoto(photo != null && photo.length > 0 ? new String(entity.getPhoto(), StandardCharsets.UTF_8) : null);
        return museum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MuseumJson that = (MuseumJson) o;
        return Objects.equals(id, that.id) && Objects.equals(title, that.title) && Objects.equals(description, that.description) && Objects.equals(photo, that.photo) && Objects.equals(geo, that.geo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, photo, geo);
    }

}
