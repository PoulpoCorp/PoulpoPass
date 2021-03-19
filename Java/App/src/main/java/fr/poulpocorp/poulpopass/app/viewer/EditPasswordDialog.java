package fr.poulpocorp.poulpopass.app.viewer;

import com.formdev.flatlaf.FlatClientProperties;
import fr.poulpocorp.poulpopass.app.layout.HorizontalLayout;
import fr.poulpocorp.poulpopass.app.layout.VerticalConstraint;
import fr.poulpocorp.poulpopass.app.model.CategoryModel;
import fr.poulpocorp.poulpopass.app.model.PasswordManagerModel;
import fr.poulpocorp.poulpopass.app.model.PasswordModel;
import fr.poulpocorp.poulpopass.app.tag.Tag;
import fr.poulpocorp.poulpopass.app.text.PPPasswordTextField;
import fr.poulpocorp.poulpopass.app.text.PPTextField;
import fr.poulpocorp.poulpopass.app.utils.Icons;
import fr.poulpocorp.poulpopass.app.utils.Utils;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Layout description
 *
 * JPanel mainContent with BorderLayout
 *   -> JScrollPane scrollPane
 *        -> JPanel content with VerticalLayout
 *             -> JLabel "Name"
 *             -> PPTextField nameField
 *             -> Separator
 *             -> JLabel "Password"
 *             -> PPPasswordTextField passwordField
 *             -> JLabel "Urls"
 *             -> PPTextField url1
 *             -> ...
 *             -> PPTextField urlN
 *             -> JPanel newUrlPanel with HorizontalLayout
 *                  -> JLabel "New Url"
 *                  -> JButton newUrlButton
 *             -> JTagComponent categories
 *   -> JPanel bottomPanel with HorizontalLayout
 *        -> JButton saveButton (at right)
 */
public class EditPasswordDialog extends AbstractEditDialog<PasswordModel> {

    public static void showDialog(Frame parent, PasswordModel model) {
        new EditPasswordDialog(parent, model);
    }

    private PPPasswordTextField passwordField;
    private List<PPTextField> urlFields;

    private JPanel newUrlPanel;

    private EditPasswordDialog(Frame parent, PasswordModel model) {
        super(parent, "Edit password", model);
    }

    @Override
    protected String getModelName() {
        return model.getName();
    }

    @Override
    protected void initFields(JPanel content, VerticalConstraint constraint, Color titleLabelColor) {
        // Password
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setForeground(titleLabelColor);

        passwordField = Utils.createPasswordTextField(model.getPassword());

        // Urls
        newUrlPanel = new JPanel();
        newUrlPanel.setLayout(new HorizontalLayout(3, 3));

        JLabel urlLabel = new JLabel("Urls");
        urlLabel.setForeground(titleLabelColor);

        JLabel newUrlLabel = new JLabel("New url:");
        JButton newUrlButton = new JButton(Icons.ADD);
        newUrlButton.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_TOOLBAR_BUTTON);
        newUrlButton.addActionListener(this::addURL);

        urlFields = new ArrayList<>();

        // Layout
        // password
        constraint.fillXAxis = false;
        content.add(passwordLabel, constraint);
        constraint.fillXAxis = true;
        content.add(passwordField, constraint);

        // urls
        constraint.fillXAxis = false;
        content.add(urlLabel, constraint);

        constraint.fillXAxis = true;
        for (String url : model.getURLs()) {
            PPTextField field = createURLField(url);

            content.add(field, constraint);
        }

        newUrlPanel.add(newUrlLabel);
        newUrlPanel.add(newUrlButton);

        constraint.fillXAxis = false;
        constraint.xAlignment = 1;
        content.add(newUrlPanel, constraint);
    }

    @Override
    protected void fillAssociations() {
        for (CategoryModel category : model.getPasswordManager().getCategories()) {
            associations.addTagToComboBox(category.getName());
        }
        for (CategoryModel category : model.getCategories()) {
            associations.moveTag(category.getName());
        }
    }

    private PPTextField createURLField(String url) {
        PPTextField urlField = new PPTextField(url);

        JButton remove = new JButton(Icons.REMOVE);
        remove.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_TOOLBAR_BUTTON);
        remove.addActionListener((l) -> removeURL(urlField));

        urlField.setTrailingComponent(remove);
        urlFields.add(urlField);

        return urlField;
    }

    /**
     * Add a text field before the newUrlPanel, which is the panel
     * who contains the add button
     */
    private void addURL(ActionEvent e) {
        int index = content.getComponentZOrder(newUrlPanel);

        PPTextField field = createURLField(null);
        VerticalConstraint constraint = new VerticalConstraint();
        constraint.fillXAxis = true;

        content.add(field, constraint, index);
        content.revalidate();
        content.repaint();
    }

    private void removeURL(PPTextField urlField) {
        content.remove(urlField);
        content.revalidate();
        content.repaint();

        urlFields.remove(urlField);
    }

    @Override
    protected void save(ActionEvent e) {
        PasswordManagerModel manager = model.getPasswordManager();

        boolean passwordNameChanged = !nameField.getText().equals(model.getName());

        if (passwordNameChanged && manager.containsPasswordWithName(nameField.getText())) {
            nameField.error("A password with this name already exist");
            repaint();
        } else {
            model.edit();

            model.setName(nameField.getText());

            if (!Arrays.equals(passwordField.getPassword(), model.getPassword())) {
                model.setPassword(passwordField.getPassword());
            }

            List<String> urls = urlFields.stream()
                    .map(JTextComponent::getText)
                    .filter((s) -> !s.isEmpty())
                    .collect(Collectors.toList());

            if (!urls.equals(model.getURLs())) {
                model.setURLs(urls);
            }

            for (Tag tag : associations.getTags()) {
                CategoryModel category = manager.getCategoryIfExists(tag.getName());

                if (tag.isSelected()) {
                    model.associateWith(category);
                } else {
                    model.dissociateWith(category);
                }
            }

            if (model.finishEdit()) {
                dispose();
            }
        }
    }
}