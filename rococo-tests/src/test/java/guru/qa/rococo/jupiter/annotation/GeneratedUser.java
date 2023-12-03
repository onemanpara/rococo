package guru.qa.rococo.jupiter.annotation;

import java.lang.annotation.Retention;

import static guru.qa.rococo.jupiter.annotation.GeneratedUser.Selector.GENERATE_USER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
public @interface GeneratedUser {
    Selector selector() default GENERATE_USER;

    enum Selector {
        GENERATE_USER, API_LOGIN
    }
}
