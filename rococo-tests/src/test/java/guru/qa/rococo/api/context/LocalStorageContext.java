package guru.qa.rococo.api.context;

import guru.qa.rococo.util.OauthUtils;

import java.util.HashMap;
import java.util.Map;

public class LocalStorageContext {
    private static final ThreadLocal<LocalStorageContext>
            INSTANCE = ThreadLocal.withInitial(LocalStorageContext::new);
    private static final String
            CODE_CHALLENGE_KEY = "CODE_CHALLENGE_KEY",
            CODE_VERIFIER_KEY = "CODE_VERIFIER_KEY",
            TOKEN_KEY = "TOKEN_KEY",
            CODE_KEY = "CODE_KEY";

    private final Map<String, String> store = new HashMap<>();

    public static LocalStorageContext getInstance() {
        return INSTANCE.get();
    }

    public LocalStorageContext init() {
        clearContext();
        return getInstance();
    }

    public String getCodeChallenge() {
        if (store.get(CODE_CHALLENGE_KEY) == null) {
            final String codeVerifier = OauthUtils.generateCodeVerifier();
            setCodeChallenge(OauthUtils.generateCodeChallenge(codeVerifier));
            setCodeVerifier(codeVerifier);
        }
        return store.get(CODE_CHALLENGE_KEY);
    }

    public void setCodeChallenge(String codeChallenge) {
        store.put(CODE_CHALLENGE_KEY, codeChallenge);
    }

    public String getCodeVerifier() {
        return store.get(CODE_VERIFIER_KEY);
    }

    public void setCodeVerifier(String codeVerifier) {
        store.put(CODE_VERIFIER_KEY, codeVerifier);
    }

    public String getToken() {
        return store.get(TOKEN_KEY);
    }

    public void setToken(String token) {
        store.put(TOKEN_KEY, token);
    }

    public String getCode() {
        return store.get(CODE_KEY);
    }

    public void setCode(String code) {
        store.put(CODE_KEY, code);
    }

    public void clearContext() {
        store.clear();
    }
}
