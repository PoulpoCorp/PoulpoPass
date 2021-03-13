package fr.poulpocorp.poulpopass.app.viewer;

import fr.poulpocorp.poulpopass.app.model.PasswordEvent;
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

            viewer.addPasswordEditedListener((event) -> {
                if ((event.getType() & PasswordEvent.ASSOCIATION) != 0) {
                    for (Category category : password.getCategories()) {
                        CategoryViewer categoryViewer = categoryMap.get(category);
                        categoryViewer.updateViewer();
                    }
                }
            });
        }
    }

    public void highlightPassword(Password password) {
        PasswordViewer comp = passwordMap.get(password);

        ViewerBorder.startAnimation(comp, comp.getViewerBorder());
    }

    public void highlightCategory(Category category) {
        CategoryViewer comp = categoryMap.get(category);

        ViewerBorder.startAnimation(comp, comp.getViewerBorder());
    }
}