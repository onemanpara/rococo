package guru.qa.rococo.jupiter.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(METHOD)
public @interface GenerateUser {
    boolean handleAnnotation() default true;

    String username() default "";

    String password() default "";
}
