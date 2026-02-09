package com.inventory.service;

import com.inventory.exception.AuthException;
import com.inventory.model.User;
import com.inventory.repository.UserRepository;
import com.inventory.security.PasswordHasher;
import com.inventory.security.StatusChecker;

public class AuthService {

    private final UserRepository userRepository = new UserRepository();
    private final StatusChecker statusChecker = new StatusChecker();

    public User login(String username, String password) throws Exception {

        User user = userRepository.findByUsername(username);
        String hashedInput = PasswordHasher.hash(password);
        if (user == null || !hashedInput.equals(user.getPasswordHash())) {
            throw new AuthException("Username atau Password salah");
        }
        String status = statusChecker.getUserStatus(username);
        if (!status.equals("ACTIVE")) {
            throw new AuthException("Akun tidak aktif: " + status);
        }

        return user;
    }

    public void register(String NID, String username, String rawPassword, String role, int subBidangId) throws Exception {
        if (userRepository.findByUsername(username) != null) {
            throw new Exception("Username sudah digunakan!");
        }
        String hashedPassword = PasswordHasher.hash(rawPassword);
        User newUser = new User(0, username, NID, hashedPassword, role.toUpperCase(), "PENDING", subBidangId);
        userRepository.register(newUser);
    }
}