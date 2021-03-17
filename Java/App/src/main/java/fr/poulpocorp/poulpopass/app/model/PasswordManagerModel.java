package fr.poulpocorp.poulpopass.app.model;

import fr.poulpocorp.poulpopass.core.Category;
import fr.poulpocorp.poulpopass.core.Password;
import fr.poulpocorp.poulpopass.core.PasswordManager;

import java.io.IOException;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.util.Collection;
import java.util.LinkedHashMap;

public class PasswordManagerModel extends Model {

    private final PasswordManager manager;

    private final LinkedHashMap<Password, PasswordModel> passwords;
    private final LinkedHashMap<Category, CategoryModel> categories;

    public PasswordManagerModel(PasswordManager manager) {
        this.manager = manager;

        passwords = new LinkedHashMap<>();
        categories = new LinkedHashMap<>();

        init();
    }

    private void init() {
        for (Password password : manager.getPasswords()) {
            passwords.put(password, new PasswordModel(this, password));
        }

        for (Category category : manager.getCategories()) {
            categories.put(category, new CategoryModel(this, category));
        }

        passwords.values().forEach(PasswordModel::createAssociations);
        categories.values().forEach(CategoryModel::createAssociations);
    }

    /**
     * Package-private methods for initializing {@link PasswordModel} and {@link CategoryModel}
     */
    CategoryModel get(Category category) {
        CategoryModel model = categories.get(category);

        if (model == null) {
            model = new CategoryModel(this, category);

            categories.put(category, model);
        }

        return model;
    }

    PasswordModel get(Password password) {
        PasswordModel model = passwords.get(password);

        if (model == null) {
            model = new PasswordModel(this, password);

            passwords.put(password, model);
        }

        return model;
    }

    public CategoryModel getOrCreateCategory(String name) {
        int oldSize = manager.getNumberOfCategories();
        Category category = manager.getOrCreateCategory(name);
        int newSize = manager.getNumberOfCategories();

        if (oldSize != newSize) {
            CategoryModel model = new CategoryModel(this, category);

            categories.put(category, model);

            fireListener(PasswordManagerListener.class, (l) -> l.categoryCreated(this, model));

            return model;
        } else {
            return categories.get(category);
        }
    }

    public PasswordModel getOrCreatePassword(String name, char[] password) {
        int oldSize = manager.getNumberOfPasswords();
        Password pass = manager.getOrCreatePassword(name, password);
        int newSize = manager.getNumberOfCategories();

        if (oldSize != newSize) {
            PasswordModel model = new PasswordModel(this, pass);

            passwords.put(pass, model);

            fireListener(PasswordManagerListener.class, (l) -> l.passwordCreated(this, model));

            return model;
        } else {
            return passwords.get(pass);
        }
    }

    public CategoryModel getCategoryIfExists(String name) {
        return categories.values()
                .stream()
                .filter((p) -> p.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public PasswordModel getPasswordIfExists(String name) {
        return passwords.values()
                .stream()
                .filter((p) -> p.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public boolean containsPasswordWithName(String name) {
        return manager.containsPasswordWithName(name);
    }

    public boolean containsCategoryWithName(String name) {
        return manager.containsCategoryWithName(name);
    }

    public boolean removePassword(PasswordModel model) {
        Password password = model.getPasswordInstance();

        if (manager.removePassword(password)) {
            passwords.remove(password, model);

            fireListener(PasswordManagerListener.class, (l) -> l.passwordRemoved(this, model));

            return true;
        }

        return false;
    }

    public boolean removeCategory(CategoryModel model) {
        Category category = model.getCategoryInstance();

        if (manager.removeCategory(category)) {
            categories.remove(category, model);

            fireListener(PasswordManagerListener.class, (l) -> l.categoryRemoved(this, model));

            return true;
        }

        return false;
    }

    public int getNumberOfCategories() {
        return manager.getNumberOfCategories();
    }

    public int getNumberOfPasswords() {
        return manager.getNumberOfPasswords();
    }

    public Collection<CategoryModel> getCategories() {
        return categories.values();
    }

    public Collection<PasswordModel> getPasswords() {
        return passwords.values();
    }

    // TODO: make this methods
    public char[] getMasterPassword() {
        throw new UnsupportedOperationException();
    }

    public boolean setMasterPassword(char[] masterPassword) {
        throw new UnsupportedOperationException();
    }

    public Path getPath() {
        throw new UnsupportedOperationException();
    }

    public void setPath(Path path) {
        throw new UnsupportedOperationException();
    }

    public void save() throws IOException, InvalidKeyException {
        throw new UnsupportedOperationException();
    }

    public void addPasswordManagerListener(PasswordManagerListener listener) {
        listenerList.add(PasswordManagerListener.class, listener);
    }

    public void removePasswordManagerListener(PasswordManagerListener listener) {
        listenerList.remove(PasswordManagerListener.class, listener);
    }

    public PasswordManagerListener[] getPasswordManagerListeners() {
        return listenerList.getListeners(PasswordManagerListener.class);
    }
}