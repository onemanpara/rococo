package guru.qa.rococo.model;

import java.time.Instant;
import java.util.Objects;

public class SessionJson {
    private String username;
    private Instant issuedAt;
    private Instant expiresAt;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Instant getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(Instant issuedAt) {
        this.issuedAt = issuedAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public static SessionJson emptySession() {
        return new SessionJson(null, null, null);
    }

    public SessionJson(String username, Instant issuedAt, Instant expiresAt) {
        this.username = username;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SessionJson that = (SessionJson) o;
        return Objects.equals(expiresAt, that.expiresAt) && Objects.equals(issuedAt, that.issuedAt) && Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expiresAt, issuedAt, username);
    }
}
