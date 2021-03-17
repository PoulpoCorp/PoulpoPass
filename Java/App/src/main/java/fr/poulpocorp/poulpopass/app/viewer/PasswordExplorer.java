package fr.poulpocorp.poulpopass.app.viewer;

import fr.poulpocorp.poulpopass.app.model.*;
import fr.poulpocorp.poulpopass.core.Category;
import fr.poulpocorp.poulpopass.core.Password;
import fr.poulpocorp.poulpopass.core.PasswordManager;

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

            /*model.addPasswordEditedListener(new PasswordEditedAdapter() {
                @Override
                public void passwordEdited(PasswordEvent event) {
                    int type = event.getType();

                    if ((type & PasswordEvent.NAME) != 0 || (type & PasswordEvent.ASSOCIATION) != 0) {
                        for (Category category : password.getCategories()) {
                            CategoryViewer categoryViewer = categoryMap.get(category);
                            categoryViewer.updateViewer();
                        }
                    }
                }

                @Override
                public void nameChanged(PasswordEvent event) {
                    passwordEdited(event);
                }

                @Override
                public void associationsChanged(PasswordEvent event) {
                    passwordEdited(event);
                }
            });*/
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