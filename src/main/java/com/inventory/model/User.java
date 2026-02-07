package main.java.com.inventory.model;

public class User {

    private int id;
    private String username;
    private String passwordHash;
    private String role;
    private String status;
    private String NID;

    public User(int id, String username, String NID, String passwordHash, String role, String status) {
        this.id = id;
        this.username = username;
        this.NID = NID;
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

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNID() {
        return NID;
    }

    public void setNID(String NID) {
        this.NID = NID;
    }
    
    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public boolean isAdmin() {
        return "ADMIN".equals(role);
    }
    public boolean isManager() {
        return "MANAGER".equals(role);
    }
}
