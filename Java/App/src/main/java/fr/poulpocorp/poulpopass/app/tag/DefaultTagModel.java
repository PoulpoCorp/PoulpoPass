package fr.poulpocorp.poulpopass.app.tag;

import fr.poulpocorp.poulpopass.app.utils.CompositeList;

import javax.swing.event.EventListenerList;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public class DefaultTagModel implements TagModel {

    private final EventListenerList listenerList = new EventListenerList();

    /**
     * The first part contains tags which are in the combo box
     * The second part contains tags which are showed
     */
    private final CompositeList<Tag> elements = new CompositeList<>(new ArrayList<>(), new ArrayList<>());

    private boolean editable = true;

    public DefaultTagModel() {

    }

    @Override
    public Tag addTagToComboBox(String tag) {
        Tag t = getTag(tag);

        if (t != null) {
            if (t.isSelected()) {
                elements.remove(t);
                elements.addFirst(t);

                t.selected = false;

                fireTagListeners(t, TagEvent.TAG_MOVED_TO_COMBO);
            }

            return null;
        }

        t = new Tag(tag, false);
        t.addActionListener(createButtonListener(t));

        elements.addFirst(t);

        fireTagListeners(t, TagEvent.TAG_ADDED_TO_COMBO);

        return t;
    }


    @Override
    public Tag addTagToList(String tag) {
        Tag t = getTag(tag);

        if (t != null) {
            if (!t.isSelected()) {
                elements.remove(t);
                elements.addSecond(t);

                t.selected = true;

                fireTagListeners(t, TagEvent.TAG_MOVED_TO_LIST);
            }

            return null;
        }

        t = new Tag(tag, true);
        t.addActionListener(createButtonListener(t));

        elements.addSecond(t);

        fireTagListeners(t, TagEvent.TAG_ADDED_TO_LIST);

        return t;
    }

    protected ActionListener createButtonListener(Tag tag) {
        return (event) -> {
            if (editable) {
                addTagToComboBox(tag.getName());
            }
        };
    }

    @Override
    public Tag removeTag(String tag) {
        for (Tag t : elements) {
            if (t.getName().equals(tag)) {
                elements.remove(t);

                fireTagListeners(t, TagEvent.TAG_REMOVED);

                return t;
            }
        }

        return null;
    }

    @Override
    public boolean removeTag(Tag tag) {
        boolean removed = elements.remove(tag);

        if (removed) {
            fireTagListeners(tag, TagEvent.TAG_REMOVED);

            return true;
        }

        return false;
    }

    @Override
    public Tag getTag(String tag) {
        for (Tag t : elements) {
            if (t.getName().equals(tag)) {
                return t;
            }
        }

        return null;
    }

    @Override
    public boolean containsTag(String tag) {
        return getTag(tag) != null;
    }

    @Override
    public void setEditable(boolean editable) {
        if (this.editable != editable) {
            this.editable = editable;

            fireEditableListener();
        }
    }

    @Override
    public boolean isEditable() {
        return editable;
    }

    protected void fireEditableListener() {
        PropertyChangeListener[] listeners = listenerList.getListeners(PropertyChangeListener.class);

        if (listeners.length != 0) {
            PropertyChangeEvent event = new PropertyChangeEvent(this, null, !editable, editable);

            for (PropertyChangeListener listener : listeners) {
                listener.propertyChange(event);
            }
        }
    }

    @Override
    public Tag[] getTags() {
        return elements.toArray(new Tag[0]);
    }

    @Override
    public Tag[] getSelectedTags() {
        return elements.getSecond().toArray(new Tag[0]);
    }

    @Override
    public Tag[] getNotSelectedTags() {
        return elements.getFirst().toArray(new Tag[0]);
    }

    protected void fireTagListeners(Tag tag, int type) {
        TagListener[] listeners = listenerList.getListeners(TagListener.class);

        if (listeners.length != 0) {
            TagEvent event = new TagEvent(this, tag, type);

            for (TagListener listener : listeners) {
                listener.stateChanged(event);
            }
        }
    }

    @Override
    public void addTagListener(TagListener listener) {
        listenerList.add(TagListener.class, listener);
    }

    @Override
    public void removeTagListener(TagListener listener) {
        listenerList.remove(TagListener.class, listener);
    }

    @Override
    public void addEditableListener(PropertyChangeListener listener) {
        listenerList.add(PropertyChangeListener.class, listener);
    }

    @Override
    public void removeEditableListener(PropertyChangeListener listener) {
        listenerList.remove(PropertyChangeListener.class, listener);
    }
}