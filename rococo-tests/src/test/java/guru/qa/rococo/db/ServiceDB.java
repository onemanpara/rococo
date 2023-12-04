package guru.qa.rococo.db;

import guru.qa.rococo.config.Config;
import org.apache.commons.lang3.StringUtils;

public enum ServiceDB {
    AUTH("jdbc:mysql://%s/rococo-auth"),
    USERDATA("jdbc:mysql://%s/rococo-userdata"),
    MUSEUM("jdbc:mysql://%s/rococo-museum"),
    ARTIST("jdbc:mysql://%s/rococo-artist"),
    PAINTING("jdbc:mysql://%s/rococo-painting"),
    GEO("jdbc:mysql://%s/rococo-geo");

    private static final Config cfg = Config.getConfig();
    private final String url;

    ServiceDB(String url) {
        this.url = url;
    }

    public String getUrl() {
        return String.format(url, cfg.databaseAddress());
    }

    public String p6spyUrl() {
        return "jdbc:p6spy:" + StringUtils.substringAfter(getUrl(), "jdbc:");
    }
}
