package guru.qa.rococo.api.auth;

import com.fasterxml.jackson.databind.JsonNode;
import retrofit2.Call;
import retrofit2.http.*;

public interface AuthService {
    @GET("/oauth2/authorize")
    Call<Void> authorize(
            @Query("response_type") String responseType,
            @Query("client_id") String clientId,
            @Query("scope") String scope,
            @Query(value = "redirect_uri", encoded = true) String redirectUri,
            @Query("code_challenge") String codeChallenge,
            @Query("code_challenge_method") String codeChallengeMethod
    );

    @POST("/login")
    @FormUrlEncoded
    Call<Void> login(
            @Field("username") String username,
            @Field("password") String password,
            @Field("_csrf") String csrf
    );

    @POST("oauth2/token")
    Call<JsonNode> token(
            @Header("Authorization") String basicAuthorization,
            @Query("client_id") String clientId,
            @Query(value = "redirect_uri", encoded = true) String redirectUri,
            @Query("grant_type") String grantType,
            @Query("code") String code,
            @Query("code_verifier") String codeVerifier
    );

}
