package guru.qa.rococo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.protobuf.ByteString;
import guru.qa.grpc.rococo.grpc.AddPaintingRequest;
import guru.qa.grpc.rococo.grpc.ArtistId;
import guru.qa.grpc.rococo.grpc.MuseumId;
import guru.qa.grpc.rococo.grpc.PaintingResponse;
import guru.qa.rococo.model.museum.MuseumJson;
import lombok.Getter;
import lombok.Setter;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;

import static com.google.protobuf.ByteString.copyFromUtf8;

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
        ArtistJson artist = new ArtistJson(
                UUID.fromString(paintingResponse.getArtistId().getId().toStringUtf8()),
                null,
                null,
                null);
        MuseumJson museum = new MuseumJson(
                UUID.fromString(paintingResponse.getMuseumId().getId().toStringUtf8()),
                null,
                null,
                null,
                null);

        PaintingJson paintingJson = new PaintingJson();
        paintingJson.setId(UUID.fromString(paintingResponse.getId().toStringUtf8()));
        paintingJson.setTitle(paintingResponse.getTitle());
        paintingJson.setDescription(paintingResponse.getDescription());
        paintingJson.setContent(paintingResponse.getContent().toStringUtf8());
        paintingJson.setArtist(artist);
        paintingJson.setMuseum(museum);
        return paintingJson;
    }

    public static AddPaintingRequest toGrpcMessage(PaintingJson paintingJson) {
        return AddPaintingRequest.newBuilder()
                .setTitle(paintingJson.getTitle())
                .setDescription(paintingJson.getDescription())
                .setContent(ByteString.copyFrom(paintingJson.getContent(), StandardCharsets.UTF_8))
                .setMuseumId(MuseumId.newBuilder().setId(copyFromUtf8(paintingJson.getMuseum().id().toString())).build())
                .setArtistId(ArtistId.newBuilder().setId(copyFromUtf8(paintingJson.getArtist().id().toString())).build())
                .build();
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
