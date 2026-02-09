package com.inventory.service;
// INI PAKAI USER
import java.util.List;

import com.inventory.exception.AuthException;
import com.inventory.exception.ValidationException;
import com.inventory.model.User;
import com.inventory.repository.UserManagementRepository;

public class UserManagementService {
    private final UserManagementRepository repository = new UserManagementRepository();

    private void authorize(User user) {
        if (!user.isAdmin() && !user.isManager()) {
            throw new AuthException("Akses ditolak: role tidak memiliki izin");
        }
    }

    public List<User> getAllUsers(User user) {
        //! User harus admin atau manager INGAT WOI
        authorize(user);
        try {
            return repository.getAllUsers();
        } catch (Exception e) {
            throw new RuntimeException("Gagal mengambil semua pengguna", e);
        }
    }

    public User getUserById(int id) {
        try {
            User user = repository.getUserById(id);
            if (user == null) {
                throw new ValidationException("User tidak ditemukan");
            }
            return user;
        } catch (Exception e) {
            throw new ValidationException("Error saat mengambil user");
        }
    }

    public void insertUser(User user, User newUser) {
        authorize(user);
        try {
            repository.insertUser(newUser);
        } catch (Exception e) {
            throw new RuntimeException("Gagal memasukkan pengguna baru", e);
        }
    }

    public void updateUser(User user, User updatedUser) {
        authorize(user);
        try {
            repository.updateUser(updatedUser);
        } catch (Exception e) {
            throw new RuntimeException("Gagal memperbarui pengguna", e);
        }
    }

    public void deleteUserById(User user, int id) {
        authorize(user);
        try {
            repository.deleteUserById(id);
        } catch (Exception e) {
            throw new RuntimeException("Gagal menghapus pengguna berdasarkan ID", e);
        }
    }

    public void suspendUserById(User user, int id) {
        //! User harus admin atau manager INGAT WOI
        authorize(user);
        try {
            repository.suspendUserById(id);
        } catch (Exception e) {
            throw new RuntimeException("Gagal memblokir pengguna berdasarkan ID", e);
        }
    }


    
}
