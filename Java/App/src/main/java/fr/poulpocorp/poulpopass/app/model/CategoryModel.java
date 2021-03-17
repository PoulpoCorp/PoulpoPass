package fr.poulpocorp.poulpopass.app.model;

import fr.poulpocorp.poulpopass.core.Category;
import fr.poulpocorp.poulpopass.core.Password;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static fr.poulpocorp.poulpopass.app.model.PasswordEvent.ASSOCIATION;
import static fr.poulpocorp.poulpopass.app.model.PasswordEvent.NAME;

public class CategoryModel extends Model {

    private final PasswordManagerModel manager;
    private final Category category;

    private final List<PasswordModel> passwords;

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
            if (isEditing) {
                type |= NAME;
            } else {
                CategoryEvent event = new CategoryEvent(this, NAME);

                fireListener(CategoryEditedListener.class, (l) -> l.nameChanged(event));
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
                CategoryEvent event = new CategoryEvent(this, ASSOCIATION);

                fireListener(CategoryEditedListener.class, (l) -> l.associationsChanged(event));
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
            CategoryEvent event = new CategoryEvent(this, ASSOCIATION);

            fireListener(CategoryEditedListener.class, (l) -> l.associationsChanged(event));
        }

    }

    public boolean dissociateWith(PasswordModel password) {
        if (category.dissociateWith(password.getPasswordInstance())) {
            passwords.remove(password);
            password.notifyAssociation(this);

            if (isEditing) {
                type |= ASSOCIATION;
            } else {
                CategoryEvent event = new CategoryEvent(this, ASSOCIATION);

                fireListener(CategoryEditedListener.class, (l) -> l.associationsChanged(event));
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
            CategoryEvent event = new CategoryEvent(this, ASSOCIATION);

            fireListener(CategoryEditedListener.class, (l) -> l.associationsChanged(event));
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
            if (type > 0) {
                CategoryEvent event = new CategoryEvent(this, type);

                fireListener(CategoryEditedListener.class, (l) -> l.categoryEdited(event));

                return true;
            }

            type = 0;
            isEditing = false;
        }

        return false;
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