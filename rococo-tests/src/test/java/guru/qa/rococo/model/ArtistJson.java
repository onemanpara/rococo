package guru.qa.rococo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.protobuf.ByteString;
import guru.qa.rococo.db.model.ArtistEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ArtistJson {
    @JsonProperty("id")
    private UUID id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("biography")
    private String biography;
    @JsonProperty("photo")
    private String photo;

    public static ArtistJson fromEntity(ArtistEntity entity) {
        ArtistJson artistJson = new ArtistJson();
        artistJson.setId(entity.getId());
        artistJson.setName(entity.getName());
        artistJson.setBiography(entity.getBiography());
        artistJson.setPhoto(ByteString.copyFrom(entity.getPhoto()).toStringUtf8());
        return artistJson;
    }
}
