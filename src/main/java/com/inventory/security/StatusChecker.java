package main.java.com.inventory.security;

import main.java.com.inventory.model.User;
import main.java.com.inventory.repository.UserRepository;

public class StatusChecker {

    private final UserRepository userRepository = new UserRepository();

    public String getUserStatus(String username) throws Exception {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new Exception("User not found");
        }
        
        String status = user.getStatus();
        return formatStatus(status);
    }

    private String formatStatus(String status) {
        switch (status.toLowerCase()) {
            case "active":
                return "ACTIVE";
            case "inactive":
                return "INACTIVE";
            case "suspended":
                return "SUSPENDED";
            case "pending":
                return "PENDING";
            default:
                return "SUSPENDED";
        }
    }
}
