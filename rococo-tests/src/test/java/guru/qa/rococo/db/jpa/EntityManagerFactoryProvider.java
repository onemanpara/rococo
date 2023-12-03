package guru.qa.rococo.db.jpa;

import guru.qa.rococo.config.Config;
import guru.qa.rococo.db.ServiceDB;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum EntityManagerFactoryProvider {
    INSTANCE;

    private static final Config cfg = Config.getConfig();

    private final Map<ServiceDB, EntityManagerFactory> dataSourceStore = new ConcurrentHashMap<>();

    public EntityManagerFactory getDataSource(ServiceDB db) {
        return dataSourceStore.computeIfAbsent(db, key -> {
            Map<String, Object> props = new HashMap<>();
            props.put("hibernate.connection.url", db.p6spyUrl());
            props.put("hibernate.connection.user", cfg.databaseUser());
            props.put("hibernate.connection.password", cfg.databasePassword());
            props.put("hibernate.connection.driver_class", "com.p6spy.engine.spy.P6SpyDriver");
            props.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");

            EntityManagerFactory entityManagerFactory =
                    new ThreadLocalEntityManagerFactory(
                            Persistence.createEntityManagerFactory("rococo", props)
                    );
            return entityManagerFactory;
        });
    }

    public Collection<EntityManagerFactory> allStoredEntityManagerFactories() {
        return dataSourceStore.values();
    }
}
