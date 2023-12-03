package guru.qa.rococo.config;

import com.codeborne.selenide.Configuration;

public class LocalConfig implements Config {
    static final LocalConfig config = new LocalConfig();

    static {
        Configuration.browserSize = "1920x1080";
    }

    @Override
    public String databaseHost() {
        return "localhost";
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
    public int userdataGrpcPort() {
        return 8091;
    }

    @Override
    public String artistGrpcAddress() {
        return "localhost";
    }

    @Override
    public int artistGrpcPort() {
        return 8092;
    }

    @Override
    public String museumGrpcAddress() {
        return "localhost";
    }

    @Override
    public int museumGrpcPort() {
        return 8093;
    }

    @Override
    public String geoGrpcAddress() {
        return "localhost";
    }

    @Override
    public int geoGrpcPort() {
        return 8094;
    }

    @Override
    public String paintingGrpcAddress() {
        return "localhost";
    }

    @Override
    public int paintingGrpcPort() {
        return 8095;
    }


}
