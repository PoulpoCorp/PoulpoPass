package fr.poulpocorp.poulpopass.app.text;

import java.awt.*;

public interface IPPTextField {

    Component getTrailingComponent();

    void setTrailingComponent(Component trailingComponent);

    Component getLeadingComponent();

    void setLeadingComponent(Component leadingComponent);
}
