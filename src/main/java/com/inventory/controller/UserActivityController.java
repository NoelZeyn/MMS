package main.java.com.inventory.controller;

import java.util.List;

import main.java.com.inventory.model.User;
import main.java.com.inventory.model.UserActivity;
import main.java.com.inventory.service.UserActivityService;

public class UserActivityController {
    private final UserActivityService service;

    public UserActivityController(UserActivityService service) {
        this.service = service;
    }
    public void insertLogActivity(User user, String activity, String targetTable, int targetId, String description) {
        service.insertLogActivity(user, activity, targetTable, targetId, description);
    }
    public void updateLogActivity(User user, int logId, String activity, String targetTable, int targetId,
            String description, String timestamp) {
        service.updateLogActivity(user, logId, activity, targetTable, targetId, description, timestamp);
    }
    public void getLogsByUserId(User user, int targetUserId) {
        service.getLogsByUserId(user, targetUserId);
    }

    public List<UserActivity> getAllUserActivities(User user) {
        return service.getAllUserActivities(user);
    }

    
}
