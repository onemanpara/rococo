package guru.qa.rococo.jupiter.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(METHOD)
public @interface GeneratePainting {
    GenerateArtist artist() default @GenerateArtist();

    GenerateMuseum museum() default @GenerateMuseum();

    String title() default "";

    String description() default "";
}
