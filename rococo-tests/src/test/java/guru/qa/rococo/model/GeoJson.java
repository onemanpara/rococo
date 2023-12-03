package guru.qa.rococo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
public class GeoJson {
    @JsonProperty("city")
    private String city;
    @JsonProperty("country")
    private CountryJson country;

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
