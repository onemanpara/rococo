package guru.qa.rococo.config;

import com.codeborne.selenide.Configuration;
import org.openqa.selenium.chrome.ChromeOptions;

public class DockerConfig implements Config {
    static final DockerConfig config = new DockerConfig();

    static {
        Configuration.remote = "http://selenoid:4444/wd/hub";
        Configuration.browserSize = "1920x1080";
        Configuration.browserCapabilities = new ChromeOptions()
                .addArguments("--no-sandbox");
    }

    @Override
    public String databaseAddress() {
        return "rococo-all-db:3306";
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
        return "userdata.rococo.dc";
    }

    @Override
    public String artistGrpcAddress() {
        return "artist.rococo.dc";
    }

    @Override
    public String museumGrpcAddress() {
        return "museum.rococo.dc";
    }

    @Override
    public String geoGrpcAddress() {
        return "geo.rococo.dc";
    }

    @Override
    public String paintingGrpcAddress() {
        return "painting.rococo.dc";
    }
}
