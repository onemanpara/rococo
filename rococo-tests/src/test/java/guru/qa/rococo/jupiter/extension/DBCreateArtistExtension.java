package guru.qa.rococo.jupiter.extension;

import guru.qa.rococo.db.model.ArtistEntity;
import guru.qa.rococo.db.repository.ArtistRepository;
import guru.qa.rococo.db.repository.ArtistRepositoryHibernate;
import guru.qa.rococo.jupiter.annotation.GenerateArtist;
import guru.qa.rococo.model.ArtistJson;
import guru.qa.rococo.util.DataUtil;
import guru.qa.rococo.util.ImageUtil;
import org.junit.jupiter.api.extension.*;

import static io.qameta.allure.Allure.step;

public class DBCreateArtistExtension implements BeforeEachCallback, ParameterResolver {

    public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(DBCreateArtistExtension.class);
    private static final String IMAGE_PATH = "files/testimg.jpg";

    @Override
    public void beforeEach(ExtensionContext extensionContext) {
        GenerateArtist annotation = extensionContext.getRequiredTestMethod().getAnnotation(GenerateArtist.class);
        if (annotation != null) {
            step("Create artist for test (DB)", () -> {
                ArtistRepository artistRepository = new ArtistRepositoryHibernate();

                String name = annotation.name().isEmpty() ? DataUtil.generateRandomName() : annotation.name();
                String biography = annotation.biography().isEmpty() ? DataUtil.generateRandomSentence(30) : annotation.biography();
                String photoBase64 = ImageUtil.convertImageToBase64(IMAGE_PATH);

                ArtistEntity artist = new ArtistEntity();
                artist.setName(name);
                artist.setBiography(biography);
                artist.setPhoto(photoBase64.getBytes());

                artistRepository.createArtistForTest(artist);
                extensionContext.getStore(NAMESPACE).put(extensionContext.getUniqueId(), ArtistJson.fromEntity(artist));
            });
        }

    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext
                .getParameter()
                .getType()
                .isAssignableFrom(ArtistJson.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext
                .getStore(NAMESPACE)
                .get(extensionContext.getUniqueId(), ArtistJson.class);
    }
}