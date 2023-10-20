package guru.qa.rococo.service.api;

import guru.qa.grpc.rococo.grpc.GetAristRequest;
import guru.qa.grpc.rococo.grpc.GetAristResponse;
import guru.qa.grpc.rococo.grpc.RococoArtistServiceGrpc;
import guru.qa.rococo.model.ArtistJson;
import io.grpc.StatusRuntimeException;
import jakarta.annotation.Nonnull;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static com.google.protobuf.ByteString.copyFromUtf8;

@Component
public class GrpcArtistClient {

    private static final Logger LOG = LoggerFactory.getLogger(GrpcArtistClient.class);

    @GrpcClient("grpcArtistClient")
    private RococoArtistServiceGrpc.RococoArtistServiceBlockingStub rococoArtistServiceStub;

    public @Nonnull ArtistJson getArtist(UUID id) {
        GetAristRequest request = GetAristRequest.newBuilder()
                .setUuid(copyFromUtf8(id.toString()))
                .build();

        try {
            GetAristResponse response = rococoArtistServiceStub.getArtist(request);
            return ArtistJson.fromGrpcMessage(response);
        } catch (StatusRuntimeException e) {
            LOG.error("### Error while calling gRPC server ", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
        }
    }
}
