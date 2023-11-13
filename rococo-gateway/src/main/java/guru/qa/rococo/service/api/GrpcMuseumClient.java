package guru.qa.rococo.service.api;

import com.google.protobuf.ByteString;
import guru.qa.grpc.rococo.grpc.*;
import guru.qa.rococo.model.museum.CountryJson;
import guru.qa.rococo.model.museum.MuseumJson;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.google.protobuf.ByteString.copyFromUtf8;

@Component
public class GrpcMuseumClient {

    private static final Logger LOG = LoggerFactory.getLogger(GrpcMuseumClient.class);

    @GrpcClient("grpcMuseumClient")
    private RococoMuseumServiceGrpc.RococoMuseumServiceBlockingStub rococoMuseumServiceStub;

    private final GrpcCountryClient grpcCountryClient;

    @Autowired
    public GrpcMuseumClient(GrpcCountryClient grpcCountryClient) {
        this.grpcCountryClient = grpcCountryClient;
    }

    public @Nonnull MuseumJson getMuseum(UUID id) {
        MuseumRequest request = MuseumRequest.newBuilder()
                .setId(copyFromUtf8(id.toString()))
                .build();
        try {
            MuseumResponse response = rococoMuseumServiceStub.getMuseum(request);
            MuseumJson museum = MuseumJson.fromGrpcMessage(response);
            enrichMuseumWithCountry(museum);
            return museum;
        } catch (StatusRuntimeException e) {
            if (e.getStatus().getCode() == Status.Code.NOT_FOUND) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Запрашиваемый музей с id " + id + " не найден", e);
            } else {
                LOG.error("### Error while calling gRPC server", e);
                throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
            }
        }
    }

    public @Nonnull Page<MuseumJson> getAllMuseum(@Nullable String title, Pageable pageable) {
        AllMuseumRequest.Builder builder = AllMuseumRequest.newBuilder()
                .setPage(pageable.getPageNumber())
                .setSize(pageable.getPageSize());
        if (title != null) {
            builder.setTitle(title);
        }
        AllMuseumRequest request = builder.build();

        try {
            AllMuseumResponse response = rococoMuseumServiceStub.getAllMuseum(request);
            List<MuseumJson> museumJsonList = response.getMuseumList()
                    .stream()
                    .map(MuseumJson::fromGrpcMessage)
                    .toList();
            enrichMuseumWithCountry(museumJsonList);
            return new PageImpl<>(museumJsonList, pageable, response.getTotalCount());
        } catch (StatusRuntimeException e) {
            LOG.error("### Error while calling gRPC server ", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
        }
    }

    public @Nonnull MuseumJson addMuseum(MuseumJson museum) {
        AddMuseumRequest request = MuseumJson.toGrpcMessage(museum);
        MuseumResponse response = rococoMuseumServiceStub.addMuseum(request);
        MuseumJson addedMuseum = MuseumJson.fromGrpcMessage(response);
        enrichMuseumWithCountry(addedMuseum);
        return addedMuseum;
    }

    public @Nonnull MuseumJson updateMuseum(MuseumJson museum) {
        AddMuseumRequest museumData = MuseumJson.toGrpcMessage(museum);
        UpdateMuseumRequest request = UpdateMuseumRequest.newBuilder()
                .setId(ByteString.copyFromUtf8(museum.id().toString()))
                .setMuseumData(museumData)
                .build();
        MuseumResponse response = rococoMuseumServiceStub.updateMuseum(request);
        MuseumJson addedMuseum = MuseumJson.fromGrpcMessage(response);
        enrichMuseumWithCountry(addedMuseum);
        return addedMuseum;
    }

    private void enrichMuseumWithCountry(MuseumJson museum) {
        CountryJson country = grpcCountryClient.getCountryById(museum.geo().getCountry().id());
        museum.geo().setCountry(country);
    }

    private void enrichMuseumWithCountry(List<MuseumJson> museumJsonList) {
        Set<UUID> countryIds = museumJsonList.stream()
                .map(museum -> museum.geo().getCountry().id())
                .collect(Collectors.toSet());

        List<CountryJson> countries = grpcCountryClient.getCountryByIds(countryIds);

        museumJsonList.forEach(museum -> {
            UUID countryId = museum.geo().getCountry().id();
            Optional<CountryJson> matchingCountry = countries.stream()
                    .filter(country -> country.id().equals(countryId))
                    .findFirst();
            matchingCountry.ifPresent(country -> museum.geo().setCountry(country));
        });
    }

}
