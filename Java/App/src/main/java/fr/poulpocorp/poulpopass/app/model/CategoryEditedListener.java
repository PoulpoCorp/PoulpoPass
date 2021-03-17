package fr.poulpocorp.poulpopass.app.model;

import java.util.EventListener;

import static fr.poulpocorp.poulpopass.app.model.CategoryEvent.ASSOCIATION;
import static fr.poulpocorp.poulpopass.app.model.CategoryEvent.NAME;

public interface CategoryEditedListener extends EventListener {

    default void categoryEdited(CategoryEvent event) {
        int type = event.getType();

        if ((type & NAME) != 0) {
            nameChanged(event);
        }

        if ((type & ASSOCIATION) != 0) {
            associationsChanged(event);
        }
    }

    void nameChanged(CategoryEvent event);

    void associationsChanged(CategoryEvent event);
}