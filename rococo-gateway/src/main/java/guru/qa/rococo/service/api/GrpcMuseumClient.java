package guru.qa.rococo.service.api;

import guru.qa.grpc.rococo.grpc.*;
import guru.qa.rococo.model.museum.CountryJson;
import guru.qa.rococo.model.museum.MuseumJson;
import io.grpc.StatusRuntimeException;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
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
import java.util.UUID;

import static com.google.protobuf.ByteString.copyFromUtf8;

@Component
public class GrpcMuseumClient {

    private static final Logger LOG = LoggerFactory.getLogger(GrpcMuseumClient.class);

    @GrpcClient("grpcMuseumClient")
    private RococoMuseumServiceGrpc.RococoMuseumServiceBlockingStub rococoMuseumServiceStub;

    public @Nonnull MuseumJson getMuseum(UUID id) {
        GetMuseumRequest request = GetMuseumRequest.newBuilder()
                .setUuid(copyFromUtf8(id.toString()))
                .build();

        try {
            GetMuseumResponse response = rococoMuseumServiceStub.getMuseum(request);
            return MuseumJson.fromGrpcMessage(response);
        } catch (StatusRuntimeException e) {
            LOG.error("### Error while calling gRPC server ", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
        }
    }

    public @Nonnull Page<MuseumJson> getAllMuseum(@Nullable String title, Pageable pageable) {
        GetMuseumsWithPaginationRequest.Builder builder = GetMuseumsWithPaginationRequest.newBuilder()
                .setPage(pageable.getPageNumber())
                .setSize(pageable.getPageSize());
        if (title != null) {
            builder.setTitle(title);
        }
        GetMuseumsWithPaginationRequest request = builder.build();

        try {
            GetMuseumsWithPaginationResponse response = rococoMuseumServiceStub.getMuseumWithPagination(request);
            List<MuseumJson> museumJsonList = response.getMuseumList()
                    .stream()
                    .map(MuseumJson::fromGrpcMessage)
                    .toList();
            return new PageImpl<>(museumJsonList, pageable, response.getTotalCount());
        } catch (StatusRuntimeException e) {
            LOG.error("### Error while calling gRPC server ", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
        }
    }

    public @Nonnull Page<CountryJson> getAllCountry(Pageable pageable) {
        GetCountriesRequest request = GetCountriesRequest.newBuilder()
                .setPage(pageable.getPageNumber())
                .setSize(pageable.getPageSize())
                .build();

        try {
            GetCountriesResponse response = rococoMuseumServiceStub.getCountries(request);
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

}
