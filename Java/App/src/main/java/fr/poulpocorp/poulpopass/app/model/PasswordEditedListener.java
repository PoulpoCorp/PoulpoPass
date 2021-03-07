package fr.poulpocorp.poulpopass.app.model;

import java.util.EventListener;

public interface PasswordEditedListener extends EventListener {

    void passwordEdited(PasswordEvent event);
}
