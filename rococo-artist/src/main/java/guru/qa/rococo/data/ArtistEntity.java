package guru.qa.rococo.data;

import guru.qa.grpc.rococo.grpc.AddArtistRequest;
import guru.qa.grpc.rococo.grpc.ArtistResponse;
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
@Table(name = "artist")
public class ArtistEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, length = 36)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column()
    private String biography;

    @Column(name = "photo", columnDefinition = "bytea")
    private byte[] photo;

    public static ArtistEntity fromGrpcMessage(AddArtistRequest request) {
        ArtistEntity entity = new ArtistEntity();
        entity.setName(request.getName());
        entity.setBiography(request.getBiography());
        entity.setPhoto(request.getPhoto().toByteArray());
        return entity;
    }

    public static ArtistResponse toGrpcMessage(ArtistEntity entity) {
        return ArtistResponse.newBuilder()
                .setId(copyFromUtf8(entity.getId().toString()))
                .setName(entity.getName())
                .setBiography(entity.getBiography())
                .build();
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        ArtistEntity that = (ArtistEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

}
