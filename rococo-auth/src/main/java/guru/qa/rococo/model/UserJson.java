package guru.qa.rococo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class UserJson {
    @JsonProperty("username")
    private String username;

    public UserJson() {
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
        UserJson userJson = (UserJson) o;
        return Objects.equals(username, userJson.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

}
