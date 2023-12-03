package guru.qa.rococo.config;

import com.codeborne.selenide.Configuration;
import org.openqa.selenium.chrome.ChromeOptions;

public class DockerConfig implements Config {
    static final DockerConfig config = new DockerConfig();

    static {
        Configuration.remote = "http://selenoid:4444/wd";
        Configuration.browser = "chrome";
        Configuration.browserCapabilities = new ChromeOptions()
                .addArguments("--no-sandbox");
    }

    @Override
    public String databaseHost() {
        return "rococo-all-db";
    }

    @Override
    public String rococoFrontendUrl() {
        return "http://client.rococo.dc";
    }

    @Override
    public String rococoAuthUrl() {
        return "http://auth.rococo.dc:9000";
    }

    @Override
    public String userdataGrpcAddress() {
        return "rococo-userdata";
    }

    @Override
    public int userdataGrpcPort() {
        return 8091;
    }

    @Override
    public String artistGrpcAddress() {
        return "rococo-artist";
    }

    @Override
    public int artistGrpcPort() {
        return 8092;
    }

    @Override
    public String museumGrpcAddress() {
        return "rococo-museum";
    }

    @Override
    public int museumGrpcPort() {
        return 8093;
    }

    @Override
    public String geoGrpcAddress() {
        return "rococo-geo";
    }

    @Override
    public int geoGrpcPort() {
        return 8094;
    }

    @Override
    public String paintingGrpcAddress() {
        return "rococo-painting";
    }

    @Override
    public int paintingGrpcPort() {
        return 8095;
    }
}
