package guru.qa.rococo.db.model.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "user")
@Getter
@Setter
public class UserDataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, columnDefinition = "UUID default gen_random_uuid()")
    private UUID id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column
    private String firstname;
    @Column
    private String lastname;
    @Column(columnDefinition = "bytea")
    private byte[] avatar;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDataEntity that = (UserDataEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(username, that.username) && Objects.equals(firstname, that.firstname) && Objects.equals(lastname, that.lastname) && Arrays.equals(avatar, that.avatar);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, username, firstname, lastname);
        result = 31 * result + Arrays.hashCode(avatar);
        return result;
    }
}
