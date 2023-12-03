package guru.qa.rococo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.rococo.db.model.user.AuthUserEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
@Getter
@Setter
public class UserJson {
    @JsonProperty("id")
    private UUID id;
    @JsonProperty("username")
    private String username;
    @JsonProperty("firstname")
    private String firstname;
    @JsonProperty("surname")
    private String surname;
    @JsonProperty("avatar")
    private String avatar;
    transient String password;

    public static UserJson fromEntity(AuthUserEntity entity) {
        UserJson user = new UserJson();
        user.setId(entity.getId());
        user.setUsername(entity.getUsername());
        user.setPassword(entity.getEncodedPassword());
        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserJson userJson = (UserJson) o;
        return Objects.equals(id, userJson.id) && Objects.equals(username, userJson.username) && Objects.equals(firstname, userJson.firstname) && Objects.equals(surname, userJson.surname) && Objects.equals(avatar, userJson.avatar) && Objects.equals(password, userJson.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, firstname, surname, avatar, password);
    }
}
