package fr.poulpocorp.poulpopass.app.model;

import java.util.EventListener;

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
    }

    void nameChanged(PasswordEvent event);

    void passwordChanged(PasswordEvent event);

    void urlsChanged(PasswordEvent event);

    void associationsChanged(PasswordEvent event);
}
