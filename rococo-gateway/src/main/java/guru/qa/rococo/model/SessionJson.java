package guru.qa.rococo.model;

import java.time.Instant;

public record SessionJson(
        String username,
        Instant issuedAt,
        Instant expiresAt
) {

    public static SessionJson emptySession() {
        return new SessionJson(null, null, null);
    }
}
