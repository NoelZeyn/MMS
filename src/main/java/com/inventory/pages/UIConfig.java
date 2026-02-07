package main.java.com.inventory.pages;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import java.awt.*;

public class UIConfig {
    // Palet Warna Modern (Indigo & Slate)
    public static final Color PRIMARY = new Color(79, 70, 229);
    public static final Color BG_LIGHT = new Color(248, 250, 252);
    public static final Color TEXT_DARK = new Color(15, 23, 42);

    public static void setup() {
        try {
            // Memasang FlatLaf
            UIManager.setLookAndFeel(new FlatLightLaf());
            
            // Global Smoothness (Kelembutan Komponen)
            UIManager.put("Button.arc", 16);
            UIManager.put("Component.arc", 12);
            UIManager.put("TextComponent.arc", 12);
            UIManager.put("Component.focusWidth", 1);
            UIManager.put("ScrollBar.thumbArc", 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}