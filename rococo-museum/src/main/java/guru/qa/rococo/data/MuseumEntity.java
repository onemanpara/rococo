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
@Table(name = "museum")
public class MuseumEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, length = 36)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "text", nullable = false)
    private String description;

    @Column(nullable = false)
    private String city;

    @Column(name = "photo", columnDefinition = "bytea", nullable = false)
    private byte[] photo;

    @Column(name = "geo_id", nullable = false)
    private UUID geoId;

    public static MuseumEntity fromAddMuseumGrpcMessage(AddMuseumRequest request) {
        MuseumEntity entity = new MuseumEntity();
        entity.setTitle(request.getTitle());
        entity.setDescription(request.getDescription());
        entity.setCity(request.getGeo().getCity());
        entity.setPhoto(request.getPhoto().toByteArray());
        entity.setGeoId(UUID.fromString(request.getGeo().getCountry().getId().toStringUtf8()));
        return entity;
    }

    public static MuseumEntity fromUpdateMuseumGrpcMessage(UpdateMuseumRequest request) {
        MuseumEntity entity = fromAddMuseumGrpcMessage(request.getMuseumData());
        entity.setId(UUID.fromString(request.getId().toStringUtf8()));
        return entity;
    }

    public static MuseumResponse toGrpcMessage(MuseumEntity entity) {
        CountryId countryId = CountryId.newBuilder()
                .setId(copyFromUtf8(entity.getGeoId().toString()))
                .build();
        Geo geo = Geo.newBuilder()
                .setCity(entity.getCity())
                .setCountry(countryId)
                .build();
        return MuseumResponse.newBuilder()
                .setId(copyFromUtf8(entity.getId().toString()))
                .setTitle(entity.getTitle())
                .setDescription(entity.getDescription())
                .setGeo(geo)
                .setPhoto(ByteString.copyFrom(entity.getPhoto()))
                .build();
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        MuseumEntity that = (MuseumEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

}
