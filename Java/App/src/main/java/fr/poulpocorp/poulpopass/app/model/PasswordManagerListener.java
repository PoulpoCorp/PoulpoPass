package fr.poulpocorp.poulpopass.app.model;

import java.util.EventListener;

public interface PasswordManagerListener extends EventListener {

    void passwordCreated(PasswordManagerModel model, PasswordModel password);

    void passwordRemoved(PasswordManagerModel model, PasswordModel password);

    void categoryCreated(PasswordManagerModel model, CategoryModel category);

    void categoryRemoved(PasswordManagerModel model, CategoryModel category);
}