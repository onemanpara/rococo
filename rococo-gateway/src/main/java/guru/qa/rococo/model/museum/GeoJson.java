package guru.qa.rococo.model.museum;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class GeoJson {

    @JsonProperty("city")
    private String city;
    @JsonProperty("country")
    private CountryJson country;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public CountryJson getCountry() {
        return country;
    }

    public void setCountry(CountryJson country) {
        this.country = country;
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
