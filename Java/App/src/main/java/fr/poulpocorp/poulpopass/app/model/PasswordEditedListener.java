package fr.poulpocorp.poulpopass.app.model;

import java.util.EventListener;

import static fr.poulpocorp.poulpopass.app.model.CategoryEvent.ASSOCIATION_NAME;
import static fr.poulpocorp.poulpopass.app.model.PasswordEvent.*;

public interface PasswordEditedListener extends EventListener {

    default void passwordEdited(PasswordEvent event) {
        int type = event.getType();

        if ((type & NAME) != 0) {
            nameChanged(event);
        }
        if ((type & PASSWORD) != 0) {
            passwordChanged(event);
        }
        if ((type & URLS) != 0) {
            urlsChanged(event);
        }
        if ((type & ASSOCIATION) != 0) {
            associationsChanged(event);
        }
        if (((type) & ASSOCIATION_NAME) != 0) {
            associationNameChanged(event);
        }
    }

    void nameChanged(PasswordEvent event);

    void passwordChanged(PasswordEvent event);

    void urlsChanged(PasswordEvent event);

    void associationsChanged(PasswordEvent event);

    /**
     * Invoked when a category associate with a password changes his name.
     * Therefore, this is the {@link PasswordModel} class which call
     * this method
     */
    void associationNameChanged(PasswordEvent event);
}
