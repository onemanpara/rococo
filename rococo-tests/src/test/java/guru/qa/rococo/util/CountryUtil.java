package guru.qa.rococo.util;

import guru.qa.grpc.rococo.grpc.CountryId;
import guru.qa.grpc.rococo.grpc.CountryName;
import guru.qa.grpc.rococo.grpc.CountryResponse;
import guru.qa.grpc.rococo.grpc.RococoCountryServiceGrpc;
import guru.qa.rococo.config.Config;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.qameta.allure.grpc.AllureGrpc;

import javax.annotation.Nonnull;
import java.util.UUID;

import static com.google.protobuf.ByteString.copyFromUtf8;

public class CountryUtil {
    private static final Config CFG = Config.getConfig();
    private static final Channel channel;

    static {
        channel = ManagedChannelBuilder
                .forAddress(CFG.geoGrpcAddress(), CFG.geoGrpcPort())
                .intercept(new AllureGrpc())
                .usePlaintext()
                .build();
    }

    private final RococoCountryServiceGrpc.RococoCountryServiceBlockingStub countryStub = RococoCountryServiceGrpc.newBlockingStub(channel);

    @Nonnull
    public UUID getCountryIdByName(String countryName) {
        CountryName request = CountryName.newBuilder().setName(countryName).build();
        CountryResponse response = countryStub.getCountryByName(request);
        return UUID.fromString(response.getId().toStringUtf8());
    }

    @Nonnull
    public String getCountryNameById(UUID countryId) {
        CountryId request = CountryId.newBuilder().setId(copyFromUtf8(countryId.toString())).build();
        CountryResponse response = countryStub.getCountry(request);
        return response.getName();
    }
}
