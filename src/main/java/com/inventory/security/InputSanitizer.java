package main.java.com.inventory.security;

public class InputSanitizer {

    public static String sanitize(String input) {
        if (input == null) return null;
        return input.replaceAll("[<>\"']", "");
    }
}
