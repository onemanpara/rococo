package guru.qa.rococo.test.grpc;

import guru.qa.grpc.rococo.grpc.RococoUserdataServiceGrpc;
import guru.qa.grpc.rococo.grpc.UpdateUserRequest;
import guru.qa.grpc.rococo.grpc.UserRequest;
import guru.qa.grpc.rococo.grpc.UserResponse;
import guru.qa.rococo.jupiter.annotation.GenerateUser;
import guru.qa.rococo.jupiter.annotation.GeneratedUser;
import guru.qa.rococo.model.UserJson;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.qameta.allure.grpc.AllureGrpc;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.google.protobuf.ByteString.copyFromUtf8;
import static guru.qa.rococo.util.DataUtil.generateRandomName;
import static guru.qa.rococo.util.ImageUtil.convertImageToBase64;
import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("GRPC: Профиль пользователя")
public class UserdataGrpcTest extends BaseGrpcTest {
    private static final Channel userdataChannel;

    static {
        userdataChannel = ManagedChannelBuilder
                .forAddress(CFG.userdataGrpcAddress(), CFG.userdataGrpcPort())
                .intercept(new AllureGrpc())
                .usePlaintext()
                .build();
    }

    private final RococoUserdataServiceGrpc.RococoUserdataServiceBlockingStub userdataStub = RococoUserdataServiceGrpc.newBlockingStub(userdataChannel);

    @Test
    @DisplayName("GRPC: Для нового пользователя должна возвращаться информация из rococo-userdata c пустыми значениями")
    @GenerateUser
    @Tag("GRPC")
    void shouldReturnUserdataFromDB(@GeneratedUser UserJson createdUser) {
        UserRequest userRequest = UserRequest.newBuilder()
                .setUsername(createdUser.getUsername())
                .build();
        final UserResponse currentUserResponse = userdataStub.getUser(userRequest);

        step("Check that response contains user ID", () ->
                assertTrue(currentUserResponse.getId().toStringUtf8().matches(ID_REGEXP))
        );
        step("Check that response contains username", () ->
                assertEquals(createdUser.getUsername(), currentUserResponse.getUsername())
        );
        step("Check that response contains empty firstname", () ->
                assertTrue(currentUserResponse.getFirstname().isEmpty())
        );
        step("Check that response contains empty lastname", () ->
                assertTrue(currentUserResponse.getLastname().isEmpty())
        );
        step("Check that response contains empty avatar", () ->
                assertTrue(currentUserResponse.getAvatar().toStringUtf8().isEmpty())
        );
    }

    @Test
    @DisplayName("GRPC: При обновлении пользователя должны сохраняться значения в rococo-userdata")
    @Tag("GRPC")
    @GenerateUser
    void shouldUpdateUser(@GeneratedUser UserJson createdUser) {
        UserRequest userRequest = UserRequest.newBuilder()
                .setUsername(createdUser.getUsername())
                .build();
        final UserResponse user = userdataStub.getUser(userRequest);
        String firstname = generateRandomName();
        String lastname = generateRandomName();


        UpdateUserRequest updateUserRequest = UpdateUserRequest.newBuilder()
                .setId(user.getId())
                .setUsername(createdUser.getUsername())
                .setFirstname(firstname)
                .setLastname(lastname)
                .setAvatar(copyFromUtf8(convertImageToBase64(PHOTO_PATH)))
                .build();

        final UserResponse response = userdataStub.updateUser(updateUserRequest);

        step("Check that response contains user ID", () ->
                assertEquals(user.getId(), response.getId())
        );
        step("Check that response contains user firstname", () ->
                assertEquals(firstname, response.getFirstname())
        );
        step("Check that response contains updated user lastname", () ->
                assertEquals(lastname, response.getLastname())
        );
        step("Check that response contains updated user photo", () ->
                assertEquals(convertImageToBase64(PHOTO_PATH), response.getAvatar().toStringUtf8())
        );
    }
}
