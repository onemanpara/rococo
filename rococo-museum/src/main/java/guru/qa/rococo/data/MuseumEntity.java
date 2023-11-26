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
@Table(name = "museum")
public class MuseumEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, length = 36)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(nullable = false)
    private String city;

    @Column(name = "photo", columnDefinition = "bytea")
    private byte[] photo;

    @Column(name = "geo_id")
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MuseumEntity entity = (MuseumEntity) o;
        return Objects.equals(id, entity.id) && Objects.equals(title, entity.title) && Objects.equals(description, entity.description) && Objects.equals(city, entity.city) && Arrays.equals(photo, entity.photo) && Objects.equals(geoId, entity.geoId);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, title, description, city, geoId);
        result = 31 * result + Arrays.hashCode(photo);
        return result;
    }
}
