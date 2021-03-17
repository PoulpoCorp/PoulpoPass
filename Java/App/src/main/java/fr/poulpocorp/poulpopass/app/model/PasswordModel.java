package fr.poulpocorp.poulpopass.app.model;

import fr.poulpocorp.poulpopass.core.Category;
import fr.poulpocorp.poulpopass.core.Password;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static fr.poulpocorp.poulpopass.app.model.PasswordEvent.*;

public class PasswordModel extends Model {

    private final PasswordManagerModel manager;
    private final Password password;

    private final List<CategoryModel> categories;

    private boolean isEditing = false;
    private int type = 0;

    public PasswordModel(PasswordManagerModel manager, Password password) {
        this.manager = manager;
        this.password = password;

        categories = new ArrayList<>();
    }

    void createAssociations() {
        for (Category category : password.getCategories()) {
            categories.add(manager.get(category));
        }
    }

    public boolean setName(String name) {
        if (password.setName(name)) {
            CategoryEvent e = new CategoryEvent(this, CategoryEvent.ASSOCIATION_NAME);

            for (CategoryModel model : categories) {
                model.fireListener(CategoryEditedListener.class, (l) -> l.associationNameChanged(e));
            }

            if (isEditing) {
                type |= NAME;
            } else {
                PasswordEvent event = new PasswordEvent(password, NAME);

                fireListener(PasswordEditedListener.class, (l) -> l.nameChanged(event));
            }

            return true;
        }

        return false;
    }

    public String getName() {
        return password.getName();
    }

    public boolean setPassword(char[] pass) {
        if (password.setPassword(pass)) {
            if (isEditing) {
                type |= PASSWORD;
            } else {
                PasswordEvent event = new PasswordEvent(password, PASSWORD);

                fireListener(PasswordEditedListener.class, (l) -> l.passwordChanged(event));
            }

            return true;
        }

        return false;
    }

    public char[] getPassword() {
        return password.getPassword();
    }

    public boolean associateWith(CategoryModel category) {
        if (password.associateWith(category.getCategoryInstance())) {
            categories.add(category);
            category.notifyAssociation(this);

            if (isEditing) {
                type |= ASSOCIATION;
            } else {
                PasswordEvent event = new PasswordEvent(password, ASSOCIATION);

                fireListener(PasswordEditedListener.class, (l) -> l.associationsChanged(event));
            }

            return true;
        }

        return false;
    }

    void notifyAssociation(CategoryModel category) {
        categories.add(category);

        if (isEditing) {
            type |= ASSOCIATION;
        } else {
            PasswordEvent event = new PasswordEvent(password, ASSOCIATION);

            fireListener(PasswordEditedListener.class, (l) -> l.associationsChanged(event));
        }
    }

    public boolean dissociateWith(CategoryModel category) {
        if (password.dissociateWith(category.getCategoryInstance())) {
            categories.remove(category);
            category.notifyDissociation(this);

            if (isEditing) {
                type |= ASSOCIATION;
            } else {
                PasswordEvent event = new PasswordEvent(password, ASSOCIATION);

                fireListener(PasswordEditedListener.class, (l) -> l.associationsChanged(event));
            }

            return true;
        }

        return false;
    }

    void notifyDissociation(CategoryModel category) {
        categories.remove(category);

        if (isEditing) {
            type |= ASSOCIATION;
        } else {
            PasswordEvent event = new PasswordEvent(password, ASSOCIATION);

            fireListener(PasswordEditedListener.class, (l) -> l.associationsChanged(event));
        }
    }

    public List<CategoryModel> getCategories() {
        return Collections.unmodifiableList(categories);
    }

    public int getNumberOfCategories() {
        return password.getNumberOfCategories();
    }

    public void addURL(String url) {
        password.addURL(url);

        if (isEditing) {
            type |= URLS;
        } else {
            PasswordEvent event = new PasswordEvent(password, URLS);

            fireListener(PasswordEditedListener.class, (l) -> l.urlsChanged(event));
        }
    }

    public void addURL(int index, String url) {
        password.addURL(index, url);

        if (isEditing) {
            type |= URLS;
        } else {
            PasswordEvent event = new PasswordEvent(password, URLS);

            fireListener(PasswordEditedListener.class, (l) -> l.urlsChanged(event));
        }
    }

    public void setURL(int index, String url) {
        password.setURL(index, url);

        if (isEditing) {
            type |= URLS;
        } else {
            PasswordEvent event = new PasswordEvent(password, URLS);

            fireListener(PasswordEditedListener.class, (l) -> l.urlsChanged(event));
        }
    }

    public boolean removeURL(String url) {
        if (password.removeURL(url)) {
            if (isEditing) {
                type |= URLS;
            } else {
                PasswordEvent event = new PasswordEvent(password, URLS);

                fireListener(PasswordEditedListener.class, (l) -> l.urlsChanged(event));
            }

            return true;
        }

        return false;
    }

    public void removeURL(int index) {
        password.removeURL(index);

        if (isEditing) {
            type |= URLS;
        } else {
            PasswordEvent event = new PasswordEvent(password, URLS);

            fireListener(PasswordEditedListener.class, (l) -> l.urlsChanged(event));
        }
    }

    public void removeAllURL() {
        password.removeAllURL();

        if (isEditing) {
            type |= URLS;
        } else {
            PasswordEvent event = new PasswordEvent(password, URLS);

            fireListener(PasswordEditedListener.class, (l) -> l.urlsChanged(event));
        }
    }

    public void addAll(Collection<? extends String> c) {
        password.addAll(c);

        if (isEditing) {
            type |= URLS;
        } else {
            PasswordEvent event = new PasswordEvent(password, URLS);

            fireListener(PasswordEditedListener.class, (l) -> l.urlsChanged(event));
        }
    }

    public void setURLs(String[] urls) {
        password.setURLs(urls);

        if (isEditing) {
            type |= URLS;
        } else {
            PasswordEvent event = new PasswordEvent(password, URLS);

            fireListener(PasswordEditedListener.class, (l) -> l.urlsChanged(event));
        }
    }

    public void setURLs(List<String> urls) {
        password.setURLs(urls);

        if (isEditing) {
            type |= URLS;
        } else {
            PasswordEvent event = new PasswordEvent(password, URLS);

            fireListener(PasswordEditedListener.class, (l) -> l.urlsChanged(event));
        }
    }

    public List<String> getURLs() {
        return password.getURLs();
    }

    public int getNumberOfURL() {
        return password.getNumberOfURL();
    }

    public PasswordManagerModel getPasswordManager() {
        return manager;
    }

    Password getPasswordInstance() {
        return password;
    }

    public void edit() {
        isEditing = true;
    }

    public boolean finishEdit() {
        if (isEditing) {
            if (type > 0) {
                PasswordEvent event = new PasswordEvent(password, type);

                fireListener(PasswordEditedListener.class, (l) -> l.passwordEdited(event));

                return true;
            }

            type = 0;
            isEditing = false;
        }

        return false;
    }

    public void addPasswordEditedListener(PasswordEditedListener listener) {
        listenerList.add(PasswordEditedListener.class, listener);
    }

    public void removePasswordEditedListener(PasswordEditedListener listener) {
        listenerList.remove(PasswordEditedListener.class, listener);
    }

    public PasswordEditedListener[] getPasswordEditedListeners() {
        return listenerList.getListeners(PasswordEditedListener.class);
    }
}