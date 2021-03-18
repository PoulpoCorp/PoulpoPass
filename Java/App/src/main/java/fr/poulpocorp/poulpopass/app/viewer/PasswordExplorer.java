package fr.poulpocorp.poulpopass.app.viewer;

import fr.poulpocorp.poulpopass.app.model.CategoryModel;
import fr.poulpocorp.poulpopass.app.model.PasswordManagerModel;
import fr.poulpocorp.poulpopass.app.model.PasswordModel;

import javax.swing.*;
import java.util.HashMap;

public class PasswordExplorer extends JPanel {

    private final PasswordManagerModel manager;

    private final HashMap<PasswordModel, PasswordViewer> passwordMap = new HashMap<>();
    private final HashMap<CategoryModel, CategoryViewer> categoryMap = new HashMap<>();

    public PasswordExplorer(PasswordManagerModel manager) {
        this.manager = manager;

        initComponents();
    }

    private void initComponents() {
        setLayout(new PasswordExplorerLayout());

        for (CategoryModel model : manager.getCategories()) {
            CategoryViewer viewer = new CategoryViewer(this, model);

            add(viewer);
            categoryMap.put(model, viewer);
        }

        for (PasswordModel model : manager.getPasswords()) {
            PasswordViewer viewer = new PasswordViewer(this, model);

            add(viewer);
            passwordMap.put(model, viewer);
        }
    }

    public void highlightPassword(PasswordModel password) {
        PasswordViewer comp = passwordMap.get(password);

        ViewerBorder.startAnimation(comp, comp.getViewerBorder());
    }

    public void highlightCategory(CategoryModel category) {
        CategoryViewer comp = categoryMap.get(category);

        ViewerBorder.startAnimation(comp, comp.getViewerBorder());
    }
}