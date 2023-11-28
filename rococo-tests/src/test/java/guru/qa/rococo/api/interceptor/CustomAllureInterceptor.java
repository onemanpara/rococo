package guru.qa.rococo.api.interceptor;

import io.qameta.allure.okhttp3.AllureOkHttp3;

public class CustomAllureInterceptor extends AllureOkHttp3 {
    public CustomAllureInterceptor() {
        this.setRequestTemplate("request.ftl");
        this.setResponseTemplate("response.ftl");
    }
}
