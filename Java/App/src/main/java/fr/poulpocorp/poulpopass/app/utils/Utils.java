package fr.poulpocorp.poulpopass.app.utils;

import fr.poulpocorp.poulpopass.app.text.PPPasswordTextField;

import javax.swing.*;

public class Utils {

    public static PPPasswordTextField createPasswordTextField(char[] password) {
        PPPasswordTextField field = new PPPasswordTextField();
        field.setText(String.valueOf(password));

        JButton see = new JButton("Show");
        see.addActionListener((a) -> {
            if (field.echoCharIsSet()) {
                field.setEchoChar((char) 0);
                see.setText("Hide");
            } else {
                field.setEchoChar('•');
                see.setText("Show");
            }
        });

        field.setTrailingComponent(see);

        return field;
    }

    public static PPPasswordTextField createPasswordLabel(char[] password) {
        PPPasswordTextField field = createPasswordTextField(password);
        field.setEditable(false);
        field.setFocusable(false);
        field.setBorder(null);

        return field;
    }
}