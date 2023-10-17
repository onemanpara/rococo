package guru.qa.rococo.service;

import guru.qa.rococo.model.SessionJson;
import guru.qa.rococo.model.UserJson;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@Qualifier("rest")
public class RestUserDataClient implements UserDataClient {

    private final WebClient webClient;
    private final String rococoUserdataBaseUri;

    @Autowired
    public RestUserDataClient(WebClient webClient,
                              @Value("${rococo-userdata.base-uri}") String rococoUserdataBaseUri) {
        this.webClient = webClient;
        this.rococoUserdataBaseUri = rococoUserdataBaseUri;
    }

    @Override
    public @Nonnull
    UserJson updateUserInfo(@Nonnull UserJson user) {
        return webClient.patch()
                .uri(rococoUserdataBaseUri + "/api/user")
                .body(Mono.just(user), UserJson.class)
                .retrieve()
                .bodyToMono(UserJson.class)
                .block();
    }

    @Override
    public @Nonnull
    UserJson user(@Nonnull String username) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", username);
        URI uri = UriComponentsBuilder.fromHttpUrl(rococoUserdataBaseUri + "/api/user").queryParams(params).build().toUri();

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(UserJson.class)
                .block();
    }

    public SessionJson getSessionInfo(@Nonnull String username) {
        return webClient.get()
                .uri(rococoUserdataBaseUri + "/api/session")
                .retrieve()
                .bodyToMono(SessionJson.class)
                .block();
    }

}
