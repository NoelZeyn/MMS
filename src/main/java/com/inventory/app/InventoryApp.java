package main.java.com.inventory.app;

import main.java.com.inventory.menu.MainMenu;
import main.java.com.inventory.model.User;
import main.java.com.inventory.security.InputSanitizer;
import main.java.com.inventory.service.AuthService;

import java.util.Scanner;

public class InventoryApp {

    public static void main(String[] args) {

        try (Scanner sc = new Scanner(System.in)) {

            AuthService authService = new AuthService();

            System.out.print("Username: ");
            String username = InputSanitizer.sanitize(sc.nextLine());

            System.out.print("Password: ");
            String password = sc.nextLine();

            User user = authService.login(username, password);

            MainMenu mainMenu = new MainMenu(sc, user);
            mainMenu.start();

        } catch (Exception e) {
            System.err.println("❌ ERROR: " + e.getMessage());
        }
    }
}
