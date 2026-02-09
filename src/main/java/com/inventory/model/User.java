package com.inventory.model;

public class User {

    private int id;
    private String username;
    private String passwordHash;
    private String role;
    private String status;
    private String NID;
    private int subBidangId;
    private String subBidangName;

    // Constructor 1: Untuk mengambil data lengkap dari DB (Termasuk Nama Sub-Bidang hasil JOIN)
    public User(int id, String username, String NID, String passwordHash, String role, String status, String subBidangName) {
        this.id = id;
        this.username = username;
        this.NID = NID;
        this.passwordHash = passwordHash;
        this.role = role;
        this.status = status;
        this.subBidangName = subBidangName;
    }

    // Constructor 2: Untuk Operasi Update/Insert (Membutuhkan ID Sub-Bidang secara spesifik)
    public User(int id, String username, String NID, String passwordHash, String role, String status, int subBidangId) {
        this.id = id;
        this.username = username;
        this.NID = NID;
        this.passwordHash = passwordHash;
        this.role = role;
        this.status = status;
        this.subBidangId = subBidangId;
    }

    // Constructor 3: Untuk Insert User Baru (Tanpa ID)
    public User(String username, String NID, String passwordHash, String role, String status, int subBidangId) {
        this.username = username;
        this.NID = NID;
        this.passwordHash = passwordHash;
        this.role = role;
        this.status = status;
        this.subBidangId = subBidangId;
    }

    // --- GETTERS & SETTERS ---

    public int getId() { return id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getNID() { return NID; }
    public void setNID(String NID) { this.NID = NID; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getSubBidangId() { return subBidangId; }
    public void setSubBidangId(int subBidangId) { this.subBidangId = subBidangId; }

    public String getSubBidangName() { return subBidangName; }
    public void setSubBidangName(String subBidangName) { this.subBidangName = subBidangName; }

    // --- ROLE CHECKS ---

    public boolean isAdmin() { return "ADMIN".equals(role); }
    public boolean isManager() { return "MANAGER".equals(role); }
    public boolean isStaff() { return "STAFF".equals(role); }
    public boolean isUser() { return "USER".equals(role); }
    public boolean isBudget() { return "BUDGETER".equals(role); }
    public boolean isAsman() { return "ASMAN".equals(role); }
    public boolean isSuperAdmin() { return "SUPERADMIN".equals(role); }

}