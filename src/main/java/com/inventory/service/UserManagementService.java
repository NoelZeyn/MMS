package com.inventory.service;

import java.util.List;
import com.inventory.exception.AuthException;
import com.inventory.exception.ValidationException;
import com.inventory.model.User;
import com.inventory.repository.UserManagementRepository;

public class UserManagementService {
    private final UserManagementRepository repository = new UserManagementRepository();

    private void authorize(User user) {
        if (user == null || (!user.isAdmin() && !user.isManager() && !user.isSuperAdmin())) {
            throw new AuthException("Akses ditolak: Anda tidak memiliki izin administratif.");
        }
    }

    public List<User> getAllUsers(User currentUser) {
        authorize(currentUser);
        return repository.getAllUsers();
    }

    public User getUserById(int id) {
        User user = repository.getUserById(id);
        if (user == null) {
            throw new ValidationException("User dengan ID " + id + " tidak ditemukan.");
        }
        return user;
    }

    public void insertUser(User currentUser, User newUser) {
        authorize(currentUser);
        
        // Validasi simpel
        if (newUser.getUsername() == null || newUser.getUsername().isBlank()) {
            throw new ValidationException("Username tidak boleh kosong.");
        }
        
        repository.insertUser(newUser);
    }

    public void updateUser(User currentUser, User updatedUser) {
        authorize(currentUser);
        
        // Pastikan user ada sebelum update
        getUserById(updatedUser.getId());
        
        repository.updateUser(updatedUser);
    }

    public void deleteUserById(User currentUser, int id) {
        authorize(currentUser);
        
        if (currentUser.getId() == id) {
            throw new ValidationException("Anda tidak bisa menghapus akun Anda sendiri.");
        }
        
        repository.deleteUserById(id);
    }

    public void suspendUserById(User currentUser, int id) {
        authorize(currentUser);
        repository.suspendUserById(id);
    }
}