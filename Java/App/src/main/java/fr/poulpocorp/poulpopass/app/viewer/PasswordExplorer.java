package fr.poulpocorp.poulpopass.app.viewer;

import fr.poulpocorp.poulpopass.core.PasswordManager;

import javax.swing.*;
import java.awt.*;

public class PasswordExplorer extends JPanel {

    private PasswordManager manager;

    public PasswordExplorer(PasswordManager manager) {
        this.manager = manager;

        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());

        add(new PasswordViewer(this, manager.getPasswords().get(0)));
    }
}