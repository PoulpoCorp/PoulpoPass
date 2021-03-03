package fr.poulpocorp.poulpopass.app;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLaf;

import java.awt.*;

public class Main {

    public static void main(String[] args) {
        FlatLaf.registerCustomDefaultsSource("theme");

        EventQueue.invokeLater(() -> {
            FlatDarculaLaf.install();

            App app = new App();
            app.setVisible(true);
        });
    }
}