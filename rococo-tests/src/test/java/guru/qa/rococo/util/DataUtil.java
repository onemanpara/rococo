package guru.qa.rococo.util;

import com.github.javafaker.Faker;
import guru.qa.grpc.rococo.grpc.AllCountryRequest;
import guru.qa.grpc.rococo.grpc.AllCountryResponse;
import guru.qa.grpc.rococo.grpc.RococoCountryServiceGrpc;
import guru.qa.rococo.config.Config;
import guru.qa.rococo.model.CountryJson;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.qameta.allure.Step;
import io.qameta.allure.grpc.AllureGrpc;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static java.time.format.DateTimeFormatter.ofPattern;

public class DataUtil {
    private static final Config CFG = Config.getConfig();
    private static final Faker fakerEn = new Faker();
    private static final Faker fakerRu = new Faker(new Locale("ru"));
    private static final Channel channel;
    private static final RococoCountryServiceGrpc.RococoCountryServiceBlockingStub countryStub;

    static {
        channel = ManagedChannelBuilder
                .forAddress(CFG.geoGrpcAddress(), CFG.geoGrpcPort())
                .intercept(new AllureGrpc())
                .usePlaintext()
                .build();
        countryStub = RococoCountryServiceGrpc.newBlockingStub(channel);
    }

    @Nonnull
    public static String generateRandomUsername() {
        return fakerEn.name().username();
    }

    @Nonnull
    public static String generateRandomPassword() {
        return fakerEn.internet().password(5, 12);
    }

    @Nonnull
    public static String generateRandomPassword(Integer minLength, Integer maxLength) {
        return fakerEn.internet().password(minLength, maxLength);
    }

    @Nonnull
    public static String generateRandomWord(Integer charsCount) {
        return fakerEn.lorem().characters(charsCount);
    }

    @Nonnull
    public static String generateRandomName() {
        return fakerRu.name().nameWithMiddle() + " " + LocalDateTime.now().format(ofPattern("dd-MM-yy-HH:mm"));
    }

    @Nonnull
    public static String generateRandomPaintingName() {
        return fakerRu.funnyName().name() + " " + LocalDateTime.now().format(ofPattern("dd-MM-yy-HH:mm"));
    }

    @Nonnull
    public static String generateRandomMuseumName() {
        return "Музей имени " + fakerRu.name().firstName() + " " + LocalDateTime.now().format(ofPattern("dd-MM-yy-HH:mm"));
    }

    @Nonnull
    public static String generateRandomSentence(int wordsCount) {
        return fakerEn.lorem().sentence(wordsCount);
    }

    @Nonnull
    @Step("Getting random country name from rococo-geo-service")
    public static synchronized CountryJson getRandomCountry() {
        AllCountryRequest request = AllCountryRequest.newBuilder().setPage(0).setSize(200).build();
        AllCountryResponse response = countryStub.getAllCountry(request);
        List<CountryJson> countryName = response.getCountryList().stream().map(CountryJson::fromGrpcMessage).toList();
        Random random = new Random();
        int randomIndex = random.nextInt(countryName.size());
        return countryName.get(randomIndex);
    }

    @Nonnull
    public static String getRandomCityName() {
        return fakerRu.address().cityName();
    }
}
