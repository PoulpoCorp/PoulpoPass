package fr.poulpocorp.poulpopass.app.viewer;

import fr.poulpocorp.poulpopass.app.layout.VerticalConstraint;
import fr.poulpocorp.poulpopass.app.model.CategoryEditedListener;
import fr.poulpocorp.poulpopass.app.model.CategoryEvent;
import fr.poulpocorp.poulpopass.app.model.CategoryModel;
import fr.poulpocorp.poulpopass.app.model.PasswordModel;
import fr.poulpocorp.poulpopass.app.tag.JTagComponent;
import fr.poulpocorp.poulpopass.app.tag.Tag;
import fr.poulpocorp.poulpopass.app.utils.Icons;
import fr.poulpocorp.poulpopass.app.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class CategoryViewer extends AbstractViewer implements CategoryEditedListener {

    private CategoryModel model;

    private JLabel nameLabel;
    private JTagComponent passwords;

    public CategoryViewer(PasswordExplorer explorer, CategoryModel model) {
        super(explorer);
        this.model = model;

        initComponents();

        model.addCategoryEditedListener(this);
    }

    @Override
    protected Component getTopComponent() {
        if (nameLabel == null) {
            nameLabel = new JLabel(model.getName());
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

        for (PasswordModel password : model.getPasswords()) {
            Tag tag = passwords.addTagToList(password.getName());

            tag.addActionListener(password.getOrCreateHighlightListener(explorer));
        }

        add(passwordsLabel, constraint);
        constraint.endComponent = true;
        add(passwords, constraint);
    }

    @Override
    protected void edit(ActionEvent e) {
        Window ancestor = SwingUtilities.getWindowAncestor(this);

        if (ancestor instanceof Frame) {
            EditCategoryDialog.showDialog((Frame) ancestor, model);
        } else {
            EditCategoryDialog.showDialog(null, model);
        }
    }

    @Override
    public void nameChanged(CategoryEvent event) {
        nameLabel.setText(model.getName());
    }

    @Override
    public void associationsChanged(CategoryEvent event) {
        int i = 0;

        List<PasswordModel> models = model.getPasswords();
        Tag[] tags = passwords.getSelectedTags();

        int length = Math.min(models.size(), tags.length);

        for (; i < length; i++) {
            Tag tag = passwords.getTagInListAt(i);

            tag.setName(models.get(i).getName());
        }

        if (models.size() > length) { // add
            for (; i < models.size(); i++) {
                PasswordModel model = models.get(i);

                Tag tag = passwords.addTagToList(model.getName());
                tag.addActionListener(model.getOrCreateHighlightListener(explorer));
            }
        } else { // remove
            while (i < passwords.length()) {
                passwords.removeTagInListAt(i);
            }
        }

        revalidate();
        repaint();
    }

    @Override
    public void associationNameChanged(CategoryEvent event) {
        List<PasswordModel> models = model.getPasswords();

        for (int i = 0; i < models.size(); i++) {
            PasswordModel password = models.get(i);

            passwords.getTagAt(i).setName(password.getName());
        }
    }

    @Override
    protected Icon getIcon() {
        return Icons.CATEGORY;
    }
}