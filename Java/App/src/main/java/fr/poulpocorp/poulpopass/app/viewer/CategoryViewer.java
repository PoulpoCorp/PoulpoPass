package fr.poulpocorp.poulpopass.app.viewer;

import fr.poulpocorp.poulpopass.app.layout.VerticalConstraint;
import fr.poulpocorp.poulpopass.app.layout.VerticalLayout;
import fr.poulpocorp.poulpopass.app.text.JLabelLink;
import fr.poulpocorp.poulpopass.core.Category;
import fr.poulpocorp.poulpopass.core.Password;

import javax.swing.*;

public class CategoryViewer extends AbstractPasswordViewer  {

    private Category category;

    public CategoryViewer(PasswordExplorer explorer, Category category) {
        super(explorer);

        this.category = category;

        initComponents();
    }

    @Override
    protected void initComponents() {
        setLayout(new VerticalLayout(5, 5));

        JLabel nameLabel = new JLabel(category.getName());
        nameLabel.setForeground(nameLabel.getForeground().darker());

        VerticalConstraint constraint = new VerticalConstraint();
        constraint.xAlignment = 0;

        add(nameLabel, constraint);

        constraint.fillXAxis = true;
        add(new JSeparator(), constraint);
        constraint.fillXAxis = false;

        for (Password password : category.getPasswords()) {
            JLabelLink link = new JLabelLink(password.getName());

            add(link, constraint);
        }
    }
}