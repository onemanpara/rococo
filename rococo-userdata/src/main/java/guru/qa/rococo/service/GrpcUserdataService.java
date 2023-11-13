package guru.qa.rococo.service;

import guru.qa.grpc.rococo.grpc.RococoUserdataServiceGrpc;
import guru.qa.grpc.rococo.grpc.UpdateUserRequest;
import guru.qa.grpc.rococo.grpc.UserRequest;
import guru.qa.grpc.rococo.grpc.UserResponse;
import guru.qa.rococo.data.UserEntity;
import guru.qa.rococo.data.repository.UserRepository;
import guru.qa.rococo.model.UserJson;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;

import static io.grpc.Status.NOT_FOUND;

@GrpcService
public class GrpcUserdataService extends RococoUserdataServiceGrpc.RococoUserdataServiceImplBase {

    private static final Logger LOG = LoggerFactory.getLogger(GrpcUserdataService.class);

    private final UserRepository userRepository;

    @Autowired
    public GrpcUserdataService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @KafkaListener(topics = "users", groupId = "userdata")
    public void listener(@Payload UserJson user, ConsumerRecord<String, UserJson> cr) {
        LOG.info("### Kafka topic [users] received message: " + user.username());
        LOG.info("### Kafka consumer record: " + cr.toString());
        UserEntity userDataEntity = new UserEntity();
        userDataEntity.setUsername(user.username());
        UserEntity userEntity = userRepository.save(userDataEntity);
        LOG.info(String.format(
                "### User '%s' successfully saved to database with id: %s",
                user.username(),
                userEntity.getId()
        ));
    }

    @Override
    public void getUser(UserRequest request, StreamObserver<UserResponse> responseObserver) {
        String username = request.getUsername();
        UserEntity userEntity = userRepository.findByUsername(username);

        if (userEntity != null) {
            UserResponse userResponse = UserEntity.toGrpcMessage(userEntity);
            responseObserver.onNext(userResponse);
            responseObserver.onCompleted();
        } else {
            throw new StatusRuntimeException(Status.NOT_FOUND.withDescription("User not found by username: " + username));
        }
    }

    @Override
    public void updateUser(UpdateUserRequest request, StreamObserver<UserResponse> responseObserver) {
        String username = request.getUsername();
        UserEntity userEntity = userRepository.findByUsername(username);

        if (userEntity != null) {
            userEntity.setFirstname(request.getFirstname());
            userEntity.setLastname(request.getLastname());
            userEntity.setAvatar(request.getAvatar().toByteArray());
            userRepository.save(userEntity);

            UserResponse userResponse = UserEntity.toGrpcMessage(userEntity);
            responseObserver.onNext(userResponse);
            responseObserver.onCompleted();
        } else {
            throw new StatusRuntimeException(
                    NOT_FOUND.withDescription("User not found by username: " + username)
            );
        }
    }

}
