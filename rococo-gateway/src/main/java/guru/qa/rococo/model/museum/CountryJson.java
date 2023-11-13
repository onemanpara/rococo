package guru.qa.rococo.model.museum;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.grpc.rococo.grpc.CountryResponse;

import java.util.UUID;

public record CountryJson(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("name")
        String name
) {

    public static CountryJson fromGrpcMessage(CountryResponse response) {
        return new CountryJson(
                UUID.fromString(response.getId().toStringUtf8()),
                response.getName()
        );
    }

}
