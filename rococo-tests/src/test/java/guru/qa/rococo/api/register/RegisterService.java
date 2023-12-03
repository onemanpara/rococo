package guru.qa.rococo.api.register;

import retrofit2.Call;
import retrofit2.http.*;

public interface RegisterService {

    @GET("/register")
    Call<Void> register();

    @POST("/register")
    @FormUrlEncoded
    Call<Void> registerUser(
            @Header("Cookie") String cookie,
            @Field("_csrf") String csrf,
            @Field("username") String username,
            @Field("password") String password,
            @Field("passwordSubmit") String passwordSubmit
    );

}
