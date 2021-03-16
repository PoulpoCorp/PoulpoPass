package fr.poulpocorp.poulpopass.app.model;

import fr.poulpocorp.poulpopass.core.Category;
import fr.poulpocorp.poulpopass.core.IPassword;
import fr.poulpocorp.poulpopass.core.IPasswordManager;
import fr.poulpocorp.poulpopass.core.Password;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static fr.poulpocorp.poulpopass.app.model.PasswordEvent.*;

public class PasswordModel extends Model implements IPassword {

    private Password password;

    private boolean isEditing = false;
    private int type = 0;

    public PasswordModel(Password password) {
        this.password = password;
    }

    @Override
    public boolean setName(String name) {
        if (password.setName(name)) {
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

    @Override
    public String getName() {
        return password.getName();
    }

    @Override
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

    @Override
    public char[] getPassword() {
        return password.getPassword();
    }

    @Override
    public boolean associateWith(Category category) {
        if (password.associateWith(category)) {
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

    @Override
    public boolean dissociateWith(Category category) {
        if (password.dissociateWith(category)) {
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

    @Override
    public Set<Category> getCategories() {
        return password.getCategories();
    }

    @Override
    public int getNumberOfCategories() {
        return password.getNumberOfCategories();
    }

    @Override
    public void addURL(String url) {
        password.addURL(url);

        if (isEditing) {
            type |= URLS;
        } else {
            PasswordEvent event = new PasswordEvent(password, URLS);

            fireListener(PasswordEditedListener.class, (l) -> l.urlsChanged(event));
        }
    }

    @Override
    public void addURL(int index, String url) {
        password.addURL(index, url);

        if (isEditing) {
            type |= URLS;
        } else {
            PasswordEvent event = new PasswordEvent(password, URLS);

            fireListener(PasswordEditedListener.class, (l) -> l.urlsChanged(event));
        }
    }

    @Override
    public void setURL(int index, String url) {
        password.setURL(index, url);

        if (isEditing) {
            type |= URLS;
        } else {
            PasswordEvent event = new PasswordEvent(password, URLS);

            fireListener(PasswordEditedListener.class, (l) -> l.urlsChanged(event));
        }
    }

    @Override
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

    @Override
    public void removeURL(int index) {
        password.removeURL(index);

        if (isEditing) {
            type |= URLS;
        } else {
            PasswordEvent event = new PasswordEvent(password, URLS);

            fireListener(PasswordEditedListener.class, (l) -> l.urlsChanged(event));
        }
    }

    @Override
    public void removeAllURL() {
        password.removeAllURL();

        if (isEditing) {
            type |= URLS;
        } else {
            PasswordEvent event = new PasswordEvent(password, URLS);

            fireListener(PasswordEditedListener.class, (l) -> l.urlsChanged(event));
        }
    }

    @Override
    public void addAll(Collection<? extends String> c) {
        password.addAll(c);

        if (isEditing) {
            type |= URLS;
        } else {
            PasswordEvent event = new PasswordEvent(password, URLS);

            fireListener(PasswordEditedListener.class, (l) -> l.urlsChanged(event));
        }
    }

    @Override
    public void setURLs(String[] urls) {
        password.setURLs(urls);

        if (isEditing) {
            type |= URLS;
        } else {
            PasswordEvent event = new PasswordEvent(password, URLS);

            fireListener(PasswordEditedListener.class, (l) -> l.urlsChanged(event));
        }
    }

    @Override
    public void setURLs(List<String> urls) {
        password.setURLs(urls);

        if (isEditing) {
            type |= URLS;
        } else {
            PasswordEvent event = new PasswordEvent(password, URLS);

            fireListener(PasswordEditedListener.class, (l) -> l.urlsChanged(event));
        }
    }

    @Override
    public List<String> getURLs() {
        return password.getURLs();
    }

    @Override
    public int getNumberOfURL() {
        return password.getNumberOfURL();
    }

    public IPasswordManager getPasswordManager() {
        return password.getPasswordManager();
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