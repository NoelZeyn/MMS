package main.java.com.inventory.security;

public class InputSanitizer {

    private static final int MAX_LENGTH = 255;

    public static String sanitize(String input) {
        if (input == null) return null;

        if (input.length() > MAX_LENGTH) {
            input = input.substring(0, MAX_LENGTH);
        }

        return input.replaceAll("[<>\"']", "");
    }

    public static int sanitizeInt(String input) throws NumberFormatException {
        if (input == null) throw new NumberFormatException("Input is null");

        if (input.length() > MAX_LENGTH) {
            input = input.substring(0, MAX_LENGTH);
        }

        String sanitized = input.replaceAll("[^0-9]", "");
        if (sanitized.isEmpty()) throw new NumberFormatException("Input does not contain valid digits");

        return Integer.parseInt(sanitized);
    }
}
