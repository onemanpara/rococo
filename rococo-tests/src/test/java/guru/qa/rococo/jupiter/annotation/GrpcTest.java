package guru.qa.rococo.jupiter.annotation;

import guru.qa.rococo.jupiter.extension.DBCreateArtistExtension;
import guru.qa.rococo.jupiter.extension.DBCreateMuseumExtension;
import guru.qa.rococo.jupiter.extension.DBCreatePaintingExtension;
import guru.qa.rococo.jupiter.extension.DBCreateUserExtension;
import io.qameta.allure.junit5.AllureJunit5;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
@ExtendWith({DBCreateMuseumExtension.class,
        DBCreateArtistExtension.class,
        DBCreatePaintingExtension.class,
        DBCreateUserExtension.class,
        AllureJunit5.class})
public @interface GrpcTest {
}
