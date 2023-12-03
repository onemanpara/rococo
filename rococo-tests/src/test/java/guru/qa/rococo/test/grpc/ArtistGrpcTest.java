package guru.qa.rococo.test.grpc;

import com.google.protobuf.ByteString;
import guru.qa.grpc.rococo.grpc.*;
import guru.qa.rococo.jupiter.annotation.GenerateArtist;
import guru.qa.rococo.model.ArtistJson;
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
import static guru.qa.rococo.model.ArtistJson.fromGrpcMessage;
import static guru.qa.rococo.util.DataUtil.*;
import static guru.qa.rococo.util.ImageUtil.convertImageToBase64;
import static io.qameta.allure.Allure.step;
import static java.util.UUID.fromString;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GRPC: Художники")
public class ArtistGrpcTest extends BaseGrpcTest {

    private static final Channel artistChannel;

    static {
        artistChannel = ManagedChannelBuilder
                .forAddress(CFG.artistGrpcAddress(), CFG.artistGrpcPort())
                .intercept(new AllureGrpc())
                .usePlaintext()
                .build();
    }

    private final RococoArtistServiceGrpc.RococoArtistServiceBlockingStub artistStub = RococoArtistServiceGrpc.newBlockingStub(artistChannel);

    @Test
    @DisplayName("GRPC: Получение информации о художнике из rococo-artist")
    @GenerateArtist
    @Tag("GRPC")
    void shouldReturnArtistDataFromDB(ArtistJson createdArtist) {
        String createdArtistId = createdArtist.getId().toString();
        ArtistRequest request = ArtistRequest.newBuilder()
                .setId(copyFromUtf8(createdArtistId))
                .build();

        final ArtistResponse artistResponse = artistStub.getArtist(request);

        step("Check artist in response", () -> assertEquals(createdArtist, fromGrpcMessage(artistResponse)));
    }

    @Test
    @DisplayName("GRPC: При запросе художника из rococo-artist по ID, которого нет в БД - возвращается соответствующая ошибка")
    @Tag("GRPC")
    void shouldReturnNotFoundErrorIfSendNotExistingArtistId() {
        String notExistingId = UUID.randomUUID().toString();
        ArtistRequest request = ArtistRequest.newBuilder()
                .setId(copyFromUtf8(notExistingId))
                .build();

        final StatusRuntimeException exception = assertThrows(StatusRuntimeException.class, () -> artistStub.getArtist(request));

        assertEquals(Status.NOT_FOUND.getCode(), exception.getStatus().getCode());
        assertEquals("Artist not found by id: " + notExistingId, exception.getStatus().getDescription());
    }

    @Test
    @GenerateArtist
    @DisplayName("GRPC: Фильтрация списка художников по имени")
    @Tag("GRPC")
    void shouldReturnArtistAfterFilterByName(ArtistJson createdArtist) {
        String artistName = createdArtist.getName();
        AllArtistRequest request = AllArtistRequest.newBuilder()
                .setName(artistName)
                .setPage(0)
                .setSize(10)
                .build();

        final ArtistResponse artistResponse = artistStub.getAllArtist(request).getArtists(0);

        step("Check artist in response", () -> assertEquals(createdArtist, fromGrpcMessage(artistResponse)));
    }

    @Test
    @DisplayName("GRPC: Фильтрация списка художников по имени: результаты отсутствуют")
    @Tag("GRPC")
    void shouldReturnZeroIfArtistSearchResultIsEmpty() {
        String artistName = generateRandomName();
        AllArtistRequest request = AllArtistRequest.newBuilder()
                .setName(artistName)
                .setPage(0)
                .setSize(10)
                .build();

        final AllArtistResponse response = artistStub.getAllArtist(request);

        step("Check that artists in response count is 0", () -> {
            assertEquals(0, response.getArtistsList().size());
            assertEquals(0, response.getTotalCount());
        });
    }

    @Test
    @DisplayName("GRPC: При создании нового художника возвращается ID из rococo-artist")
    @Tag("GRPC")
    void shouldAddNewArtist() {
        String name = generateRandomName();
        String biography = generateRandomSentence(30);

        AddArtistRequest addArtistRequest = AddArtistRequest.newBuilder()
                .setName(name)
                .setBiography(biography)
                .setPhoto(copyFromUtf8(convertImageToBase64(PHOTO_PATH)))
                .build();

        final ArtistResponse response = artistStub.addArtist(addArtistRequest);

        step("Check that response contains created artist ID", () ->
                assertTrue(response.getId().toStringUtf8().matches(ID_REGEXP))
        );
    }

    @Test
    @GenerateArtist
    @DisplayName("GRPC: При обновлении художника должны сохраняться значения в rococo-artist")
    @Tag("GRPC")
    void shouldUpdateArtist(ArtistJson createdArtist) {
        String newName = generateRandomMuseumName();
        String newBiography = generateRandomSentence(30);

        AddArtistRequest artistData = AddArtistRequest.newBuilder()
                .setName(newName)
                .setBiography(newBiography)
                .setPhoto(copyFromUtf8(convertImageToBase64(NEW_PHOTO_PATH)))
                .build();


        UpdateArtistRequest updateArtistRequest = UpdateArtistRequest.newBuilder()
                .setId(ByteString.copyFromUtf8(createdArtist.getId().toString()))
                .setArtistData(artistData)
                .build();

        final ArtistResponse response = artistStub.updateArtist(updateArtistRequest);

        step("Check that response contains updated artist ID", () ->
                assertEquals(createdArtist.getId(), fromString(response.getId().toStringUtf8()))
        );
        step("Check that response contains updated arist name", () ->
                assertEquals(newName, response.getName())
        );
        step("Check that response contains updated artist biography", () ->
                assertEquals(newBiography, response.getBiography())
        );
        step("Check that response contains updated artist photo", () ->
                assertEquals(convertImageToBase64(NEW_PHOTO_PATH), response.getPhoto().toStringUtf8())
        );
    }
}
