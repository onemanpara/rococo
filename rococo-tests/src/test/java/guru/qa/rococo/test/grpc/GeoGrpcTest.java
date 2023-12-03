package guru.qa.rococo.test.grpc;

import com.google.protobuf.ByteString;
import guru.qa.grpc.rococo.grpc.*;
import guru.qa.rococo.model.CountryJson;
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
import static guru.qa.rococo.util.DataUtil.generateRandomName;
import static guru.qa.rococo.util.DataUtil.getRandomCountry;
import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GRPC: Страны")
public class GeoGrpcTest extends BaseGrpcTest {

    private static final Channel geoChannel;

    static {
        geoChannel = ManagedChannelBuilder
                .forAddress(CFG.geoGrpcAddress(), CFG.geoGrpcPort())
                .intercept(new AllureGrpc())
                .usePlaintext()
                .build();
    }

    private final RococoGeoServiceGrpc.RococoGeoServiceBlockingStub geoStub = RococoGeoServiceGrpc.newBlockingStub(geoChannel);

    @Test
    @DisplayName("GRPC: Получение информации о стране из rococo-geo по id")
    @Tag("GRPC")
    void shouldReturnCountryDataByIdFromDB() {
        CountryJson country = getRandomCountry();
        CountryId request = CountryId.newBuilder()
                .setId(copyFromUtf8(country.id().toString()))
                .build();

        final CountryResponse countryResponse = geoStub.getCountry(request);

        step("Check country name in response", () -> assertEquals(country.name(), countryResponse.getName()));
        step("Check country ID in response", () -> assertEquals(country.id(), UUID.fromString(countryResponse.getId().toStringUtf8())));
    }

    @Test
    @DisplayName("GRPC: Получение информации о стране из rococo-geo по имени страны")
    @Tag("GRPC")
    void shouldReturnCountryDataByNameFromDB() {
        CountryJson country = getRandomCountry();
        CountryName request = CountryName.newBuilder()
                .setName(country.name())
                .build();

        final CountryResponse countryResponse = geoStub.getCountryByName(request);

        step("Check country name in response", () -> assertEquals(country.name(), countryResponse.getName()));
        step("Check country ID in response", () -> assertEquals(country.id(), UUID.fromString(countryResponse.getId().toStringUtf8())));
    }

    @Test
    @DisplayName("GRPC: При запросе страны из rococo-geo по ID, которого нет в БД - возвращается соответствующая ошибка")
    @Tag("GRPC")
    void shouldReturnNotFoundErrorIfSendNotExistingCountryId() {
        String notExistingId = UUID.randomUUID().toString();
        CountryId request = CountryId.newBuilder()
                .setId(ByteString.copyFromUtf8(notExistingId))
                .build();

        final StatusRuntimeException exception = assertThrows(StatusRuntimeException.class, () -> geoStub.getCountry(request));

        assertEquals(Status.NOT_FOUND.getCode(), exception.getStatus().getCode());
        assertEquals("Country not found by id: " + notExistingId, exception.getStatus().getDescription());
    }

    @Test
    @DisplayName("GRPC: При запросе страны из rococo-geo по имени, которого нет в БД - возвращается соответствующая ошибка")
    @Tag("GRPC")
    void shouldReturnNotFoundErrorIfSendNotExistingCountryName() {
        String notExistingCountryName = generateRandomName();
        CountryName request = CountryName.newBuilder()
                .setName(notExistingCountryName)
                .build();

        final StatusRuntimeException exception = assertThrows(StatusRuntimeException.class, () -> geoStub.getCountryByName(request));

        assertEquals(Status.NOT_FOUND.getCode(), exception.getStatus().getCode());
        assertEquals("Country not found by name: " + notExistingCountryName, exception.getStatus().getDescription());
    }

    @Test
    @DisplayName("GRPC: Запрос на все страны корректно отдаёт список с учётом пагинации")
    @Tag("GRPC")
    void shouldReturnAllCountry() {
        AllCountryRequest request = AllCountryRequest.newBuilder()
                .setPage(0)
                .setSize(20)
                .build();

        final AllCountryResponse response = geoStub.getAllCountry(request);
        assertEquals(20, response.getCountryCount());

        final CountryResponse country = response.getCountry(0);
        assertEquals("Австралия", country.getName());
        assertTrue(country.getId().toStringUtf8().matches(ID_REGEXP));
    }

}
