package guru.qa.rococo.api.context;

import java.util.HashMap;
import java.util.Map;

public class CookieContext {
    private static final ThreadLocal<CookieContext>
            INSTANCE = ThreadLocal.withInitial(CookieContext::new);
    private static final String
            JSESSIONID_KEY = "JSESSIONID",
            XSRF_TOKEN_KEY = "XSRF-TOKEN";

    private final Map<String, String> store = new HashMap<>();

    public static CookieContext getInstance() {
        return INSTANCE.get();
    }

    public CookieContext init() {
        clearContext();
        return getInstance();
    }

    public void setJsessionid(String cookie) {
        store.put(JSESSIONID_KEY, cookie);
    }

    public void setXsrf(String xsrf) {
        store.put(XSRF_TOKEN_KEY, xsrf);
    }

    public String getJSessionIdCookieValue() {
        return store.get(JSESSIONID_KEY);
    }

    public String getXsrfTokenCookieValue() {
        return store.get(XSRF_TOKEN_KEY);
    }

    public String getJSessionIdFormattedCookie() {
        String cookieValue = store.get(JSESSIONID_KEY);
        if (cookieValue == null) {
            return JSESSIONID_KEY + "=";
        }
        return JSESSIONID_KEY + "=" + store.get(JSESSIONID_KEY);
    }

    public String getXsrfTokenFormattedCookie() {
        String cookieValue = store.get(XSRF_TOKEN_KEY);
        if (cookieValue == null) {
            return XSRF_TOKEN_KEY + "=";
        }
        return XSRF_TOKEN_KEY + "=" + store.get(XSRF_TOKEN_KEY);
    }

    public void clearContext() {
        store.clear();
    }
}
