package guru.qa.rococo.data;

import com.google.protobuf.ByteString;
import guru.qa.grpc.rococo.grpc.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

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
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        PaintingEntity that = (PaintingEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

}
