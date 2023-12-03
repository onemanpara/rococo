package guru.qa.rococo.service.api;

import com.google.protobuf.ByteString;
import guru.qa.grpc.rococo.grpc.*;
import guru.qa.rococo.model.ArtistJson;
import io.grpc.Status;
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
import java.util.Set;
import java.util.UUID;

import static com.google.protobuf.ByteString.copyFromUtf8;

@Component
public class GrpcArtistClient {

    private static final Logger LOG = LoggerFactory.getLogger(GrpcArtistClient.class);

    @GrpcClient("grpcArtistClient")
    private RococoArtistServiceGrpc.RococoArtistServiceBlockingStub rococoArtistServiceStub;

    public @Nonnull ArtistJson getArtist(UUID id) {
        ArtistRequest request = ArtistRequest.newBuilder()
                .setId(copyFromUtf8(id.toString()))
                .build();
        try {
            ArtistResponse response = rococoArtistServiceStub.getArtist(request);
            return ArtistJson.fromGrpcMessage(response);
        } catch (StatusRuntimeException e) {
            if (e.getStatus().getCode() == Status.Code.NOT_FOUND) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Запрашиваемый художник с id " + id + " не найден", e);
            } else {
                LOG.error("### Error while calling gRPC server ", e);
                throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
            }
        }
    }

    public @Nonnull Page<ArtistJson> getAllArtist(@Nullable String name, Pageable pageable) {
        AllArtistRequest.Builder builder = AllArtistRequest.newBuilder()
                .setPage(pageable.getPageNumber())
                .setSize(pageable.getPageSize());
        if (name != null) {
            builder.setName(name);
        }
        AllArtistRequest request = builder.build();

        try {
            AllArtistResponse response = rococoArtistServiceStub.getAllArtist(request);
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

    public @Nonnull ArtistJson addArtist(ArtistJson artist) {
        AddArtistRequest request = ArtistJson.toGrpcMessage(artist);
        ArtistResponse response = rococoArtistServiceStub.addArtist(request);
        return ArtistJson.fromGrpcMessage(response);
    }

    public @Nonnull ArtistJson updateArtist(ArtistJson artist) {
        AddArtistRequest artistData = ArtistJson.toGrpcMessage(artist);
        UpdateArtistRequest request = UpdateArtistRequest.newBuilder()
                .setId(ByteString.copyFromUtf8(artist.id().toString()))
                .setArtistData(artistData)
                .build();
        ArtistResponse response = rococoArtistServiceStub.updateArtist(request);
        return ArtistJson.fromGrpcMessage(response);
    }

    @Nonnull
    List<ArtistJson> getArtistByIds(Set<UUID> museumIds) {
        ArtistIdsRequest.Builder requestBuilder = ArtistIdsRequest.newBuilder();
        museumIds.forEach(museumId -> requestBuilder.addId(ByteString.copyFromUtf8(museumId.toString())));
        ArtistIdsRequest request = requestBuilder.build();
        try {
            AllArtistByIdsResponse response = rococoArtistServiceStub.getArtistByIds(request);
            return response.getArtistList()
                    .stream()
                    .map(ArtistJson::fromGrpcMessage)
                    .toList();
        } catch (StatusRuntimeException e) {
            LOG.error("### Error while calling gRPC server", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
        }
    }

}
