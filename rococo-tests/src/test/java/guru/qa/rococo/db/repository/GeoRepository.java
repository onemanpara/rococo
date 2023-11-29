package guru.qa.rococo.db.repository;

import guru.qa.rococo.db.model.CountryEntity;

import java.util.List;
import java.util.UUID;

public interface GeoRepository {
    CountryEntity getCountryById(UUID countryId);

    CountryEntity getCountryByName(String countryName);
    List<CountryEntity> getAllCountry();
}
