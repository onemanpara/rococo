package guru.qa.rococo.db;

import guru.qa.rococo.config.Config;
import org.apache.commons.lang3.StringUtils;

public enum ServiceDB {
    AUTH("jdbc:mysql://%s:%d/rococo-auth"),
    USERDATA("jdbc:mysql://%s:%d/rococo-userdata"),
    MUSEUM("jdbc:mysql://%s:%d/rococo-museum"),
    ARTIST("jdbc:mysql://%s:%d/rococo-artist"),
    PAINTING("jdbc:mysql://%s:%d/rococo-painting"),
    GEO("jdbc:mysql://%s:%d/rococo-geo");

    private static final Config cfg = Config.getConfig();
    private final String url;

    ServiceDB(String url) {
        this.url = url;
    }

    public String getUrl() {
        return String.format(this.url, cfg.databaseHost(), cfg.databasePort());
    }

    public String p6spyUrl() {
        return "jdbc:p6spy:mysql:" + StringUtils.substringAfter(getUrl(), "jdbc:mysql:");
    }
}
