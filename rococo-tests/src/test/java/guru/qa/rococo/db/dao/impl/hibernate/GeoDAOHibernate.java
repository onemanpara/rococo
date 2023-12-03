package guru.qa.rococo.db.dao.impl.hibernate;

import guru.qa.rococo.db.ServiceDB;
import guru.qa.rococo.db.dao.GeoDAO;
import guru.qa.rococo.db.jpa.EntityManagerFactoryProvider;
import guru.qa.rococo.db.jpa.JpaService;
import guru.qa.rococo.db.model.CountryEntity;

import java.util.List;
import java.util.UUID;

public class GeoDAOHibernate extends JpaService implements GeoDAO {

    public GeoDAOHibernate() {
        super(EntityManagerFactoryProvider.INSTANCE.getDataSource(ServiceDB.GEO).createEntityManager());
    }

    @Override
    public CountryEntity getCountryById(UUID countryId) {
        return em.createQuery("select country from CountryEntity country where country.id=:countryId", CountryEntity.class)
                .setParameter("countryId", countryId)
                .getSingleResult();
    }

    @Override
    public CountryEntity getCountryName(String countryName) {
        return em.createQuery("select country from CountryEntity country where country.name=:countryName", CountryEntity.class)
                .setParameter("countryName", countryName)
                .getSingleResult();
    }

    @Override
    public List<CountryEntity> getAllCountry() {
        return em.createQuery("select c from CountryEntity c", CountryEntity.class)
                .getResultList();
    }

}
