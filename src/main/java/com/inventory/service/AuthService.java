package main.java.com.inventory.service;

import main.java.com.inventory.exception.AuthException;
import main.java.com.inventory.model.User;
import main.java.com.inventory.repository.UserRepository;
import main.java.com.inventory.security.PasswordHasher;
import main.java.com.inventory.security.StatusChecker;

public class AuthService {

    private final UserRepository userRepository = new UserRepository();
    private final StatusChecker statusChecker = new StatusChecker();

    public User login(String username, String password) throws Exception {

        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new AuthException("Username atau Password salah");
        }

        String hashedInput = PasswordHasher.hash(password);
        if (!hashedInput.equals(user.getPasswordHash())) {
            throw new AuthException("Password salah");
        }

        String status = statusChecker.getUserStatus(username);
        if (!status.equals("ACTIVE")) {
            throw new AuthException("Akun tidak aktif: " + status);
        }

        return user;
    }

    public void register(String NID, String username, String rawPassword, String role) throws Exception {
        if (userRepository.findByUsername(username) != null) {
            throw new Exception("Username sudah digunakan!");
        }
        String hashedPassword = PasswordHasher.hash(rawPassword);
        User newUser = new User(0, username, NID, hashedPassword, role.toUpperCase(), "PENDING");
        userRepository.register(newUser);
    }
}