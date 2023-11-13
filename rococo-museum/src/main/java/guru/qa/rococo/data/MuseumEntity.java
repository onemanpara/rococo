package guru.qa.rococo.data;

import com.google.protobuf.ByteString;
import guru.qa.grpc.rococo.grpc.AddMuseumRequest;
import guru.qa.grpc.rococo.grpc.CountryId;
import guru.qa.grpc.rococo.grpc.Geo;
import guru.qa.grpc.rococo.grpc.MuseumResponse;
import jakarta.persistence.*;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

import static com.google.protobuf.ByteString.copyFromUtf8;

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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public UUID getGeoId() {
        return geoId;
    }

    public void setGeoId(UUID geoId) {
        this.geoId = geoId;
    }

    public static MuseumEntity fromAddGrpcMessage(AddMuseumRequest request) {
        MuseumEntity entity = new MuseumEntity();
        entity.setTitle(request.getTitle());
        entity.setDescription(request.getDescription());
        entity.setCity(request.getGeo().getCity());
        entity.setPhoto(request.getPhoto().toByteArray());
        entity.setGeoId(UUID.fromString(request.getGeo().getCountry().getId().toStringUtf8()));
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
        MuseumEntity museum = (MuseumEntity) o;
        return Objects.equals(id, museum.id) && Objects.equals(title, museum.title) && Objects.equals(description, museum.description) && Objects.equals(city, museum.city) && Arrays.equals(photo, museum.photo) && Objects.equals(geoId, museum.geoId);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, title, description, city, geoId);
        result = 31 * result + Arrays.hashCode(photo);
        return result;
    }

}
