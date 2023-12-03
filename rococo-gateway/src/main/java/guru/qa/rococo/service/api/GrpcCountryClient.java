package guru.qa.rococo.service.api;

import com.google.protobuf.ByteString;
import guru.qa.grpc.rococo.grpc.*;
import guru.qa.rococo.model.museum.CountryJson;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import jakarta.annotation.Nonnull;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.google.protobuf.ByteString.copyFromUtf8;

@Component
public class GrpcCountryClient {

    private static final Logger LOG = LoggerFactory.getLogger(GrpcCountryClient.class);

    @GrpcClient("grpcGeoClient")
    private RococoGeoServiceGrpc.RococoGeoServiceBlockingStub rococoCountryServiceStub;

    public @Nonnull Page<CountryJson> getAllCountry(Pageable pageable) {
        AllCountryRequest request = AllCountryRequest.newBuilder()
                .setPage(pageable.getPageNumber())
                .setSize(pageable.getPageSize())
                .build();
        try {
            AllCountryResponse response = rococoCountryServiceStub.getAllCountry(request);
            List<CountryJson> countryJsonList = response.getCountryList()
                    .stream()
                    .map(CountryJson::fromGrpcMessage)
                    .toList();
            return new PageImpl<>(countryJsonList, pageable, response.getTotalCount());
        } catch (StatusRuntimeException e) {
            LOG.error("### Error while calling gRPC server ", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
        }
    }

    @Nonnull
    CountryJson getCountryById(UUID id) {
        CountryId request = CountryId.newBuilder()
                .setId(copyFromUtf8(id.toString()))
                .build();
        try {
            CountryResponse response = rococoCountryServiceStub.getCountry(request);
            return new CountryJson(UUID.fromString(response.getId().toStringUtf8()), response.getName());
        } catch (StatusRuntimeException e) {
            if (e.getStatus().getCode() == Status.Code.NOT_FOUND) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Запрашиваемая страна с id " + id + " не найдена", e);
            } else {
                LOG.error("### Error while calling gRPC server", e);
                throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
            }
        }
    }

    @Nonnull
    List<CountryJson> getCountryByIds(Set<UUID> countryIds) {
        CountryIdsRequest.Builder requestBuilder = CountryIdsRequest.newBuilder();
        countryIds.forEach(countryId -> requestBuilder.addId(ByteString.copyFromUtf8(countryId.toString())));
        CountryIdsRequest request = requestBuilder.build();
        try {
            AllCountryByIdsResponse response = rococoCountryServiceStub.getCountriesByIds(request);
            return response.getCountryList()
                    .stream()
                    .map(CountryJson::fromGrpcMessage)
                    .toList();
        } catch (StatusRuntimeException e) {
            LOG.error("### Error while calling gRPC server", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
        }
    }

}
