package guru.qa.rococo.data;

import com.google.protobuf.ByteString;
import guru.qa.grpc.rococo.grpc.AddPaintingRequest;
import guru.qa.grpc.rococo.grpc.ArtistId;
import guru.qa.grpc.rococo.grpc.MuseumId;
import guru.qa.grpc.rococo.grpc.PaintingResponse;
import jakarta.persistence.*;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

import static com.google.protobuf.ByteString.copyFromUtf8;

@Entity
@Table(name = "painting")
public class PaintingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, length = 36)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(columnDefinition = "bytea")
    private byte[] content;

    @Column(name = "museum_id", nullable = false, length = 36)
    private UUID museumId;

    @Column(name = "artist_id", nullable = false, length = 36)
    private UUID artistId;

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

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public UUID getMuseumId() {
        return museumId;
    }

    public void setMuseumId(UUID museumId) {
        this.museumId = museumId;
    }

    public UUID getArtistId() {
        return artistId;
    }

    public void setArtistId(UUID artistId) {
        this.artistId = artistId;
    }

    public static PaintingEntity fromGrpcMessage(AddPaintingRequest request) {
        PaintingEntity entity = new PaintingEntity();
        entity.setTitle(request.getTitle());
        entity.setDescription(request.getDescription());
        entity.setContent(request.getContent().toByteArray());
        entity.setArtistId(UUID.fromString(request.getArtistId().getId().toStringUtf8()));
        entity.setMuseumId(UUID.fromString(request.getMuseumId().getId().toStringUtf8()));
        return entity;
    }

    public static PaintingResponse toGrpcMessage(PaintingEntity entity) {
        return PaintingResponse.newBuilder()
                .setId(copyFromUtf8(entity.getId().toString()))
                .setTitle(entity.getTitle())
                .setDescription(entity.getDescription())
                .setContent(ByteString.copyFrom(entity.getContent()))
                .setArtistId(ArtistId.newBuilder().setId(copyFromUtf8(entity.getArtistId().toString())).build())
                .setMuseumId(MuseumId.newBuilder().setId(copyFromUtf8(entity.getMuseumId().toString())).build())
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaintingEntity that = (PaintingEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(title, that.title) && Objects.equals(description, that.description) && Arrays.equals(content, that.content) && Objects.equals(museumId, that.museumId) && Objects.equals(artistId, that.artistId);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, title, description, museumId, artistId);
        result = 31 * result + Arrays.hashCode(content);
        return result;
    }
}
