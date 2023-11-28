package guru.qa.rococo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.protobuf.ByteString;
import guru.qa.rococo.db.model.MuseumEntity;
import guru.qa.rococo.util.CountryUtil;
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
        CountryUtil countryUtil = new CountryUtil();
        UUID countryId = entity.getGeoId();
        CountryJson countryJson = new CountryJson(countryId, countryUtil.getCountryNameById(countryId));
        GeoJson geoJson = new GeoJson(entity.getCity(), countryJson);
        MuseumJson museumJson = new MuseumJson();

        museumJson.setId(entity.getId());
        museumJson.setTitle(entity.getTitle());
        museumJson.setGeo(geoJson);
        museumJson.setDescription(entity.getDescription());
        museumJson.setPhoto(ByteString.copyFrom(entity.getPhoto()).toStringUtf8());
        return museumJson;
    }
}
