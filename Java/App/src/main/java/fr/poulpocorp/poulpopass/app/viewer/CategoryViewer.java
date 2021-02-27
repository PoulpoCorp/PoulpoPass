package fr.poulpocorp.poulpopass.app.viewer;

import fr.poulpocorp.poulpopass.app.layout.VerticalConstraint;
import fr.poulpocorp.poulpopass.app.text.JLabelLink;
import fr.poulpocorp.poulpopass.app.utils.Icons;
import fr.poulpocorp.poulpopass.core.Category;
import fr.poulpocorp.poulpopass.core.Password;

import javax.swing.*;
import java.awt.*;

public class CategoryViewer extends AbstractViewer<Category> {

    private JLabel nameLabel;

    public CategoryViewer(PasswordExplorer explorer, Category category) {
        super(explorer, category);
    }

    @Override
    protected Component getTopComponent() {
        if (nameLabel == null) {
            nameLabel = new JLabel(element.getName());
            nameLabel.setForeground(nameLabel.getForeground().darker());
        }

        return nameLabel;
    }

    @Override
    protected void initFields() {
        VerticalConstraint constraint = new VerticalConstraint();
        constraint.xAlignment = 0;
        constraint.fillXAxis = true;

        add(new JSeparator(), constraint);
        constraint.fillXAxis = false;

        for (Password password : element.getPasswords()) {
            JLabelLink link = new JLabelLink(password.getName());
            link.addActionListener((l) -> explorer.highlightPassword(password));

            add(link, constraint);
        }
    }

    @Override
    protected Icon getIcon() {
        return Icons.CATEGORY;
    }
}