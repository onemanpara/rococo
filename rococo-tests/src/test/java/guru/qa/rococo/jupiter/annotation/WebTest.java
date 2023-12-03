package guru.qa.rococo.jupiter.annotation;

import guru.qa.rococo.jupiter.extension.*;
import io.qameta.allure.junit5.AllureJunit5;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
@ExtendWith({DBCreateUserExtension.class,
        DBCreateMuseumExtension.class,
        DBCreateArtistExtension.class,
        DBCreatePaintingExtension.class,
        ApiLoginExtension.class,
        BrowserExtension.class,
        AllureJunit5.class})
public @interface WebTest {
}
