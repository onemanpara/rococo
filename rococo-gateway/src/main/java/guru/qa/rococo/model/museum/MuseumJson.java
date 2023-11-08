package guru.qa.rococo.model.museum;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.grpc.rococo.grpc.GetArtistResponse;
import guru.qa.grpc.rococo.grpc.GetMuseumResponse;
import guru.qa.rococo.model.ArtistJson;

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

    public static MuseumJson fromGrpcMessage(GetMuseumResponse response) {
        MuseumJson museum = new MuseumJson();
        String photo = response.getPhoto().toStringUtf8();

        GeoJson geoJson = new GeoJson();
        geoJson.setCity(response.getGeo().getCity());
        geoJson.setCountry(CountryJson.fromGrpcMessage(response));

        museum.setId(UUID.fromString(response.getUuid().toStringUtf8()));
        museum.setTitle(response.getTitle());
        museum.setDescription(response.getDescription());
        museum.setGeo(geoJson);
        museum.setPhoto(photo != null && !photo.isEmpty() ? photo : null);
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
