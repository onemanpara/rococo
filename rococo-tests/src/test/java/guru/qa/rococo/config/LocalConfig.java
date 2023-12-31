package guru.qa.rococo.config;

import com.codeborne.selenide.Configuration;

public class LocalConfig implements Config {
    static final LocalConfig config = new LocalConfig();

    static {
        Configuration.browserSize = "1920x1080";
    }

    @Override
    public String databaseAddress() {
        return "localhost:3306";
    }

    @Override
    public String rococoFrontendUrl() {
        return "http://127.0.0.1:3000";
    }

    @Override
    public String rococoAuthUrl() {
        return "http://127.0.0.1:9000";
    }

    @Override
    public String userdataGrpcAddress() {
        return "localhost";
    }

    @Override
    public String artistGrpcAddress() {
        return "localhost";
    }

    @Override
    public String museumGrpcAddress() {
        return "localhost";
    }

    @Override
    public String geoGrpcAddress() {
        return "localhost";
    }

    @Override
    public String paintingGrpcAddress() {
        return "localhost";
    }

}
