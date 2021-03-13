package fr.poulpocorp.poulpopass.app.tag;

import java.util.EventListener;

public interface TagListener extends EventListener {

    void stateChanged(TagEvent event);
}
