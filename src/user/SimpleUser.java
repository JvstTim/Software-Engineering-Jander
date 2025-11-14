package user;

public class SimpleUser implements User {
    private volatile String username = "Anonymous";

    @Override public void setUsername(String name) { this.username = (name == null || name.isBlank()) ? "Anonymous" : name.trim(); }
    @Override public String getUsername() { return username; }
    @Override public boolean isUsernameSet() { return username != null && !username.isBlank() && !"Anonymous".equals(username); }
}
