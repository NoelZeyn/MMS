package com.inventory.model;

public class UserActivity {
    private int id;
    private int userId;
    private String activity;
    private String targetTable;
    private int targetId;
    private String description;
    private String timestamp;

    public UserActivity(int userId, String activity, String targetTable, int targetId, String description,
            String timestamp) {
        this.userId = userId;
        this.activity = activity;
        this.targetTable = targetTable;
        this.targetId = targetId;
        this.description = description;
        this.timestamp = timestamp;
    }

    public UserActivity(int id, int userId, String activity, String targetTable, int targetId, String description,
            String timestamp) {
        this.id = id;
        this.userId = userId;
        this.activity = activity;
        this.targetTable = targetTable;
        this.targetId = targetId;
        this.description = description;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int setUserId(int userId) {
        this.userId = userId;
        return userId;
    }

    public String getActivity() {
        return activity;
    }

    public String setActivity(String activity) {
        this.activity = activity;
        return activity;
    }

    public String getTargetTable() {
        return targetTable;
    }

    public String setTargetTable(String targetTable) {
        this.targetTable = targetTable;
        return targetTable;
    }

    public int getTargetId() {
        return targetId;
    }

    public int setTargetId(int targetId) {
        this.targetId = targetId;
        return targetId;
    }

    public String getDescription() {
        return description;
    }

    public String setDescription(String description) {
        this.description = description;
        return description;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String setTimestamp(String timestamp) {
        this.timestamp = timestamp;
        return timestamp;
    }
}
