package fr.poulpocorp.poulpopass.app.viewer;

import com.formdev.flatlaf.FlatClientProperties;
import fr.poulpocorp.poulpopass.app.layout.VerticalConstraint;
import fr.poulpocorp.poulpopass.app.model.CategoryModel;
import fr.poulpocorp.poulpopass.app.model.PasswordManagerModel;
import fr.poulpocorp.poulpopass.app.model.PasswordModel;
import fr.poulpocorp.poulpopass.app.tag.Tag;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Layout description
 *
 * JPanel mainContent with BorderLayout
 *   -> JScrollPane scrollPane
 *        -> JPanel content with VerticalLayout
 *             -> JLabel "Name"
 *             -> PPTextField nameField
 *             -> Separator
 *             -> JTagComponent passwords
 *   -> JPanel bottomPanel with HorizontalLayout
 *        -> JButton saveButton (at right)
 */
public class EditCategoryDialog extends AbstractEditDialog<CategoryModel> {

    public static void showDialog(Frame parent, CategoryModel model) {
        new EditCategoryDialog(parent, model);
    }

    private EditCategoryDialog(Frame parent, CategoryModel model) {
        super(parent, "Edit category", model);
    }

    @Override
    protected String getModelName() {
        return model.getName();
    }

    @Override
    protected void initFields(JPanel content, VerticalConstraint constraint, Color titleLabelColor) {

    }

    @Override
    protected void fillAssociations() {
        for (PasswordModel password : model.getPasswordManager().getPasswords()) {
            associations.addTagToComboBox(password.getName());
        }
        for (PasswordModel password : model.getPasswords()) {
            associations.moveTag(password.getName());
        }
    }

    @Override
    protected void save(ActionEvent e) {
        PasswordManagerModel manager = model.getPasswordManager();

        boolean categoryNameChanged = !nameField.getText().equals(model.getName());

        if (categoryNameChanged && manager.containsCategoryWithName(nameField.getText())) {
            // no
            nameField.putClientProperty(FlatClientProperties.OUTLINE, FlatClientProperties.OUTLINE_ERROR);
            repaint();
        } else {
            model.edit();

            model.setName(nameField.getText());

            for (Tag tag : associations.getTags()) {
                PasswordModel password = manager.getPasswordIfExists(tag.getName());

                if (tag.isSelected()) {
                    model.associateWith(password);
                } else {
                    model.dissociateWith(password);
                }
            }

            if (model.finishEdit()) {
                dispose();
            }
        }
    }
}