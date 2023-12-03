package guru.qa.rococo.config;

public interface Config {
    static Config getConfig() {
        if ("docker".equals(System.getProperty("test.env"))) {
            return DockerConfig.config;
        }
        return LocalConfig.config;
    }

    String databaseHost();

    String rococoFrontendUrl();

    String rococoAuthUrl();

    default String databaseUser() {
        return "root";
    }

    default String databasePassword() {
        return "secret";
    }

    default int databasePort() {
        return 3306;
    }

    String userdataGrpcAddress();

    int userdataGrpcPort();

    String artistGrpcAddress();

    int artistGrpcPort();

    String museumGrpcAddress();

    int museumGrpcPort();

    String geoGrpcAddress();

    int geoGrpcPort();

    String paintingGrpcAddress();

    int paintingGrpcPort();
}
