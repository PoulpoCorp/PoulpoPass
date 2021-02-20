package fr.poulpocorp.poulpopass.app.model;

import java.util.EventListener;

public interface PasswordManagerListener extends EventListener {

    void passwordCreated(PasswordModel model);

    void passwordRemoved(PasswordModel model);

    void categoryCreated(CategoryModel model);

    void categoryRemoved(CategoryModel model);
}