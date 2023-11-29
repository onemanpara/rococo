package guru.qa.rococo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.protobuf.ByteString;
import guru.qa.rococo.db.model.MuseumEntity;
import lombok.Getter;
import lombok.Setter;

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

    public static MuseumJson fromEntity(MuseumEntity entity) {
        MuseumJson museumJson = new MuseumJson();

        museumJson.setId(entity.getId());
        museumJson.setTitle(entity.getTitle());
        museumJson.setGeo(new GeoJson(entity.getCity(), new CountryJson(entity.getGeoId(), null)));
        museumJson.setDescription(entity.getDescription());
        museumJson.setPhoto(ByteString.copyFrom(entity.getPhoto()).toStringUtf8());
        return museumJson;
    }
}
