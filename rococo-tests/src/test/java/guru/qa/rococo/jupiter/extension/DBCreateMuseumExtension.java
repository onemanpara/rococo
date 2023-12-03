package guru.qa.rococo.jupiter.extension;

import guru.qa.rococo.db.model.MuseumEntity;
import guru.qa.rococo.db.repository.GeoRepository;
import guru.qa.rococo.db.repository.GeoRepositoryHibernate;
import guru.qa.rococo.db.repository.MuseumRepository;
import guru.qa.rococo.db.repository.MuseumRepositoryHibernate;
import guru.qa.rococo.jupiter.annotation.GenerateMuseum;
import guru.qa.rococo.jupiter.annotation.GeneratePainting;
import guru.qa.rococo.model.CountryJson;
import guru.qa.rococo.model.GeoJson;
import guru.qa.rococo.model.MuseumJson;
import guru.qa.rococo.util.ImageUtil;
import io.qameta.allure.Step;
import org.junit.jupiter.api.extension.*;

import java.util.UUID;

import static guru.qa.rococo.jupiter.extension.DBCreatePaintingExtension.PAINTING_KEY;
import static guru.qa.rococo.util.DataUtil.*;
import static io.qameta.allure.Allure.step;

public class DBCreateMuseumExtension implements BeforeEachCallback, ParameterResolver {

    public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(DBCreateMuseumExtension.class);
    private static final String IMAGE_PATH = "files/testimg.jpg";

    @Override
    public void beforeEach(ExtensionContext extensionContext) {
        GenerateMuseum museumAnnotation = extensionContext.getRequiredTestMethod().getAnnotation(GenerateMuseum.class);
        if (museumAnnotation != null) {
            step("Create museum (DB)", () -> {
                MuseumEntity museum = createMuseumForTest(museumAnnotation);
                MuseumJson museumJson = MuseumJson.fromEntity(museum);
                if (museumAnnotation.enrichJsonCountryName()) enrichMuseumCountryName(museumJson);
                extensionContext.getStore(NAMESPACE).put(extensionContext.getUniqueId(), museumJson);
            });
        }

        GeneratePainting paintingAnnotation = extensionContext.getRequiredTestMethod().getAnnotation(GeneratePainting.class);
        if (paintingAnnotation != null) {
            step("Create museum from painting annotation (DB)", () -> {
                MuseumEntity museum = createMuseumForTest(paintingAnnotation.museum());
                MuseumJson museumJson = MuseumJson.fromEntity(museum);
                if (paintingAnnotation.museum().enrichJsonCountryName()) enrichMuseumCountryName(museumJson);
                extensionContext.getStore(NAMESPACE).put(extensionContext.getUniqueId() + PAINTING_KEY, museumJson);
            });
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext
                .getParameter()
                .getType()
                .isAssignableFrom(MuseumJson.class);
    }

    @Override
    public MuseumJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext
                .getStore(NAMESPACE)
                .get(extensionContext.getUniqueId(), MuseumJson.class);
    }

    private MuseumEntity createMuseumForTest(GenerateMuseum museumAnnotation) {
        GeoRepository geoRepository = new GeoRepositoryHibernate();
        MuseumRepository museumRepository = new MuseumRepositoryHibernate();

        String title = museumAnnotation.title().isEmpty() ? generateRandomMuseumName() : museumAnnotation.title();
        UUID countryId = museumAnnotation.country().isEmpty() ? getRandomCountry().id() : geoRepository.getCountryByName(museumAnnotation.country()).getId();
        String city = museumAnnotation.city().isEmpty() ? getRandomCityName() : museumAnnotation.city();
        String description = museumAnnotation.city().isEmpty() ? generateRandomSentence(30) : museumAnnotation.description();
        String photoBase64 = ImageUtil.convertImageToBase64(IMAGE_PATH);

        MuseumEntity museum = new MuseumEntity();
        museum.setTitle(title);
        museum.setGeoId(countryId);
        museum.setCity(city);
        museum.setDescription(description);
        museum.setPhoto(photoBase64.getBytes());

        museumRepository.createMuseumForTest(museum);
        return museum;
    }

    @Step("Enrich museum with country name")
    private void enrichMuseumCountryName(MuseumJson museumJson) {
        GeoRepository geoRepository = new GeoRepositoryHibernate();
        UUID countryId = museumJson.getGeo().getCountry().id();
        CountryJson countryJson = new CountryJson(countryId, geoRepository.getCountryById(countryId).getName());
        GeoJson geoJson = new GeoJson(museumJson.getGeo().getCity(), countryJson);

        museumJson.setGeo(geoJson);
    }
}
