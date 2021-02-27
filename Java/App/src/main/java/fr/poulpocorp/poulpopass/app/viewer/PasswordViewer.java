package fr.poulpocorp.poulpopass.app.viewer;

import fr.poulpocorp.poulpopass.app.layout.VerticalConstraint;
import fr.poulpocorp.poulpopass.app.text.PPPasswordTextField;
import fr.poulpocorp.poulpopass.app.utils.Icons;
import fr.poulpocorp.poulpopass.app.utils.Utils;
import fr.poulpocorp.poulpopass.core.Password;

import javax.swing.*;
import java.awt.*;

public class PasswordViewer extends AbstractViewer<Password> {

    private JLabel nameLabel; // display name
    private JLabel name;      // display the name of the password
    private PPPasswordTextField passwordField;

    public PasswordViewer(PasswordExplorer explorer, Password password) {
        super(explorer, password);
    }

    @Override
    protected Component getTopComponent() {
        if (nameLabel == null) {
            nameLabel = new JLabel("Name");
            nameLabel.setForeground(nameLabel.getForeground().darker());
        }

        return nameLabel;
    }

    @Override
    protected void initFields() {
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setForeground(passwordLabel.getForeground().darker());

        name = new JLabel(element.getName());
        passwordField = Utils.createPasswordLabel(element.getPassword());

        VerticalConstraint constraint = new VerticalConstraint();
        constraint.xAlignment = 0;

        add(name, constraint);

        constraint.fillXAxis = true;
        add(new JSeparator(), constraint);

        constraint.fillXAxis = false;
        add(passwordLabel, constraint);

        constraint.fillXAxis = true;
        add(passwordField, constraint);
    }

    @Override
    protected Icon getIcon() {
        return Icons.PASSWORD;
    }
}