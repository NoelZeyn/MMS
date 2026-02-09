package com.inventory.controller;
import java.util.List;

import com.inventory.model.User;
import com.inventory.service.UserManagementService;

public class UserManagementController {

    private final UserManagementService service;

    public UserManagementController(UserManagementService service) {
        this.service = service;
    }
    public List<User> getAllUsers(User user) {
        return service.getAllUsers(user);
    }

    public void insertUser(User user, User newUser) {
        service.insertUser(user, newUser);
    }
    public User getUserById(int id) {
        return service.getUserById(id);
    }
    public void updateUser(User user, User updatedUser) {
        service.updateUser(user, updatedUser);
    }

    public void insertUser(User user) {
        service.insertUser(user, user);
    }
    public void deleteUserById(User user, int id) {
        service.deleteUserById(user, id);
    }

    public void suspendUserById(User user, int id) {
        service.suspendUserById(user, id);
    }
}