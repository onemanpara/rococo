package guru.qa.rococo.api.interceptor;

import guru.qa.rococo.api.context.CookieContext;
import okhttp3.Interceptor;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;

public class ReceivedCodeInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        CookieContext cookieContext = CookieContext.getInstance();
        Response response = chain.proceed(chain.request());
        List<String> setCookieHeader = response.headers("Set-cookie");
        if (!setCookieHeader.isEmpty()) {
            for (String header : setCookieHeader) {
                for (String cookie : header.split(";")) {
                    String[] rawCookie = cookie.split("=");
                    if (rawCookie[0].equals("JSESSIONID")) {
                        cookieContext.setJsessionid(rawCookie.length == 2 ? rawCookie[1] : null);
                    } else if (rawCookie[0].equals("XSRF-TOKEN")) {
                        cookieContext.setXsrf(rawCookie.length == 2 ? rawCookie[1] : null);
                    }
                }
            }
        }
        return response;
    }
}