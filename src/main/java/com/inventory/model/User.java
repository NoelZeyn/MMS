package main.java.com.inventory.model;

public class User {

    private int id;
    private String username;
    private String passwordHash;
    private String role;
    private String status;

    public User(int id, String username, String passwordHash, String role, String status) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
        this.status = status;   
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getRole() {
        return role;
    }

    public String getStatus() {
        return status;
    }

    public boolean isAdmin() {
        return "ADMIN".equals(role);
    }
    public boolean isManager() {
        return "MANAGER".equals(role);
    }
}
