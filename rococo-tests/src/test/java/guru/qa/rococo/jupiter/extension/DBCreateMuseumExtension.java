package guru.qa.rococo.jupiter.extension;

import guru.qa.rococo.db.model.MuseumEntity;
import guru.qa.rococo.db.repository.MuseumRepository;
import guru.qa.rococo.db.repository.MuseumRepositoryHibernate;
import guru.qa.rococo.jupiter.annotation.GenerateMuseum;
import guru.qa.rococo.model.MuseumJson;
import guru.qa.rococo.util.CountryUtil;
import guru.qa.rococo.util.DataUtil;
import guru.qa.rococo.util.ImageUtil;
import org.junit.jupiter.api.extension.*;

import java.util.UUID;

import static guru.qa.rococo.util.DataUtil.getRandomCountry;
import static io.qameta.allure.Allure.step;

public class DBCreateMuseumExtension implements BeforeEachCallback, ParameterResolver {

    public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(DBCreateMuseumExtension.class);
    private static final String IMAGE_PATH = "files/testimg.jpg";

    @Override
    public void beforeEach(ExtensionContext extensionContext) {
        GenerateMuseum annotation = extensionContext.getRequiredTestMethod().getAnnotation(GenerateMuseum.class);
        if (annotation != null) {
            step("Create museum for test (DB)", () -> {
                CountryUtil countryUtil = new CountryUtil();
                MuseumRepository museumRepository = new MuseumRepositoryHibernate();

                String title = annotation.title().isEmpty() ? DataUtil.generateRandomMuseumName() : annotation.title();
                UUID countryId = annotation.country().isEmpty() ? getRandomCountry().id() : countryUtil.getCountryIdByName(annotation.country());
                String city = annotation.city().isEmpty() ? DataUtil.getRandomCityName() : annotation.city();
                String description = annotation.city().isEmpty() ? DataUtil.generateRandomSentence(30) : annotation.description();
                String photoBase64 = ImageUtil.convertImageToBase64(IMAGE_PATH);

                MuseumEntity museum = new MuseumEntity();
                museum.setTitle(title);
                museum.setGeoId(countryId);
                museum.setCity(city);
                museum.setDescription(description);
                museum.setPhoto(photoBase64.getBytes());

                museumRepository.createMuseumForTest(museum);
                extensionContext.getStore(NAMESPACE).put(extensionContext.getUniqueId(), MuseumJson.fromEntity(museum));
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
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext
                .getStore(NAMESPACE)
                .get(extensionContext.getUniqueId(), MuseumJson.class);
    }
}
