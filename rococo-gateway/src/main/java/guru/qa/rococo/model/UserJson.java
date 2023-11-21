package guru.qa.rococo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.grpc.rococo.grpc.UserResponse;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record UserJson(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("username")
        String username,
        @JsonProperty("firstname")
        @Size(max = 30, message = "Имя не может быть длиннее 30 символов")
        String firstname,
        @JsonProperty("lastname")
        @Size(max = 50, message = "Фамилия не может быть длиннее 30 символов")
        String lastname,
        @JsonProperty("avatar")
        String avatar
) {
    public static UserJson fromGrpcMessage(UserResponse response) {
        return new UserJson(
                UUID.fromString(response.getId().toStringUtf8()),
                response.getUsername(),
                response.getFirstname(),
                response.getLastname(),
                response.getAvatar().toStringUtf8()
        );
    }
}
