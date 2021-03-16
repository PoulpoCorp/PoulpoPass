package fr.poulpocorp.poulpopass.app.viewer;

import fr.poulpocorp.poulpopass.app.layout.VerticalConstraint;
import fr.poulpocorp.poulpopass.app.tag.JTagComponent;
import fr.poulpocorp.poulpopass.app.tag.Tag;
import fr.poulpocorp.poulpopass.app.utils.Icons;
import fr.poulpocorp.poulpopass.app.utils.Utils;
import fr.poulpocorp.poulpopass.core.Category;
import fr.poulpocorp.poulpopass.core.Password;

import javax.swing.*;
import java.awt.*;

public class CategoryViewer extends AbstractViewer {

    private Category category;

    private JLabel nameLabel;
    private JTagComponent passwords;

    public CategoryViewer(PasswordExplorer explorer, Category category) {
        super(explorer);
        this.category = category;

        initComponents();
    }

    @Override
    protected Component getTopComponent() {
        if (nameLabel == null) {
            nameLabel = new JLabel(category.getName());
            nameLabel.setForeground(Utils.applyThemeColorFunction(nameLabel.getForeground()));
        }

        return nameLabel;
    }

    @Override
    protected void initFields() {
        VerticalConstraint constraint = new VerticalConstraint();
        constraint.xAlignment = 0;
        constraint.fillXAxis = true;

        JSeparator separator = new JSeparator();
        add(separator, constraint);
        constraint.fillXAxis = false;

        JLabel passwordsLabel = new JLabel("Passwords");
        Color fg = passwordsLabel.getForeground();
        passwordsLabel.setForeground(Utils.applyThemeColorFunction(fg));

        passwords = new JTagComponent();
        passwords.setEditable(false);

        for (Password password : category.getPasswords()) {
            Tag tag = passwords.addTagToList(password.getName());

            tag.addActionListener((e) -> explorer.highlightPassword(password));
        }

        add(passwordsLabel, constraint);
        constraint.endComponent = true;
        add(passwords, constraint);
    }

    protected void updateViewer() {
        Password[] pass = category.getPasswords().toArray(new Password[0]);;

        Tag[] selected = passwords.getSelectedTags();

        int i;
        for (i = 0; i < pass.length; i++) {
            Password password = pass[i];

            // modify
            if (i < selected.length) {
                Tag tag = passwords.getTagInListAt(i);

                if (!tag.getName().equals(password.getName())) {
                    tag.setName(password.getName());
                }
            } else {
                // add
                Tag tag = passwords.addTagToList(password.getName());

                tag.addActionListener((e) -> explorer.highlightPassword(password));
            }
        }

        // remove
        if (i < selected.length) {
            for (; i < selected.length; i++) {
                passwords.removeTagInListAt(i);
            }
        }

        revalidate();
        repaint();
    }

    @Override
    protected Icon getIcon() {
        return Icons.CATEGORY;
    }
}