package guru.qa.rococo.jupiter.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(METHOD)
public @interface GenerateMuseum {
    String title() default "";

    String country() default "";

    String city() default "";

    String description() default "";
    boolean enrichJsonCountryName() default true;
}
