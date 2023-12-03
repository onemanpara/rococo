package guru.qa.rococo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.protobuf.ByteString;
import guru.qa.grpc.rococo.grpc.MuseumResponse;
import guru.qa.rococo.db.model.MuseumEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
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

    public static MuseumJson fromGrpcMessage(MuseumResponse museumResponse) {
        GeoJson geoJson = new GeoJson(
                museumResponse.getGeo().getCity(),
                new CountryJson(UUID.fromString(museumResponse.getGeo().getCountry().getId().toStringUtf8()), null)
        );
        MuseumJson museumJson = new MuseumJson();
        museumJson.setId(UUID.fromString(museumResponse.getId().toStringUtf8()));
        museumJson.setTitle(museumResponse.getTitle());
        museumJson.setDescription(museumResponse.getDescription());
        museumJson.setPhoto(museumResponse.getPhoto().toStringUtf8());
        museumJson.setGeo(geoJson);
        return museumJson;
    }

    public static MuseumJson fromEntity(MuseumEntity entity) {
        MuseumJson museumJson = new MuseumJson();

        museumJson.setId(entity.getId());
        museumJson.setTitle(entity.getTitle());
        museumJson.setGeo(new GeoJson(entity.getCity(), new CountryJson(entity.getGeoId(), null)));
        museumJson.setDescription(entity.getDescription());
        museumJson.setPhoto(ByteString.copyFrom(entity.getPhoto()).toStringUtf8());
        return museumJson;
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
