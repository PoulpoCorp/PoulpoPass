package fr.poulpocorp.poulpopass.app.viewer;

import fr.poulpocorp.poulpopass.app.layout.VerticalConstraint;
import fr.poulpocorp.poulpopass.app.model.CategoryModel;
import fr.poulpocorp.poulpopass.app.model.PasswordEditedListener;
import fr.poulpocorp.poulpopass.app.model.PasswordEvent;
import fr.poulpocorp.poulpopass.app.model.PasswordModel;
import fr.poulpocorp.poulpopass.app.tag.JTagComponent;
import fr.poulpocorp.poulpopass.app.tag.Tag;
import fr.poulpocorp.poulpopass.app.text.PPPasswordTextField;
import fr.poulpocorp.poulpopass.app.utils.FaviconFetcher;
import fr.poulpocorp.poulpopass.app.utils.Icons;
import fr.poulpocorp.poulpopass.app.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class PasswordViewer extends AbstractViewer implements PasswordEditedListener {

    private PasswordModel model;

    private JLabel nameLabel; // display name
    private JLabel name;      // display the name of the password

    private JLabel passwordLabel;
    private PPPasswordTextField passwordField;

    private JLabel urlLabel;
    private ArrayList<JLabel> urlLabels;

    private JTagComponent categories;

    public PasswordViewer(PasswordExplorer explorer, PasswordModel model) {
        super(explorer);

        this.model = model;
        initComponents();

        model.addPasswordEditedListener(this);
    }

    @Override
    protected Component getTopComponent() {
        if (nameLabel == null) {
            nameLabel = new JLabel("Name");
            nameLabel.setForeground(Utils.applyThemeColorFunction(nameLabel.getForeground()));
            nameLabel.setIcon(Icons.WEBSITE);

            List<String> urls = model.getURLs();

            if (urls.size() > 0) {
                FaviconFetcher.fetch(urls.get(0), 16, nameLabel::setIcon);
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

        name = new JLabel(model.getName());
        passwordField = Utils.createPasswordLabel(model.getPassword());

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

        for (String url : model.getURLs()) {
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

        categories = new JTagComponent();
        categories.setEditable(false);

        for (CategoryModel category : model.getCategories()) {
            Tag tag = categories.addTagToList(category.getName());

            tag.addActionListener((e) -> explorer.highlightCategory(category));
        }

        constraint.fillXAxis = true;
        constraint.endComponent = true;
        add(categories, constraint);
    }

    @Override
    protected void edit(ActionEvent e) {
        Window ancestor = SwingUtilities.getWindowAncestor(this);

        if (ancestor instanceof Frame) {
            EditPasswordDialog.showDialog((Frame) ancestor, model);
        } else {
            EditPasswordDialog.showDialog(null, model);
        }
    }

    @Override
    protected Icon getIcon() {
        return Icons.PASSWORD;
    }

    @Override
    public void nameChanged(PasswordEvent event) {
        name.setText(model.getName());
    }

    @Override
    public void passwordChanged(PasswordEvent event) {
        passwordField.setText(String.valueOf(model.getPassword())); // TODO: Create a non String-api method
    }

    @Override
    public void urlsChanged(PasswordEvent event) {
        List<String> urls = model.getURLs();

        // If the first url has changed, we need to update the icon
        if (urls.size() > 0) {
            String oldFirstUrl = urlLabels.get(0).getText();

            String first = urls.get(0);
            if (!oldFirstUrl.equals(first)) {
                FaviconFetcher.fetch(first, 16, nameLabel::setIcon); // update icon
            }
        }

        // Update urls text
        int length = Math.min(urls.size(), urlLabels.size());
        for (int i = 0; i < length; i++) {
            urlLabels.get(i).setText(urls.get(i));
        }

        if (urls.size() > length) { // Need to add labels
            int addIndex = getComponentZOrder(urlLabels.get(length - 1)) + 1;

            VerticalConstraint constraint = new VerticalConstraint();
            constraint.xAlignment = 0;

            for (int i = length; i < urls.size(); i++) {
                JLabel label = new JLabel(urls.get(i));

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
    public void associationsChanged(PasswordEvent event) {
        int i = 0;

        List<CategoryModel> models = model.getCategories();
        Tag[] tags = categories.getSelectedTags();

        int length = Math.min(models.size(), tags.length);

        for (; i < length; i++) {
            Tag tag = categories.getTagInListAt(i);

            tag.setName(models.get(i).getName());
        }

        if (models.size() > length) { // add
            for (; i < models.size(); i++) {
                categories.addTagToList(models.get(i).getName());
            }
        } else { // remove
            while (i < categories.length()) {
                categories.removeTagInListAt(i);
            }

        }

        revalidate();
        repaint();
    }

    @Override
    public void associationNameChanged(PasswordEvent event) {
        List<CategoryModel> models = model.getCategories();

        for (int i = 0; i < models.size(); i++) {
            CategoryModel category = models.get(i);

            categories.getTagAt(i).setName(category.getName());
        }
    }
}