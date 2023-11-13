package guru.qa.rococo.model.museum;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.protobuf.ByteString;
import guru.qa.grpc.rococo.grpc.AddMuseumRequest;
import guru.qa.grpc.rococo.grpc.MuseumResponse;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public record MuseumJson(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("title")
        String title,
        @JsonProperty("description")
        String description,
        @JsonProperty("photo")
        String photo,
        @JsonProperty("geo")
        GeoJson geo
) {

    public static MuseumJson fromGrpcMessage(MuseumResponse museumResponse) {
        GeoJson geoJson = new GeoJson(
                museumResponse.getGeo().getCity(),
                new CountryJson(UUID.fromString(museumResponse.getGeo().getCountry().getId().toStringUtf8()), null)
        );
        return new MuseumJson(
                UUID.fromString(museumResponse.getId().toStringUtf8()),
                museumResponse.getTitle(),
                museumResponse.getDescription(),
                museumResponse.getPhoto().toStringUtf8(),
                geoJson);
    }

    public static AddMuseumRequest toGrpcMessage(MuseumJson museumJson) {
        return AddMuseumRequest.newBuilder()
                .setTitle(museumJson.title())
                .setDescription(museumJson.description())
                .setPhoto(ByteString.copyFrom(museumJson.photo(), StandardCharsets.UTF_8))
                .setGeo(GeoJson.toGrpcMessage(museumJson.geo()))
                .build();
    }

}
