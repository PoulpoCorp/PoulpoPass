package fr.poulpocorp.poulpopass.app.viewer;

import com.formdev.flatlaf.FlatClientProperties;
import fr.poulpocorp.poulpopass.app.layout.*;
import fr.poulpocorp.poulpopass.app.text.PPPasswordTextField;
import fr.poulpocorp.poulpopass.app.text.PPTextField;
import fr.poulpocorp.poulpopass.app.utils.Icons;
import fr.poulpocorp.poulpopass.app.utils.Utils;
import fr.poulpocorp.poulpopass.core.IPasswordManager;
import fr.poulpocorp.poulpopass.core.Password;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class EditPasswordDialog extends JDialog {

    /**
     * @return {@code true} if the given password has been modified
     */
    public static boolean showDialog(Frame parent, Password password) {
        EditPasswordDialog dialog = new EditPasswordDialog(parent, password);

        return dialog.modified;
    }

    private final Password password;

    private JScrollPane scrollPane;
    private JPanel content;

    private JTextField nameField;
    private PPPasswordTextField passwordField;
    private final List<PPTextField> urlFields = new ArrayList<>();

    private JPanel newUrlPanel;

    private JPanel bottomPanel;

    private boolean modified = false;

    private EditPasswordDialog(Frame parent, Password password) {
        super(parent, "Edit password", true);

        this.password = password;

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initComponents();

        pack();
        setLocationRelativeTo(parent);
        setVisible(true);
    }

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
     *   -> JPanel bottomPanel with HorizontalLayout
     *        -> JButton saveButton (at right)
     */
    private void initComponents() {
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BorderLayout());

        scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        content = new JPanel();
        content.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        content.setLayout(new VerticalLayout(5, 5));

        // Name
        JLabel nameLabel = new JLabel("Name");
        Color titleLabelColor = Utils.applyThemeColorFunction(nameLabel.getForeground());

        nameLabel.setForeground(titleLabelColor);

        nameField = new JTextField(password.getName());

        // Password
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setForeground(titleLabelColor);

        passwordField = Utils.createPasswordTextField(password.getPassword());

        // Urls
        newUrlPanel = new JPanel();
        newUrlPanel.setLayout(new HorizontalLayout(3, 3));

        JLabel urlLabel = new JLabel("Urls");
        urlLabel.setForeground(titleLabelColor);

        JLabel newUrlLabel = new JLabel("New url:");
        JButton newUrlButton = new JButton(Icons.ADD);
        newUrlButton.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_TOOLBAR_BUTTON);
        newUrlButton.addActionListener(this::addURL);

        // Bottom panel
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new HorizontalLayout(3, 3));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Save
        JButton save = new JButton(Icons.SAVE);
        save.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_TOOLBAR_BUTTON);
        save.addActionListener(this::save);

        // Content layout
        VerticalConstraint constraint = new VerticalConstraint();

        // name
        constraint.xAlignment = 0;
        content.add(nameLabel, constraint);
        constraint.fillXAxis = true;
        content.add(nameField, constraint);

        content.add(new JSeparator());

        // password
        constraint.fillXAxis = false;
        content.add(passwordLabel, constraint);
        constraint.fillXAxis = true;
        content.add(passwordField, constraint);

        // urls
        constraint.fillXAxis = false;
        content.add(urlLabel, constraint);

        constraint.fillXAxis = true;
        for (String url : password.getURLs()) {
            PPTextField field = createURLField(url);

            content.add(field, constraint);
        }

        newUrlPanel.add(newUrlLabel);
        newUrlPanel.add(newUrlButton);

        constraint.fillXAxis = false;
        constraint.xAlignment = 1;
        content.add(newUrlPanel, constraint);

        scrollPane.setViewportView(content);

        // Bottom layout
        // Save button
        HorizontalConstraint hConstraint = new HorizontalConstraint();
        hConstraint.orientation = HCOrientation.RIGHT;

        // Add in reverse order
        bottomPanel.add(save, hConstraint);
        bottomPanel.add(new JLabel("Save"), hConstraint);

        // Finish layout
        mainContent.add(scrollPane, BorderLayout.CENTER);
        mainContent.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(mainContent);
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

        urlFields.add(field);
    }

    private void removeURL(PPTextField urlField) {
        content.remove(urlField);
        content.revalidate();
        content.repaint();

        urlFields.remove(urlField);
    }

    // TODO: Check if the user has modified the password
    private void save(ActionEvent e) {
        IPasswordManager manager = password.getPasswordManager();

        if (!nameField.getText().equals(password.getName()) &&              // check if the user changes the password name
                manager.containsPasswordWithName(nameField.getText())) {
            // no
            nameField.putClientProperty(FlatClientProperties.OUTLINE, FlatClientProperties.OUTLINE_ERROR);
            repaint();

        } else {
            // yeah!
            password.setName(nameField.getText());
            password.setPassword(passwordField.getPassword());

            password.removeAllUrl();

            for (PPTextField field : urlFields) {
                password.addURL(field.getText());
            }

            modified = true;

            dispose();
        }
    }
}