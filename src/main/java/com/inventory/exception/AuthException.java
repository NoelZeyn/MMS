package main.java.com.inventory.exception;

public class AuthException extends RuntimeException {

    public AuthException(String message) {
        super(message);
    }
}