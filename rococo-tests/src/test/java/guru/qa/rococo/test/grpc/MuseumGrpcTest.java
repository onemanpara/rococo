package guru.qa.rococo.test.grpc;

import com.google.protobuf.ByteString;
import guru.qa.grpc.rococo.grpc.*;
import guru.qa.rococo.jupiter.annotation.GenerateMuseum;
import guru.qa.rococo.model.MuseumJson;
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
import static guru.qa.rococo.model.MuseumJson.fromGrpcMessage;
import static guru.qa.rococo.util.DataUtil.*;
import static guru.qa.rococo.util.ImageUtil.convertImageToBase64;
import static io.qameta.allure.Allure.step;
import static java.util.UUID.fromString;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GRPC: Музеи")
public class MuseumGrpcTest extends BaseGrpcTest {

    private static final Channel museumChannel;

    static {
        museumChannel = ManagedChannelBuilder
                .forAddress(CFG.museumGrpcAddress(), CFG.museumGrpcPort())
                .intercept(new AllureGrpc())
                .usePlaintext()
                .build();
    }

    private final RococoMuseumServiceGrpc.RococoMuseumServiceBlockingStub museumStub = RococoMuseumServiceGrpc.newBlockingStub(museumChannel);

    @Test
    @DisplayName("GRPC: Получение информации о музее из rococo-museum")
    @GenerateMuseum(enrichJsonCountryName = false)
    @Tag("GRPC")
    void shouldReturnMuseumDataFromDB(MuseumJson createdMuseum) {
        String createdMuseumId = createdMuseum.getId().toString();
        MuseumRequest request = MuseumRequest.newBuilder()
                .setId(copyFromUtf8(createdMuseumId))
                .build();

        final MuseumResponse museumResponse = museumStub.getMuseum(request);

        step("Check museum in response", () -> assertEquals(createdMuseum, fromGrpcMessage(museumResponse)));
    }

    @Test
    @DisplayName("GRPC: При запросе музея из rococo-museum по ID, которого нет в БД - возвращается соответствующая ошибка")
    @Tag("GRPC")
    void shouldReturnNotFoundErrorIfSendNotExistingMuseumId() {
        String notExistingId = UUID.randomUUID().toString();
        MuseumRequest request = MuseumRequest.newBuilder()
                .setId(copyFromUtf8(notExistingId))
                .build();

        final StatusRuntimeException exception = assertThrows(StatusRuntimeException.class, () -> museumStub.getMuseum(request));

        assertEquals(Status.NOT_FOUND.getCode(), exception.getStatus().getCode());
        assertEquals("Museum not found by id: " + notExistingId, exception.getStatus().getDescription());
    }

    @Test
    @GenerateMuseum(enrichJsonCountryName = false)
    @DisplayName("GRPC: Фильтрация списка музеев по названию")
    @Tag("GRPC")
    void shouldReturnMuseumAfterFilterByTitle(MuseumJson createdMuseum) {
        String museumTitle = createdMuseum.getTitle();
        AllMuseumRequest request = AllMuseumRequest.newBuilder()
                .setTitle(museumTitle)
                .setPage(0)
                .setSize(10)
                .build();

        final MuseumResponse museumResponse = museumStub.getAllMuseum(request).getMuseum(0);

        step("Check museum in response", () -> assertEquals(createdMuseum, fromGrpcMessage(museumResponse)));
    }

    @Test
    @DisplayName("GRPC: Фильтрация списка музеев по названию: результаты отсутствуют")
    @Tag("GRPC")
    void shouldReturnZeroIfMuseumSearchResultIsEmpty() {
        String museumTitle = generateRandomMuseumName();
        AllMuseumRequest request = AllMuseumRequest.newBuilder()
                .setTitle(museumTitle)
                .setPage(0)
                .setSize(10)
                .build();

        final AllMuseumResponse response = museumStub.getAllMuseum(request);

        step("Check that museums in response count is 0", () -> {
            assertEquals(0, response.getMuseumList().size());
            assertEquals(0, response.getTotalCount());
        });
    }

    @Test
    @DisplayName("GRPC: При создании нового музея возвращается ID из rococo-museum")
    @Tag("GRPC")
    void shouldAddNewMuseum() {
        String title = generateRandomMuseumName();
        String description = generateRandomSentence(30);
        Geo geo = Geo.newBuilder()
                .setCity(getRandomCityName())
                .setCountry(CountryId.newBuilder().setId(copyFromUtf8(getRandomCountry().id().toString())))
                .build();

        AddMuseumRequest addMuseumRequest = AddMuseumRequest.newBuilder()
                .setTitle(title)
                .setDescription(description)
                .setPhoto(copyFromUtf8(convertImageToBase64(PHOTO_PATH)))
                .setGeo(geo)
                .build();

        final MuseumResponse response = museumStub.addMuseum(addMuseumRequest);

        step("Check that response contains created museum ID", () ->
                assertTrue(response.getId().toStringUtf8().matches(ID_REGEXP))
        );
    }

    @Test
    @GenerateMuseum(enrichJsonCountryName = false)
    @DisplayName("GRPC: При обновлении музея должны сохраняться значения в rococo-museum")
    @Tag("GRPC")
    void shouldUpdateMuseum(MuseumJson createdMuseum) {
        String newTitle = generateRandomMuseumName();
        String newDescription = generateRandomSentence(30);
        Geo newGeo = Geo.newBuilder()
                .setCity(getRandomCityName())
                .setCountry(CountryId.newBuilder().setId(copyFromUtf8(getRandomCountry().id().toString())))
                .build();
        AddMuseumRequest museumData = AddMuseumRequest.newBuilder()
                .setTitle(newTitle)
                .setDescription(newDescription)
                .setPhoto(copyFromUtf8(convertImageToBase64(NEW_PHOTO_PATH)))
                .setGeo(newGeo)
                .build();

        UpdateMuseumRequest updateMuseumRequest = UpdateMuseumRequest.newBuilder()
                .setId(ByteString.copyFromUtf8(createdMuseum.getId().toString()))
                .setMuseumData(museumData)
                .build();

        final MuseumResponse response = museumStub.updateMuseum(updateMuseumRequest);

        step("Check that response contains updated museum ID", () ->
                assertEquals(createdMuseum.getId(), fromString(response.getId().toStringUtf8()))
        );
        step("Check that response contains updated museum title", () ->
                assertEquals(newTitle, response.getTitle())
        );
        step("Check that response contains updated museum description", () ->
                assertEquals(newDescription, response.getDescription())
        );
        step("Check that response contains updated museum geo", () ->
                assertEquals(newGeo, response.getGeo())
        );
        step("Check that response contains updated museum photo", () ->
                assertEquals(convertImageToBase64(NEW_PHOTO_PATH), response.getPhoto().toStringUtf8())
        );
    }
}
