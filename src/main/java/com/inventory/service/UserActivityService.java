package com.inventory.service;

import java.util.List;

import com.inventory.exception.AuthException;
import com.inventory.exception.ValidationException;
import com.inventory.model.User;
import com.inventory.model.UserActivity;
import com.inventory.repository.UserActivityRepository;

public class UserActivityService {
    private final UserActivityRepository repository = new UserActivityRepository();

    private void authorize(User user) {
        if (user == null || (!user.isAdmin() && !user.isManager() && !user.isSuperAdmin())) {
            throw new AuthException("Akses ditolak: role tidak memiliki izin");
        }
    }

    public List<UserActivity> getAllUserActivities(User user) {
        //! User harus admin atau manager INGAT WOI
        authorize(user);
        try {
            return repository.getAllUserActivities();
        } catch (Exception e) {
            throw new ValidationException("Gagal mengambil semua log aktivitas pengguna");
        }
    }

    public void insertLogActivity(User user, String activity, String targetTable, int targetId, String description) {
        try {
            String timestamp = java.time.LocalDateTime.now()
                    .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            UserActivity activityLog = new UserActivity(user.getId(), activity, targetTable, targetId, description,
                    timestamp);
            repository.insertLogActivity(activityLog);
        } catch (Exception e) {
            System.err.println("CRITICAL: Gagal mencatat log untuk " + activity);
        }
    }

    public void updateLogActivity(User user, int logId, String activity, String targetTable, int targetId,
            String description, String timestamp) {
        authorize(user);
        try {
            UserActivity activityLog = new UserActivity(logId, user.getId(), activity, targetTable, targetId,
                    description, timestamp);
            repository.updateLogActivity(activityLog);
        } catch (Exception e) {
            throw new ValidationException("Gagal memperbarui log aktivitas pengguna");
        }
    }

    public void getLogsByUserId(User user, int targetUserId) {
        authorize(user);
        try {
            repository.getLogsByUserId(targetUserId);
        } catch (Exception e) {
            throw new ValidationException("Gagal mengambil log aktivitas pengguna");
        }
    }
}
