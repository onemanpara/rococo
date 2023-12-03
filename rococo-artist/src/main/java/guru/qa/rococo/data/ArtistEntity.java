package guru.qa.rococo.data;

import com.google.protobuf.ByteString;
import guru.qa.grpc.rococo.grpc.AddArtistRequest;
import guru.qa.grpc.rococo.grpc.ArtistResponse;
import guru.qa.grpc.rococo.grpc.UpdateArtistRequest;
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
@Table(name = "artist")
public class ArtistEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, length = 36)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String biography;

    @Column(name = "photo", columnDefinition = "bytea", nullable = false)
    private byte[] photo;

    public static ArtistEntity fromAddArtistGrpcMessage(AddArtistRequest request) {
        ArtistEntity entity = new ArtistEntity();
        entity.setName(request.getName());
        entity.setBiography(request.getBiography());
        entity.setPhoto(request.getPhoto().toByteArray());
        return entity;
    }

    public static ArtistEntity fromUpdateArtistGrpcMessage(UpdateArtistRequest request) {
        ArtistEntity entity = fromAddArtistGrpcMessage(request.getArtistData());
        entity.setId(UUID.fromString(request.getId().toStringUtf8()));
        return entity;
    }

    public static ArtistResponse toGrpcMessage(ArtistEntity entity) {
        return ArtistResponse.newBuilder()
                .setId(copyFromUtf8(entity.getId().toString()))
                .setName(entity.getName())
                .setBiography(entity.getBiography())
                .setPhoto(ByteString.copyFrom(entity.getPhoto()))
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArtistEntity that = (ArtistEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(biography, that.biography) && Arrays.equals(photo, that.photo);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, name, biography);
        result = 31 * result + Arrays.hashCode(photo);
        return result;
    }
}
