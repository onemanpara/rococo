package guru.qa.rococo.jupiter.extension;

import guru.qa.rococo.db.jpa.EntityManagerFactoryProvider;
import jakarta.persistence.EntityManagerFactory;

public class JpaExtension implements SuiteExtension {

    @Override
    public void afterAllTests() {
        EntityManagerFactoryProvider.INSTANCE.allStoredEntityManagerFactories()
                .forEach(EntityManagerFactory::close);
    }

}