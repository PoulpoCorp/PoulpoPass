package fr.poulpocorp.poulpopass.app.model;

import fr.poulpocorp.poulpopass.core.Category;
import fr.poulpocorp.poulpopass.core.Password;

public class PasswordModel extends Model {

    public static final String NAME_PROPERTY = "Name";

    private Password password;

    public PasswordModel(Password password) {
        this.password = password;
    }

    public void setName(String name) {
        String old = password.getName();

        if (password.setName(name)) {
            firePropertyChange(NAME_PROPERTY, old, name);
        }
    }

    public String getName() {
        return password.getName();
    }

    public void associateWith(Category category) {
        if (password.associateWith(category)) {
            fireListener(AssociationListener.class, (l) -> l.onAssociation(password, category));
        }
    }

    public void dissociateWith(Category category) {
        if (password.dissociateWith(category)) {
            fireListener(AssociationListener.class, (l) -> l.onAssociation(password, category));
        }
    }

    public void addAssociationListener(AssociationListener listener) {
        listenerList.add(AssociationListener.class, listener);
    }

    public void removeAssociationListener(AssociationListener listener) {
        listenerList.remove(AssociationListener.class, listener);
    }

    public AssociationListener[] getAssociationListeners() {
        return listenerList.getListeners(AssociationListener.class);
    }

    Password getPassword() {
        return password;
    }
}