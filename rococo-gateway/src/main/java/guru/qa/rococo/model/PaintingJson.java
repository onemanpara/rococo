package guru.qa.rococo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.protobuf.ByteString;
import guru.qa.grpc.rococo.grpc.AddPaintingRequest;
import guru.qa.grpc.rococo.grpc.ArtistId;
import guru.qa.grpc.rococo.grpc.MuseumId;
import guru.qa.grpc.rococo.grpc.PaintingResponse;
import guru.qa.rococo.model.museum.MuseumJson;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static com.google.protobuf.ByteString.copyFromUtf8;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MuseumJson getMuseum() {
        return museum;
    }

    public void setMuseum(MuseumJson museum) {
        this.museum = museum;
    }

    public ArtistJson getArtist() {
        return artist;
    }

    public void setArtist(ArtistJson artist) {
        this.artist = artist;
    }

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

}
