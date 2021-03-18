package fr.poulpocorp.poulpopass.app.model;

import fr.poulpocorp.poulpopass.app.viewer.PasswordExplorer;
import fr.poulpocorp.poulpopass.core.Category;
import fr.poulpocorp.poulpopass.core.Password;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static fr.poulpocorp.poulpopass.app.model.CategoryEvent.*;

public class CategoryModel extends Model {

    private final PasswordManagerModel manager;
    private final Category category;

    private final List<PasswordModel> passwords;

    private ActionListener highlightListener;

    private boolean isEditing = false;
    private int type = 0;

    public CategoryModel(PasswordManagerModel manager, Category category) {
        this.manager = manager;
        this.category = category;

        passwords = new ArrayList<>();
    }

    void createAssociations() {
        for (Password password : category.getPasswords()) {
            passwords.add(manager.get(password));
        }
    }

    public boolean setName(String name) {
        if (category.setName(name)) {
            PasswordEvent e = new PasswordEvent(this);

            for (PasswordModel model : passwords) {
                model.fireAssociationsNameChanged();
            }

            if (isEditing) {
                type |= NAME;
            } else {
                fireNameChanged();
            }

            return true;
        }

        return false;
    }

    public String getName() {
        return category.getName();
    }

    public boolean associateWith(PasswordModel password) {
        if (category.associateWith(password.getPasswordInstance())) {
            passwords.add(password);
            password.notifyAssociation(this);

            if (isEditing) {
                type |= ASSOCIATION;
            } else {
                fireAssociationsChanged();
            }

            return true;
        }

        return false;
    }

    void notifyAssociation(PasswordModel password) {
        passwords.add(password);

        if (isEditing) {
            type |= ASSOCIATION;
        } else {
            fireAssociationsChanged();
        }

    }

    public boolean dissociateWith(PasswordModel password) {
        if (category.dissociateWith(password.getPasswordInstance())) {
            passwords.remove(password);
            password.notifyDissociation(this);

            if (isEditing) {
                type |= ASSOCIATION;
            } else {
                fireAssociationsChanged();
            }

            return true;
        }

        return false;
    }

    void notifyDissociation(PasswordModel password) {
        passwords.remove(password);

        if (isEditing) {
            type |= ASSOCIATION;
        } else {
            fireAssociationsChanged();
        }
    }

    public List<PasswordModel> getPasswords() {
        return Collections.unmodifiableList(passwords);
    }

    public int getNumberOfPasswords() {
        return category.getNumberOfPasswords();
    }

    public PasswordManagerModel getPasswordManager() {
        return manager;
    }

    Category getCategoryInstance() {
        return category;
    }

    public void edit() {
        isEditing = true;
    }

    public boolean finishEdit() {
        if (isEditing) {
            isEditing = false;

            int old = type;
            type = 0;

            if (old > 0) {
                fireCategoryEdited(old);

                return true;
            }
        }

        return false;
    }

    protected void fireCategoryEdited(int type) {
        CategoryEditedListener[] listeners = getCategoryEditedListeners();
        if (listeners.length == 0) {
            return;
        }

        CategoryEvent event = new CategoryEvent(this, type);
        for (CategoryEditedListener listener : listeners) {
            listener.categoryEdited(event);
        }
    }

    protected void fireNameChanged() {
        CategoryEditedListener[] listeners = getCategoryEditedListeners();
        if (listeners.length == 0) {
            return;
        }

        CategoryEvent event = new CategoryEvent(this, NAME);
        for (CategoryEditedListener listener : listeners) {
            listener.nameChanged(event);
        }
    }

    protected void fireAssociationsChanged() {
        CategoryEditedListener[] listeners = getCategoryEditedListeners();
        if (listeners.length == 0) {
            return;
        }

        CategoryEvent event = new CategoryEvent(this, ASSOCIATION);
        for (CategoryEditedListener listener : listeners) {
            listener.associationsChanged(event);
        }
    }

    /**
     * Used by the {@link PasswordModel} class
     * @see CategoryEditedListener#associationNameChanged(CategoryEvent) for explanation
     */
    protected void fireAssociationsNameChanged() {
        CategoryEditedListener[] listeners = getCategoryEditedListeners();
        if (listeners.length == 0) {
            return;
        }

        CategoryEvent event = new CategoryEvent(this, ASSOCIATION_NAME);
        for (CategoryEditedListener listener : listeners) {
            listener.associationNameChanged(event);
        }
    }

    public ActionListener getOrCreateHighlightListener(PasswordExplorer explorer) {
        if (highlightListener == null) {
            highlightListener = e -> explorer.highlightCategory(this);
        }

        return highlightListener;
    }

    public void addCategoryEditedListener(CategoryEditedListener listener) {
        listenerList.add(CategoryEditedListener.class, listener);
    }

    public void removeCategoryEditedListener(CategoryEditedListener listener) {
        listenerList.remove(CategoryEditedListener.class, listener);
    }

    public CategoryEditedListener[] getCategoryEditedListeners() {
        return listenerList.getListeners(CategoryEditedListener.class);
    }
}