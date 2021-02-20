package fr.poulpocorp.poulpopass.app.model;

import fr.poulpocorp.poulpopass.core.Category;
import fr.poulpocorp.poulpopass.core.Password;

import java.util.EventListener;

public interface AssociationListener extends EventListener {

    void onAssociation(Password password, Category category);

    void onDissociation(Password password, Category category);
}