package fr.poulpocorp.poulpopass.app.model;

import fr.poulpocorp.poulpopass.core.Category;
import fr.poulpocorp.poulpopass.core.IPasswordManager;
import fr.poulpocorp.poulpopass.core.Password;

import java.util.ArrayList;
import java.util.List;

public class PasswordManagerModel extends Model {

    public static final String MASTER_PASSWORD_PROPERTY = "MasterPassword";

    private IPasswordManager manager;
    private final List<PasswordModel> passwords;
    private final List<CategoryModel> categories;

    public PasswordManagerModel(IPasswordManager manager) {
        this.manager = manager;

        passwords = new ArrayList<>();
        categories = new ArrayList<>();
    }

    public void createPassword(String name, char[] password) {
        if (manager.containsPasswordWithName(name)) {
            return;
        }

        Password pass = manager.getOrCreatePassword(name, password);
        PasswordModel model = new PasswordModel(pass);

        passwords.add(model);

        fireListener(PasswordManagerListener.class, (l) -> l.passwordCreated(model));
    }

    public void removePassword(PasswordModel model) {
        if (manager.removePassword(model.getPassword())) {
            passwords.remove(model);

            fireListener(PasswordManagerListener.class, (l) -> l.passwordRemoved(model));
        }
    }

    public void createCategory(String name) {
        if (manager.containsCategoryWithName(name)) {
            return;
        }

        Category category = manager.getOrCreateCategory(name);
        CategoryModel model = new CategoryModel(category);

        categories.add(model);

        fireListener(PasswordManagerListener.class, (l) -> l.categoryCreated(model));
    }

    public void removeCategory(CategoryModel model) {
        if (manager.removeCategory(model.getCategory())) {
            categories.remove(model);

            fireListener(PasswordManagerListener.class, (l) -> l.categoryRemoved(model));
        }
    }

    public int getNumberOfPasswords() {
        return manager.getNumberOfPasswords();
    }

    public int getNumberOfCategories() {
        return manager.getNumberOfCategories();
    }

    public void setMasterPassword(char[] masterPassword) {
        if (masterPassword != null && manager.getMasterPassword() != masterPassword) {
            char[] old = manager.getMasterPassword();

            manager.setMasterPassword(masterPassword);

            firePropertyChange(MASTER_PASSWORD_PROPERTY, old, masterPassword);
        }
    }

    public char[] getMasterPassword() {
        return manager.getMasterPassword();
    }
}