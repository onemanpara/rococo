package guru.qa.rococo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.protobuf.ByteString;
import guru.qa.grpc.rococo.grpc.AddArtistRequest;
import guru.qa.grpc.rococo.grpc.ArtistResponse;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public record ArtistJson(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("name")
        String name,
        @JsonProperty("biography")
        String biography,
        @JsonProperty("photo")
        String photo
) {

    public static ArtistJson fromGrpcMessage(ArtistResponse response) {
        return new ArtistJson(
                UUID.fromString(response.getId().toStringUtf8()),
                response.getName(),
                response.getBiography(),
                response.getPhoto().toStringUtf8()
        );
    }

    public static AddArtistRequest toGrpcMessage(ArtistJson artistJson) {
        return AddArtistRequest.newBuilder()
                .setName(artistJson.name)
                .setBiography(artistJson.biography)
                .setPhoto(ByteString.copyFrom(artistJson.photo(), StandardCharsets.UTF_8))
                .build();
    }

}