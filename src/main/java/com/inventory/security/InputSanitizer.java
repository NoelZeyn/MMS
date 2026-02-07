package main.java.com.inventory.security;

public class InputSanitizer {

    public static String sanitize(String input) {
        if (input == null) return null;
        return input.replaceAll("[<>\"']", "");
    }

    public static int sanitizeInt(String input) throws NumberFormatException {
        String sanitized = sanitize(input);
        return Integer.parseInt(sanitized);
    }
}
