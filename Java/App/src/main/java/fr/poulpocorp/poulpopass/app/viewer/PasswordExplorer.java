package fr.poulpocorp.poulpopass.app.viewer;

import fr.poulpocorp.poulpopass.app.model.PasswordEditedAdapter;
import fr.poulpocorp.poulpopass.app.model.PasswordEditedListener;
import fr.poulpocorp.poulpopass.app.model.PasswordEvent;
import fr.poulpocorp.poulpopass.app.model.PasswordModel;
import fr.poulpocorp.poulpopass.core.Category;
import fr.poulpocorp.poulpopass.core.Password;
import fr.poulpocorp.poulpopass.core.PasswordManager;

import javax.swing.*;
import java.util.HashMap;

public class PasswordExplorer extends JPanel {

    private PasswordManager manager;

    private final HashMap<PasswordModel, PasswordViewer> passwordMap = new HashMap<>();
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
            PasswordModel model = new PasswordModel(password);

            PasswordViewer viewer = new PasswordViewer(this, model);

            add(viewer);
            passwordMap.put(model, viewer);

            model.addPasswordEditedListener(new PasswordEditedAdapter() {
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