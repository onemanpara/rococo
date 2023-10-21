package guru.qa.rococo.service.api;

import guru.qa.grpc.rococo.grpc.*;
import guru.qa.rococo.model.ArtistJson;
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
public class GrpcArtistClient {

    private static final Logger LOG = LoggerFactory.getLogger(GrpcArtistClient.class);

    @GrpcClient("grpcArtistClient")
    private RococoArtistServiceGrpc.RococoArtistServiceBlockingStub rococoArtistServiceStub;

    public @Nonnull ArtistJson getArtist(UUID id) {
        GetArtistRequest request = GetArtistRequest.newBuilder()
                .setUuid(copyFromUtf8(id.toString()))
                .build();

        try {
            GetArtistResponse response = rococoArtistServiceStub.getArtist(request);
            return ArtistJson.fromGrpcMessage(response);
        } catch (StatusRuntimeException e) {
            LOG.error("### Error while calling gRPC server ", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
        }
    }

    public @Nonnull Page<ArtistJson> getAllArtist(@Nullable String name, Pageable pageable) {
        GetArtistsWithPaginationRequest.Builder builder = GetArtistsWithPaginationRequest.newBuilder()
                .setPage(pageable.getPageNumber())
                .setSize(pageable.getPageSize());
        if (name != null) {
            builder.setName(name);
        }
        GetArtistsWithPaginationRequest request = builder.build();

        try {
            GetArtistsWithPaginationResponse response = rococoArtistServiceStub.getArtistsWithPagination(request);
            List<ArtistJson> artistJsonList = response.getArtistsList()
                    .stream()
                    .map(ArtistJson::fromGrpcMessage)
                    .toList();
            return new PageImpl<>(artistJsonList, pageable, response.getTotalCount());
        } catch (StatusRuntimeException e) {
            LOG.error("### Error while calling gRPC server ", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
        }
    }


}
