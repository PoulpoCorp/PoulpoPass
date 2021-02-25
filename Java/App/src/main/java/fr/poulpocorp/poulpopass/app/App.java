package fr.poulpocorp.poulpopass.app;

import fr.poulpocorp.poulpopass.app.viewer.PasswordExplorer;
import fr.poulpocorp.poulpopass.core.PasswordManager;

import javax.swing.*;

public class App extends JFrame  {

    public App() {
        super("PoulpoPass");

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initComponents();
        pack();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        PasswordManager manager = new PasswordManager("Hello world".toCharArray());

        for (int i = 0; i < 10; i++) {
            String name = "Password " + i;

            manager.getOrCreatePassword(name, name.toCharArray());
        }

        add(new PasswordExplorer(manager));
    }
}