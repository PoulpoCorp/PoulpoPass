package fr.poulpocorp.poulpopass.app.model;

import java.util.EventListener;

import static fr.poulpocorp.poulpopass.app.model.CategoryEvent.*;

public interface CategoryEditedListener extends EventListener {

    default void categoryEdited(CategoryEvent event) {
        int type = event.getType();

        if ((type & NAME) != 0) {
            nameChanged(event);
        }

        if ((type & ASSOCIATION) != 0) {
            associationsChanged(event);
        }

        if (((type) & ASSOCIATION_NAME) != 0) {
            associationNameChanged(event);
        }
    }

    void nameChanged(CategoryEvent event);

    void associationsChanged(CategoryEvent event);

    /**
     * Invoked when a password associate with a category changes his name.
     * Therefore, this is the {@link PasswordModel} class which call
     * this method
     */
    void associationNameChanged(CategoryEvent event);
}