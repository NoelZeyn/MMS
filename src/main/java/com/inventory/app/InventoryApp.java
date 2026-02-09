package com.inventory.app;

import javax.swing.SwingUtilities;

import com.inventory.pages.Auth.LoginFrame;
import com.inventory.pages.UI.UIConfig;

public class InventoryApp {
    public static void main(String[] args) {
        UIConfig.setup();

        SwingUtilities.invokeLater(() -> {
            LoginFrame login = new LoginFrame();
            login.setVisible(true);
        });
    }
}