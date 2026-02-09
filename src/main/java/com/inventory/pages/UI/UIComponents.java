package com.inventory.pages.UI;


import javax.swing.*;

import java.awt.*;

public class UIComponents {
    public static JButton createLinkButton(String text) {
        JButton btn = new JButton(text);
        btn.setForeground(UIConfig.PRIMARY);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMargin(new Insets(0,0,0,0));
        return btn;
    }
}