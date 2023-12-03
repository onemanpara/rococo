package guru.qa.rococo.jupiter.annotation;

public @interface GeneratePaintingsList {
    GenerateArtist artist() default @GenerateArtist();

    GenerateMuseum museum() default @GenerateMuseum();

    int count() default 1;
}
