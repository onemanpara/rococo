package guru.qa.rococo.model.museum;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.grpc.rococo.grpc.CountryId;
import guru.qa.grpc.rococo.grpc.Geo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

import static com.google.protobuf.ByteString.copyFromUtf8;

@Getter
@Setter
@AllArgsConstructor
public class GeoJson {

    @JsonProperty("city")
    private String city;
    @JsonProperty("country")
    private CountryJson country;

    public static Geo toGrpcMessage(GeoJson geo) {
        CountryId country = CountryId.newBuilder()
                .setId(copyFromUtf8(geo.getCountry().id().toString()))
                .build();
        return Geo.newBuilder()
                .setCity(geo.getCity())
                .setCountry(country)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeoJson geoJson = (GeoJson) o;
        return Objects.equals(city, geoJson.city) && Objects.equals(country, geoJson.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city, country);
    }

}
