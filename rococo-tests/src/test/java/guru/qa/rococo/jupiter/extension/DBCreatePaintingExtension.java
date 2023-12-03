package guru.qa.rococo.jupiter.extension;

import guru.qa.rococo.db.model.PaintingEntity;
import guru.qa.rococo.db.repository.PaintingRepository;
import guru.qa.rococo.db.repository.PaintingRepositoryHibernate;
import guru.qa.rococo.jupiter.annotation.GeneratePainting;
import guru.qa.rococo.model.ArtistJson;
import guru.qa.rococo.model.MuseumJson;
import guru.qa.rococo.model.PaintingJson;
import org.junit.jupiter.api.extension.*;

import java.util.UUID;

import static guru.qa.rococo.util.DataUtil.generateRandomPaintingName;
import static guru.qa.rococo.util.DataUtil.generateRandomSentence;
import static guru.qa.rococo.util.ImageUtil.convertImageToBase64;
import static io.qameta.allure.Allure.step;

public class DBCreatePaintingExtension implements BeforeEachCallback, ParameterResolver {

    public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(DBCreatePaintingExtension.class);
    private static final String IMAGE_PATH = "files/testimg.jpg";
    static final String PAINTING_KEY = "painting";

    @Override
    public void beforeEach(ExtensionContext extensionContext) {
        GeneratePainting annotation = extensionContext.getRequiredTestMethod().getAnnotation(GeneratePainting.class);
        if (annotation != null) {
            step("Create painting (DB)", () -> {
                PaintingEntity painting = createPaintingForTest(extensionContext, annotation);
                extensionContext.getStore(NAMESPACE).put(extensionContext.getUniqueId(), PaintingJson.fromEntity(painting));
            });
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext
                .getParameter()
                .getType()
                .isAssignableFrom(PaintingJson.class);
    }

    @Override
    public PaintingJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext
                .getStore(NAMESPACE)
                .get(extensionContext.getUniqueId(), PaintingJson.class);
    }

    private PaintingEntity createPaintingForTest(ExtensionContext extensionContext, GeneratePainting annotation) {
        PaintingRepository paintingRepository = new PaintingRepositoryHibernate();

        String title = annotation.title().isEmpty() ? generateRandomPaintingName() : annotation.title();
        String description = annotation.description().isEmpty() ? generateRandomSentence(30) : annotation.description();
        String photoBase64 = convertImageToBase64(IMAGE_PATH);

        UUID museumId = extensionContext
                .getStore(DBCreateMuseumExtension.NAMESPACE)
                .get(extensionContext.getUniqueId() + PAINTING_KEY, MuseumJson.class).getId();

        UUID artistId = extensionContext
                .getStore(DBCreateArtistExtension.NAMESPACE)
                .get(extensionContext.getUniqueId() + PAINTING_KEY, ArtistJson.class).getId();

        PaintingEntity painting = new PaintingEntity();
        painting.setTitle(title);
        painting.setDescription(description);
        painting.setContent(photoBase64.getBytes());
        painting.setMuseumId(museumId);
        painting.setArtistId(artistId);

        paintingRepository.createPaintingForTest(painting);
        return painting;
    }
}