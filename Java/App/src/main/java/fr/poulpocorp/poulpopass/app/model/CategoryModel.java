package fr.poulpocorp.poulpopass.app.model;

import fr.poulpocorp.poulpopass.core.Category;
import fr.poulpocorp.poulpopass.core.Password;

public class CategoryModel extends Model {

    public static final String NAME_PROPERTY = "Name";

    private Category category;

    public CategoryModel(Category category) {
        this.category = category;
    }

    public void setName(String name) {
        String old = category.getName();

        if (category.setName(name)) {
            firePropertyChange(NAME_PROPERTY, old, name);
        }
    }

    public String getName() {
        return category.getName();
    }

    public void associateWith(Password password) {
        if (category.associateWith(password)) {
            fireListener(AssociationListener.class, (l) -> l.onAssociation(password, category));
        }
    }

    public void dissociateWith(Password password) {
        if (category.dissociateWith(password)) {
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

    Category getCategory() {
        return category;
    }
}