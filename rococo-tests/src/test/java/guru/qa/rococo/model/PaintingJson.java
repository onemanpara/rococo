package guru.qa.rococo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.protobuf.ByteString;
import guru.qa.grpc.rococo.grpc.PaintingResponse;
import guru.qa.rococo.db.model.PaintingEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

import static java.util.UUID.fromString;

@Getter
@Setter
public class PaintingJson {
    @JsonProperty("id")
    private UUID id;
    @JsonProperty("title")
    private String title;
    @JsonProperty("description")
    private String description;
    @JsonProperty("content")
    private String content;
    @JsonProperty("museum")
    private MuseumJson museum;
    @JsonProperty("artist")
    private ArtistJson artist;

    public static PaintingJson fromGrpcMessage(PaintingResponse paintingResponse) {
        ArtistJson artist = new ArtistJson();
        artist.setId(fromString(paintingResponse.getArtistId().getId().toStringUtf8()));
        MuseumJson museum = new MuseumJson();
        museum.setId(fromString(paintingResponse.getMuseumId().getId().toStringUtf8()));

        PaintingJson paintingJson = new PaintingJson();
        paintingJson.setId(fromString(paintingResponse.getId().toStringUtf8()));
        paintingJson.setTitle(paintingResponse.getTitle());
        paintingJson.setDescription(paintingResponse.getDescription());
        paintingJson.setContent(paintingResponse.getContent().toStringUtf8());
        paintingJson.setArtist(artist);
        paintingJson.setMuseum(museum);
        return paintingJson;
    }

    public static PaintingJson fromEntity(PaintingEntity entity) {
        MuseumJson museumJson = new MuseumJson();
        ArtistJson artistJson = new ArtistJson();
        museumJson.setId(entity.getMuseumId());
        artistJson.setId(entity.getArtistId());


        PaintingJson paintingJson = new PaintingJson();
        paintingJson.setId(entity.getId());
        paintingJson.setTitle(entity.getTitle());
        paintingJson.setDescription(entity.getDescription());
        paintingJson.setContent(ByteString.copyFrom(entity.getContent()).toStringUtf8());
        paintingJson.setMuseum(museumJson);
        paintingJson.setArtist(artistJson);
        return paintingJson;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaintingJson that = (PaintingJson) o;
        return Objects.equals(id, that.id) && Objects.equals(title, that.title) && Objects.equals(description, that.description) && Objects.equals(content, that.content) && Objects.equals(museum, that.museum) && Objects.equals(artist, that.artist);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, content, museum, artist);
    }
}
