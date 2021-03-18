package fr.poulpocorp.poulpopass.app.model;

import fr.poulpocorp.poulpopass.app.viewer.PasswordExplorer;
import fr.poulpocorp.poulpopass.core.Category;
import fr.poulpocorp.poulpopass.core.Password;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static fr.poulpocorp.poulpopass.app.model.PasswordEvent.*;

public class PasswordModel extends Model {

    private final PasswordManagerModel manager;
    private final Password password;

    private final List<CategoryModel> categories;

    private ActionListener highlightListener;

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
            CategoryEvent e = new CategoryEvent(this);

            for (CategoryModel model : categories) {
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
        return password.getName();
    }

    public boolean setPassword(char[] pass) {
        if (password.setPassword(pass)) {
            if (isEditing) {
                type |= PASSWORD;
            } else {
                firePasswordChanged();
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
                fireAssociationsChanged();
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
            fireAssociationsChanged();
        }
    }

    public boolean dissociateWith(CategoryModel category) {
        if (password.dissociateWith(category.getCategoryInstance())) {
            categories.remove(category);
            category.notifyDissociation(this);

            if (isEditing) {
                type |= ASSOCIATION;
            } else {
                fireAssociationsChanged();
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
            fireAssociationsChanged();
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
            fireUrlsChanged();
        }
    }

    public void addURL(int index, String url) {
        password.addURL(index, url);

        if (isEditing) {
            type |= URLS;
        } else {
            fireUrlsChanged();
        }
    }

    public void setURL(int index, String url) {
        password.setURL(index, url);

        if (isEditing) {
            type |= URLS;
        } else {
            fireUrlsChanged();
        }
    }

    public boolean removeURL(String url) {
        if (password.removeURL(url)) {
            if (isEditing) {
                type |= URLS;
            } else {
                fireUrlsChanged();
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
            fireUrlsChanged();
        }
    }

    public void removeAllURL() {
        password.removeAllURL();

        if (isEditing) {
            type |= URLS;
        } else {
            fireUrlsChanged();
        }
    }

    public void addAll(Collection<? extends String> c) {
        password.addAll(c);

        if (isEditing) {
            type |= URLS;
        } else {
            fireUrlsChanged();
        }
    }

    public void setURLs(String[] urls) {
        password.setURLs(urls);

        if (isEditing) {
            type |= URLS;
        } else {
            fireUrlsChanged();
        }
    }

    public void setURLs(List<String> urls) {
        password.setURLs(urls);

        if (isEditing) {
            type |= URLS;
        } else {
            fireUrlsChanged();
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
            isEditing = false;

            int old = type;
            type = 0;

            if (old > 0) {
                firePasswordEdited(old);

                return true;
            }
        }

        return false;
    }

    protected void firePasswordEdited(int type) {
        PasswordEditedListener[] listeners = getPasswordEditedListeners();
        if (listeners.length == 0) {
            return;
        }

        PasswordEvent event = new PasswordEvent(this, type);
        for (PasswordEditedListener listener : listeners) {
            listener.passwordEdited(event);
        }
    }

    protected void fireNameChanged() {
        PasswordEditedListener[] listeners = getPasswordEditedListeners();
        if (listeners.length == 0) {
            return;
        }

        PasswordEvent event = new PasswordEvent(this, NAME);
        for (PasswordEditedListener listener : listeners) {
            listener.nameChanged(event);
        }
    }

    protected void firePasswordChanged() {
        PasswordEditedListener[] listeners = getPasswordEditedListeners();
        if (listeners.length == 0) {
            return;
        }

        PasswordEvent event = new PasswordEvent(this, PASSWORD);
        for (PasswordEditedListener listener : listeners) {
            listener.passwordChanged(event);
        }
    }

    protected void fireUrlsChanged() {
        PasswordEditedListener[] listeners = getPasswordEditedListeners();
        if (listeners.length == 0) {
            return;
        }

        PasswordEvent event = new PasswordEvent(this, URLS);
        for (PasswordEditedListener listener : listeners) {
            listener.urlsChanged(event);
        }
    }

    protected void fireAssociationsChanged() {
        PasswordEditedListener[] listeners = getPasswordEditedListeners();
        if (listeners.length == 0) {
            return;
        }

        PasswordEvent event = new PasswordEvent(this, ASSOCIATION);
        for (PasswordEditedListener listener : listeners) {
            listener.associationsChanged(event);
        }
    }

    /**
     * Used by the {@link CategoryModel} class
     * @see PasswordEditedListener#associationNameChanged(PasswordEvent) for explanation
     */
    protected void fireAssociationsNameChanged() {
        PasswordEditedListener[] listeners = getPasswordEditedListeners();
        if (listeners.length == 0) {
            return;
        }

        PasswordEvent event = new PasswordEvent(this, ASSOCIATION_NAME);
        for (PasswordEditedListener listener : listeners) {
            listener.associationNameChanged(event);
        }
    }

    public ActionListener getOrCreateHighlightListener(PasswordExplorer explorer) {
        if (highlightListener == null) {
            highlightListener = e -> explorer.highlightPassword(this);
        }

        return highlightListener;
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