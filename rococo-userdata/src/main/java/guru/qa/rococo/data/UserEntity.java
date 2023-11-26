package guru.qa.rococo.data;

import com.google.protobuf.ByteString;
import guru.qa.grpc.rococo.grpc.UserResponse;
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
@Table(name = "user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, length = 36)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column()
    private String firstname;

    @Column()
    private String lastname;

    @Column(name = "avatar", columnDefinition = "bytea")
    private byte[] avatar;

    public static UserResponse toGrpcMessage(UserEntity entity) {
        UserResponse.Builder builder = UserResponse.newBuilder()
                .setId(copyFromUtf8(entity.getId().toString()))
                .setUsername(entity.getUsername());

        if (entity.getFirstname() != null) {
            builder.setFirstname(entity.getFirstname());
        }
        if (entity.getLastname() != null) {
            builder.setLastname(entity.getLastname());
        }
        if (entity.getAvatar() != null) {
            builder.setAvatar(ByteString.copyFrom(entity.getAvatar()));
        }

        return builder.build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(username, that.username) && Objects.equals(firstname, that.firstname) && Objects.equals(lastname, that.lastname) && Arrays.equals(avatar, that.avatar);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, username, firstname, lastname);
        result = 31 * result + Arrays.hashCode(avatar);
        return result;
    }
}
