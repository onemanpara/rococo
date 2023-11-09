package guru.qa.rococo.model;

import java.time.Instant;
import java.util.Objects;

public class SessionJson {

    private Instant expiresAt;
    private Instant issuedAt;
    private String username;

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Instant getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(Instant issuedAt) {
        this.issuedAt = issuedAt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
