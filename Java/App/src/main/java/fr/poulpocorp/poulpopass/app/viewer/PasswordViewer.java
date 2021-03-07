package fr.poulpocorp.poulpopass.app.viewer;

import com.formdev.flatlaf.FlatClientProperties;
import fr.poulpocorp.poulpopass.app.layout.VCOrientation;
import fr.poulpocorp.poulpopass.app.layout.VerticalConstraint;
import fr.poulpocorp.poulpopass.app.model.PasswordEditedListener;
import fr.poulpocorp.poulpopass.app.model.PasswordEvent;
import fr.poulpocorp.poulpopass.app.text.JLabelLink;
import fr.poulpocorp.poulpopass.app.text.PPPasswordTextField;
import fr.poulpocorp.poulpopass.app.utils.FaviconFetcher;
import fr.poulpocorp.poulpopass.app.utils.Icons;
import fr.poulpocorp.poulpopass.app.utils.Utils;
import fr.poulpocorp.poulpopass.core.Category;
import fr.poulpocorp.poulpopass.core.Password;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PasswordViewer extends AbstractViewer<Password> {

    private JLabel nameLabel; // display name
    private JLabel name;      // display the name of the password

    private JLabel passwordLabel;
    private PPPasswordTextField passwordField;

    private JLabel urlLabel;
    private ArrayList<JLabel> urlLabels;

    public PasswordViewer(PasswordExplorer explorer, Password password) {
        super(explorer, password);

        addPasswordEditedListener(this::updateViewer);
    }

    @Override
    protected Component getTopComponent() {
        if (nameLabel == null) {
            nameLabel = new JLabel("Name");
            nameLabel.setForeground(Utils.applyThemeColorFunction(nameLabel.getForeground()));
            nameLabel.setIcon(Icons.WEBSITE);

            String[] urls = element.getURLs();

            if (urls.length > 0) {
                FaviconFetcher.fetch(urls[0], 16, nameLabel::setIcon);
            }
        }

        return nameLabel;
    }

    @Override
    protected void initFields() {
        // General information: name, password
        passwordLabel = new JLabel("Password");

        Color titleLabelColor = Utils.applyThemeColorFunction(passwordLabel.getForeground());
        passwordLabel.setForeground(titleLabelColor);

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
        add(new JSeparator(), constraint);

        // urls
        urlLabel = new JLabel("Urls");
        urlLabel.setForeground(titleLabelColor);

        constraint.fillXAxis = false;
        add(urlLabel, constraint);

        urlLabels = new ArrayList<>();

        for (String url : element.getURLs()) {
            JLabel label = new JLabel(url);

            urlLabels.add(label);
            add(label, constraint);
        }

        constraint.fillXAxis = true;
        add(new JSeparator(), constraint);
        constraint.fillXAxis = false;

        // Categories
        JLabel categoryLabel = new JLabel("In categories");
        categoryLabel.setForeground(titleLabelColor);

        add(categoryLabel, constraint);

        for (Category category : element.getCategories()) {
            JLabelLink link = new JLabelLink(category.getName());
            link.addActionListener((l) -> {
                explorer.highlightCategory(category);
            });

            add(link, constraint);
        }

        // edit button
        JButton edit = new JButton(Icons.EDIT);
        edit.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_TOOLBAR_BUTTON);
        edit.addActionListener((e) -> {
            Window ancestor = SwingUtilities.getWindowAncestor(this);

            PasswordEvent event;
            if (ancestor instanceof Frame) {
                event = EditPasswordDialog.showDialog((Frame) ancestor, element);
            } else {
                event = EditPasswordDialog.showDialog(null, element);
            }

            if (event != null) {
                firePasswordEditedListeners(event);
            }
        });

        constraint.orientation = VCOrientation.BOTTOM;
        constraint.xAlignment = 1;
        add(edit, constraint);
    }

    public void updateViewer(PasswordEvent event) {
        name.setText(element.getName());
        passwordField.setText(String.valueOf(element.getPassword())); // TODO: Create a non String-api method

        String[] urls = element.getURLs();

        // If the first url has changed, we need to update the icon
        if (urlLabels.size() > 0) {
            String oldFirstUrl = urlLabels.get(0).getText();

            if (!oldFirstUrl.equals(urls[0])) {
                FaviconFetcher.fetch(urls[0], 16, nameLabel::setIcon); // update icon
            }
        }

        // Update urls text
        int length = Math.min(urls.length, urlLabels.size());
        for (int i = 0; i < length; i++) {
            urlLabels.get(i).setText(urls[i]);
        }

        if (urls.length > length) { // Need to add labels
            int addIndex = getComponentZOrder(urlLabels.get(length - 1)) + 1;

            VerticalConstraint constraint = new VerticalConstraint();
            constraint.xAlignment = 0;

            for (int i = length; i < urls.length; i++) {
                JLabel label = new JLabel(urls[i]);

                urlLabels.add(label);
                add(label, constraint, addIndex);

                addIndex++;
            }
        } else { // Need to remove labels
            for (int i = length; i < urlLabels.size(); i++) {
                remove(urlLabels.get(i));
            }

            urlLabels.trimToSize();
        }

        revalidate();
        repaint();
    }

    @Override
    protected Icon getIcon() {
        return Icons.PASSWORD;
    }

    protected void firePasswordEditedListeners(PasswordEvent event) {
        PasswordEditedListener[] listeners = listenerList.getListeners(PasswordEditedListener.class);

        for (PasswordEditedListener listener : listeners) {
            listener.passwordEdited(event);
        }
    }

    public void addPasswordEditedListener(PasswordEditedListener listener) {
        listenerList.add(PasswordEditedListener.class, listener);
    }

    public void removePasswordEditedListener(PasswordEditedListener listener) {
        listenerList.remove(PasswordEditedListener.class, listener);
    }
}