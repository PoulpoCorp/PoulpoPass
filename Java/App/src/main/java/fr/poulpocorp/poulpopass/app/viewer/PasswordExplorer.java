package fr.poulpocorp.poulpopass.app.viewer;

import fr.poulpocorp.poulpopass.core.Category;
import fr.poulpocorp.poulpopass.core.Password;
import fr.poulpocorp.poulpopass.core.PasswordManager;

import javax.swing.*;

public class PasswordExplorer extends JPanel {

    private PasswordManager manager;

    public PasswordExplorer(PasswordManager manager) {
        this.manager = manager;

        initComponents();
    }

    private void initComponents() {
        setLayout(new PasswordExplorerLayout());

        for (Category category : manager.getCategories()) {
            add(new CategoryViewer(this, category));
        }

        for (Password password : manager.getPasswords()) {
            add(new PasswordViewer(this, password));
        }
    }
}