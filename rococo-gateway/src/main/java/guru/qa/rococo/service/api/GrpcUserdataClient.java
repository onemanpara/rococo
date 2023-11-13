package guru.qa.rococo.service.api;

import com.google.protobuf.ByteString;
import guru.qa.grpc.rococo.grpc.RococoUserdataServiceGrpc;
import guru.qa.grpc.rococo.grpc.UpdateUserRequest;
import guru.qa.grpc.rococo.grpc.UserRequest;
import guru.qa.grpc.rococo.grpc.UserResponse;
import guru.qa.rococo.model.UserJson;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import jakarta.annotation.Nonnull;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;

@Component
public class GrpcUserdataClient {

    private static final Logger LOG = LoggerFactory.getLogger(GrpcUserdataClient.class);

    @GrpcClient("grpcUserdataClient")
    private RococoUserdataServiceGrpc.RococoUserdataServiceBlockingStub rococoUserdataServiceStub;

    public @Nonnull UserJson getUser(String username) {
        UserRequest request = UserRequest.newBuilder()
                .setUsername(username)
                .build();
        try {
            UserResponse response = rococoUserdataServiceStub.getUser(request);
            return UserJson.fromGrpcMessage(response);
        } catch (StatusRuntimeException e) {
            if (e.getStatus().getCode() == Status.Code.NOT_FOUND) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Запрашиваемый пользователь с username " + username + " не найден", e);
            } else {
                LOG.error("### Error while calling gRPC server ", e);
                throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
            }
        }
    }

    public @Nonnull UserJson updateUser(UserJson user) {
        UpdateUserRequest request = UpdateUserRequest.newBuilder()
                .setId(ByteString.copyFromUtf8(user.id().toString()))
                .setUsername(user.username())
                .setFirstname(user.firstname())
                .setLastname(user.lastname())
                .setAvatar(ByteString.copyFrom(user.avatar(), StandardCharsets.UTF_8))
                .build();
        UserResponse response = rococoUserdataServiceStub.updateUser(request);
        return UserJson.fromGrpcMessage(response);
    }
}
