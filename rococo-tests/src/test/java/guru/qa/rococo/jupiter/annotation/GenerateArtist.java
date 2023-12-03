package guru.qa.rococo.jupiter.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(METHOD)
public @interface GenerateArtist {
    String name() default "";

    String biography() default "";

    int paintingCount() default 0;
}
