package guru.qa.rococo.test.grpc;

import guru.qa.grpc.rococo.grpc.*;
import guru.qa.rococo.jupiter.annotation.GenerateArtist;
import guru.qa.rococo.jupiter.annotation.GenerateMuseum;
import guru.qa.rococo.jupiter.annotation.GeneratePainting;
import guru.qa.rococo.model.ArtistJson;
import guru.qa.rococo.model.MuseumJson;
import guru.qa.rococo.model.PaintingJson;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.qameta.allure.grpc.AllureGrpc;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.google.protobuf.ByteString.copyFromUtf8;
import static guru.qa.rococo.model.PaintingJson.fromGrpcMessage;
import static guru.qa.rococo.util.DataUtil.generateRandomPaintingName;
import static guru.qa.rococo.util.DataUtil.generateRandomSentence;
import static guru.qa.rococo.util.ImageUtil.convertImageToBase64;
import static io.qameta.allure.Allure.step;
import static java.util.UUID.fromString;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GRPC: Картины")
public class PaintingGrpcTest extends BaseGrpcTest {

    private static final Channel paintingChannel;

    static {
        paintingChannel = ManagedChannelBuilder
                .forAddress(CFG.paintingGrpcAddress(), CFG.paintingGrpcPort())
                .intercept(new AllureGrpc())
                .usePlaintext()
                .build();
    }

    protected final RococoPaintingServiceGrpc.RococoPaintingServiceBlockingStub paintingStub = RococoPaintingServiceGrpc.newBlockingStub(paintingChannel);

    @Test
    @DisplayName("GRPC: Получение информации о картине из rococo-painting")
    @GeneratePainting
    @Tag("GRPC")
    void shouldReturnArtistDataFromDB(PaintingJson createdPainting) {
        String createdPaintingId = createdPainting.getId().toString();
        PaintingRequest request = PaintingRequest.newBuilder()
                .setId(copyFromUtf8(createdPaintingId))
                .build();

        final PaintingResponse paintingResponse = paintingStub.getPainting(request);

        step("Check artist in response", () -> assertEquals(createdPainting, fromGrpcMessage(paintingResponse)));
    }

    @Test
    @DisplayName("GRPC: При запросе картины из rococo-painting по ID, которого нет в БД - возвращается соответствующая ошибка")
    @Tag("GRPC")
    void shouldReturnNotFoundErrorIfSendNotExistingPaintingId() {
        String notExistingId = randomUUID().toString();
        PaintingRequest request = PaintingRequest.newBuilder()
                .setId(copyFromUtf8(notExistingId))
                .build();

        final StatusRuntimeException exception = assertThrows(StatusRuntimeException.class, () -> paintingStub.getPainting(request));

        assertEquals(Status.NOT_FOUND.getCode(), exception.getStatus().getCode());
        assertEquals("Painting not found by id: " + notExistingId, exception.getStatus().getDescription());
    }

    @Test
    @GeneratePainting
    @DisplayName("GRPC: Фильтрация списка картин по названию")
    @Tag("GRPC")
    void shouldReturnPaintingAfterFilterByTitle(PaintingJson createdPainting) {
        String paintingTitle = createdPainting.getTitle();
        AllPaintingRequest request = AllPaintingRequest.newBuilder()
                .setTitle(paintingTitle)
                .setPage(0)
                .setSize(10)
                .build();

        final PaintingResponse paintingResponse = paintingStub.getAllPainting(request).getPainting(0);

        step("Check painting in response", () -> assertEquals(createdPainting, fromGrpcMessage(paintingResponse)));
    }

    @Test
    @DisplayName("GRPC: Фильтрация списка картин по названию: результаты отсутствуют")
    @Tag("GRPC")
    void shouldReturnZeroIfPaintingSearchResultIsEmpty() {
        String paintingTitle = generateRandomPaintingName();
        AllPaintingRequest request = AllPaintingRequest.newBuilder()
                .setTitle(paintingTitle)
                .setPage(0)
                .setSize(10)
                .build();

        final AllPaintingResponse response = paintingStub.getAllPainting(request);

        step("Check that paintings in response count is 0", () -> {
            assertEquals(0, response.getPaintingCount());
            assertEquals(0, response.getTotalCount());
        });
    }

    @Test
    @DisplayName("GRPC: При создании новой картины возвращается ID из rococo-painting")
    @GenerateMuseum
    @GenerateArtist
    @Tag("GRPC")
    void shouldAddNewPainting(MuseumJson createdMuseum, ArtistJson createdArtist) {
        String title = generateRandomPaintingName();
        String description = generateRandomSentence(30);

        AddPaintingRequest addPaintingRequest = AddPaintingRequest.newBuilder()
                .setTitle(title)
                .setDescription(description)
                .setContent(copyFromUtf8(convertImageToBase64(PHOTO_PATH)))
                .setArtistId(ArtistId.newBuilder().setId(copyFromUtf8(createdArtist.getId().toString())))
                .setMuseumId(MuseumId.newBuilder().setId(copyFromUtf8(createdMuseum.getId().toString())))
                .build();

        final PaintingResponse response = paintingStub.addPainting(addPaintingRequest);

        step("Check that response contains created painting ID", () ->
                assertTrue(response.getId().toStringUtf8().matches(ID_REGEXP))
        );
    }

    @Test
    @DisplayName("GRPC: При обновлении картины должны сохраняться значения в rococo-artist")
    @GeneratePainting
    @GenerateMuseum
    @GenerateArtist
    @Tag("GRPC")
    void shouldUpdatePainting(PaintingJson createdPainting, MuseumJson createdMuseum, ArtistJson createdArtist) {
        String newTitle = generateRandomPaintingName();
        String newDescription = generateRandomSentence(30);

        AddPaintingRequest paintingData = AddPaintingRequest.newBuilder()
                .setTitle(newTitle)
                .setDescription(newDescription)
                .setContent(copyFromUtf8(convertImageToBase64(NEW_PHOTO_PATH)))
                .setArtistId(ArtistId.newBuilder().setId(copyFromUtf8(createdArtist.getId().toString())))
                .setMuseumId(MuseumId.newBuilder().setId(copyFromUtf8(createdMuseum.getId().toString())))
                .build();

        UpdatePaintingRequest updatePaintingRequest = UpdatePaintingRequest.newBuilder()
                .setId(copyFromUtf8(createdPainting.getId().toString()))
                .setPaintingData(paintingData)
                .build();

        final PaintingResponse response = paintingStub.updatePainting(updatePaintingRequest);

        step("Check that response contains updated painting ID", () ->
                assertEquals(createdPainting.getId(), fromString(response.getId().toStringUtf8()))
        );
        step("Check that response contains updated painting' title", () ->
                assertEquals(newTitle, response.getTitle())
        );
        step("Check that response contains updated painting's description", () ->
                assertEquals(newDescription, response.getDescription())
        );
        step("Check that response contains updated painting's photo", () ->
                assertEquals(convertImageToBase64(NEW_PHOTO_PATH), response.getContent().toStringUtf8())
        );
        step("Check that response contains updated painting's artist id", () ->
                assertEquals(createdArtist.getId(), UUID.fromString(response.getArtistId().getId().toStringUtf8()))
        );
        step("Check that response contains updated painting's museum id", () ->
                assertEquals(createdMuseum.getId(), UUID.fromString(response.getMuseumId().getId().toStringUtf8()))
        );
    }
}
