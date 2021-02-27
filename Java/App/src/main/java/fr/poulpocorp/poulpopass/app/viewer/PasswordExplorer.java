package fr.poulpocorp.poulpopass.app.viewer;

import fr.poulpocorp.poulpopass.core.Category;
import fr.poulpocorp.poulpopass.core.Password;
import fr.poulpocorp.poulpopass.core.PasswordManager;

import javax.swing.*;
import java.util.HashMap;

public class PasswordExplorer extends JPanel {

    private PasswordManager manager;

    private final HashMap<Password, PasswordViewer> passwordMap = new HashMap<>();
    private final HashMap<Category, CategoryViewer> categoryMap = new HashMap<>();

    public PasswordExplorer(PasswordManager manager) {
        this.manager = manager;

        initComponents();
    }

    private void initComponents() {
        setLayout(new PasswordExplorerLayout());

        for (Category category : manager.getCategories()) {
            CategoryViewer viewer = new CategoryViewer(this, category);

            add(viewer);
            categoryMap.put(category, viewer);
        }

        for (Password password : manager.getPasswords()) {
            PasswordViewer viewer = new PasswordViewer(this, password);

            add(viewer);
            passwordMap.put(password, viewer);
        }
    }

    public void highlightPassword(Password password) {
        PasswordViewer comp = passwordMap.get(password);

        ViewerBorder.startAnimation(comp, comp.getViewerBorder());
    }
}