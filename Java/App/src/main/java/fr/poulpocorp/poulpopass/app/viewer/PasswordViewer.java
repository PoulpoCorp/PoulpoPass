package fr.poulpocorp.poulpopass.app.viewer;

import fr.poulpocorp.poulpopass.app.layout.VerticalConstraint;
import fr.poulpocorp.poulpopass.app.text.PPPasswordTextField;
import fr.poulpocorp.poulpopass.app.utils.Icons;
import fr.poulpocorp.poulpopass.app.utils.Utils;
import fr.poulpocorp.poulpopass.core.Password;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PasswordViewer extends AbstractViewer<Password> {

    private JLabel passwordLabel;
    private PPPasswordTextField passwordField;

    private JLabel nameLabel; // display name
    private JLabel name;      // display the name of the password

    private JLabel urlLabel;
    private List<JLabel> urls;

    public PasswordViewer(PasswordExplorer explorer, Password password) {
        super(explorer, password);
    }

    @Override
    protected Component getTopComponent() {
        if (nameLabel == null) {
            nameLabel = new JLabel("Name");
            nameLabel.setForeground(nameLabel.getForeground().darker());
            nameLabel.setIcon(Icons.WEBSITE);

            String[] urls = element.getURLs();

            if (urls.length > 0) {
                SwingWorker<Void, Void> worker = new FetchIconWorker(urls[0], nameLabel);
                worker.execute();
            }
        }

        return nameLabel;
    }

    @Override
    protected void initFields() {
        passwordLabel = new JLabel("Password");
        passwordLabel.setForeground(passwordLabel.getForeground().darker());

        name = new JLabel(element.getName());
        passwordField = Utils.createPasswordLabel(element.getPassword());

        urlLabel = new JLabel("Urls");
        urlLabel.setForeground(urlLabel.getForeground().darker());

        VerticalConstraint constraint = new VerticalConstraint();
        constraint.xAlignment = 0;

        add(name, constraint);

        constraint.fillXAxis = true;
        add(new JSeparator(), constraint);

        constraint.fillXAxis = false;
        add(passwordLabel, constraint);

        constraint.fillXAxis = true;
        add(passwordField, constraint);

        constraint.fillXAxis = false;
        add(urlLabel, constraint);

        urls = new ArrayList<>();

        for (String url : element.getURLs()) {
            JLabel label = new JLabel(url);

            urls.add(label);
            add(label, constraint);
        }
    }

    @Override
    protected Icon getIcon() {
        return Icons.PASSWORD;
    }

    private static class FetchIconWorker extends SwingWorker<Void, Void> {

        private String url;
        private JLabel nameLabel;

        public FetchIconWorker(String url, JLabel nameLabel) {
            this.url = url;
            this.nameLabel = nameLabel;
        }

        @Override
        protected Void doInBackground() {
            Icon icon = Icons.fetch(url, 16);

            if (icon != null) {
                nameLabel.setIcon(icon);
            }

            return null;
        }
    }
}