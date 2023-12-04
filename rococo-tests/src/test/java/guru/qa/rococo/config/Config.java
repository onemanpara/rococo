package guru.qa.rococo.config;

public interface Config {
    static Config getConfig() {
        if ("docker".equals(System.getProperty("test.env"))) {
            return DockerConfig.config;
        }
        return LocalConfig.config;
    }

    String databaseAddress();

    String rococoFrontendUrl();

    String rococoAuthUrl();

    default String databaseUser() {
        return "root";
    }

    default String databasePassword() {
        return "secret";
    }

    String userdataGrpcAddress();

    default int userdataGrpcPort() {
        return 8091;
    }

    String artistGrpcAddress();

    default int artistGrpcPort() {
        return 8092;
    }

    String museumGrpcAddress();

    default int museumGrpcPort() {
        return 8093;
    }

    String geoGrpcAddress();

    default int geoGrpcPort() {
        return 8094;
    }

    String paintingGrpcAddress();

    default int paintingGrpcPort() {
        return 8095;
    }
}
