package guru.qa.rococo.api;

import guru.qa.rococo.api.interceptor.CustomAllureInterceptor;
import guru.qa.rococo.config.Config;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public abstract class RestService {
    protected static final Config CFG = Config.getConfig();
    protected final OkHttpClient httpClient;
    protected final Retrofit retrofit;

    public RestService(String baseUrl) {
        this(baseUrl, false, (Interceptor) null);
    }

    public RestService(String baseUrl, boolean followRedirect) {
        this(baseUrl, followRedirect, (Interceptor) null);
    }

    public RestService(String baseUrl, boolean followRedirect, Interceptor... interceptors) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .followRedirects(followRedirect)
                .addInterceptor(new CustomAllureInterceptor());

        if (interceptors != null) {
            for (Interceptor interceptor : interceptors) {
                builder.addNetworkInterceptor(interceptor);
            }
        }

        builder.addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));

        this.httpClient = builder.build();
        this.retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(httpClient)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }
}
