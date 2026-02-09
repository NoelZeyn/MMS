package com.inventory.audit;

import java.time.LocalDateTime;

public class AuditLogger {

    public static void log(String username, String action, String target) {
        System.out.println(
            "[" + LocalDateTime.now() + "] " +
            username + " | " + action + " | " + target
        );
    }
}
