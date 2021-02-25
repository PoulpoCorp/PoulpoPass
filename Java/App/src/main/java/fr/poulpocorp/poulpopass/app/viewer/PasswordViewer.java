package fr.poulpocorp.poulpopass.app.viewer;

import fr.poulpocorp.poulpopass.app.layout.VerticalConstraint;
import fr.poulpocorp.poulpopass.app.layout.VerticalLayout;
import fr.poulpocorp.poulpopass.app.text.PPPasswordTextField;
import fr.poulpocorp.poulpopass.app.utils.Utils;
import fr.poulpocorp.poulpopass.core.Password;

import javax.swing.*;

public class PasswordViewer extends AbstractPasswordViewer {

    private Password password;

    private JLabel name;
    private PPPasswordTextField passwordField;

    public PasswordViewer(PasswordExplorer explorer, Password password) {
        super(explorer);

        this.password = password;

        initComponents();
    }

    @Override
    protected void initComponents() {
        setLayout(new VerticalLayout(5, 5));

        JLabel nameLabel = new JLabel("Name");
        nameLabel.setForeground(nameLabel.getForeground().darker());

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setForeground(passwordLabel.getForeground().darker());

        name = new JLabel(password.getName());
        passwordField = Utils.createPasswordTextField(password.getPassword());

        VerticalConstraint constraint = new VerticalConstraint();
        constraint.xAlignment = 0;

        add(nameLabel, constraint);
        add(name, constraint);
        constraint.fillXAxis = true;
        add(new JSeparator(), constraint);
        constraint.fillXAxis = false;
        add(passwordLabel, constraint);
        constraint.fillXAxis = true;
        add(passwordField, constraint);
    }
}