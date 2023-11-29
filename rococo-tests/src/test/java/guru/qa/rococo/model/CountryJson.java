package guru.qa.rococo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.rococo.db.model.CountryEntity;

import java.util.UUID;

public record CountryJson(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("name")
        String name) {

    public static CountryJson fromEntity(CountryEntity entity) {
        return new CountryJson(entity.getId(), entity.getName());
    }

}
