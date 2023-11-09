package guru.qa.rococo.model.museum;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.grpc.rococo.grpc.Country;

import java.util.Objects;

public class CountryJson {

    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static CountryJson fromGrpcMessage(Country response) {
        CountryJson countryJson = new CountryJson();
        countryJson.setId(response.getUuid().toString());
        countryJson.setName(response.getName());
        return countryJson;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CountryJson that = (CountryJson) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

}
