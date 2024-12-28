package com.texteditor.ui;

import javafx.scene.paint.Color;

public class Constants {
    public static Color BACKGROUND_COLOR = Color.web("252D38");
    public static Color FOOTER_COLOR = Color.web("14191F");
    public static Color FONT_WHITE = Color.WHITE;

    public static String getCssColor(Color color) {
        return String.format("#%02X%02X%02X", (int)(color.getRed() * 255), (int)(color.getGreen() * 255), (int)(color.getBlue() * 255));
    }
}
