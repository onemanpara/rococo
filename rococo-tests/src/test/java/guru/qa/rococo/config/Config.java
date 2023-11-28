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

    String geoGrpcAddress();

    int geoGrpcPort();
}
