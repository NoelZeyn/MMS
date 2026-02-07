package main.java.com.inventory.app;

import javax.swing.SwingUtilities;

import main.java.com.inventory.pages.LoginFrame;
import main.java.com.inventory.pages.UIConfig;

public class InventoryApp {
    public static void main(String[] args) {
        UIConfig.setup();

        SwingUtilities.invokeLater(() -> {
            LoginFrame login = new LoginFrame();
            login.setVisible(true);
        });
    }
}