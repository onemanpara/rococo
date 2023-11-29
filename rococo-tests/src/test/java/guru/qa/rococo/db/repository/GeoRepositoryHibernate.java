package guru.qa.rococo.db.repository;

import guru.qa.rococo.db.dao.GeoDAO;
import guru.qa.rococo.db.dao.impl.hibernate.GeoDAOHibernate;
import guru.qa.rococo.db.model.CountryEntity;

import java.util.List;
import java.util.UUID;

public class GeoRepositoryHibernate implements GeoRepository {
    private final GeoDAO geoDAO = new GeoDAOHibernate();

    @Override
    public CountryEntity getCountryById(UUID countryId) {
        return geoDAO.getCountryById(countryId);
    }

    @Override
    public CountryEntity getCountryByName(String countryName) {
        return geoDAO.getCountryName(countryName);
    }

    @Override
    public List<CountryEntity> getAllCountry() {
        return geoDAO.getAllCountry();
    }
}
