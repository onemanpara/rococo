package guru.qa.rococo.data;

import com.google.protobuf.ByteString;
import guru.qa.grpc.rococo.grpc.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

import static com.google.protobuf.ByteString.copyFromUtf8;

@Getter
@Setter
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

    @Column(columnDefinition = "bytea", nullable = false)
    private byte[] content;

    @Column(name = "museum_id", nullable = false, length = 36)
    private UUID museumId;

    @Column(name = "artist_id", nullable = false, length = 36)
    private UUID artistId;

    public static PaintingEntity fromAddPaintingGrpcMessage(AddPaintingRequest request) {
        PaintingEntity entity = new PaintingEntity();
        entity.setTitle(request.getTitle());
        entity.setDescription(request.getDescription());
        entity.setContent(request.getContent().toByteArray());
        entity.setArtistId(UUID.fromString(request.getArtistId().getId().toStringUtf8()));
        entity.setMuseumId(UUID.fromString(request.getMuseumId().getId().toStringUtf8()));
        return entity;
    }

    public static PaintingEntity fromUpdatePaintingGrpcMessage(UpdatePaintingRequest request) {
        PaintingEntity entity = fromAddPaintingGrpcMessage(request.getPaintingData());
        entity.setId(UUID.fromString(request.getId().toStringUtf8()));
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
