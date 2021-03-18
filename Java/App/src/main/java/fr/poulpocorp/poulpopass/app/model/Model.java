package fr.poulpocorp.poulpopass.app.model;

import javax.swing.event.EventListenerList;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public abstract class Model {

    protected final PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    protected final EventListenerList listenerList = new EventListenerList();

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(property, listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(String property, PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(property, listener);
    }

    protected void firePropertyChange(String property, Object oldValue, Object newValue) {
        changeSupport.firePropertyChange(property, oldValue, newValue);
    }
}