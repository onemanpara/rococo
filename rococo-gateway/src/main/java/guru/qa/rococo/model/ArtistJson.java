package guru.qa.rococo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.grpc.rococo.grpc.GetAristResponse;

import java.util.Objects;
import java.util.UUID;

public class ArtistJson {

    @JsonProperty("id")
    private UUID id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("biography")
    private String biography;
    @JsonProperty("photo")
    private String photo;

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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public static ArtistJson fromGrpcMessage(GetAristResponse response) {
        ArtistJson artist = new ArtistJson();
        String photo = response.getPhoto().toStringUtf8();
        artist.setId(UUID.fromString(response.getUuid().toStringUtf8()));
        artist.setName(response.getName());
        artist.setBiography(response.getBiography());
        artist.setPhoto(photo != null && !photo.isEmpty() ? photo : null);
        return artist;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArtistJson that = (ArtistJson) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(biography, that.biography) && Objects.equals(photo, that.photo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, biography, photo);
    }

}