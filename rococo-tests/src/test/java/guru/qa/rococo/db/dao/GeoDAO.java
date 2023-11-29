package guru.qa.rococo.db.dao;

import guru.qa.rococo.db.model.CountryEntity;

import java.util.List;
import java.util.UUID;

public interface GeoDAO {
    CountryEntity getCountryById(UUID countryId);

    CountryEntity getCountryName(String countryName);
    List<CountryEntity> getAllCountry();
}
