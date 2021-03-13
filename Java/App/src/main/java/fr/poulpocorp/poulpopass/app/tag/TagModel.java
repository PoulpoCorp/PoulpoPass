package fr.poulpocorp.poulpopass.app.tag;

import java.beans.PropertyChangeListener;

public interface TagModel {

    Tag addTagToComboBox(String tag);

    Tag addTagToList(String tag);

    Tag removeTag(String tag);

    boolean removeTag(Tag tag);

    Tag getTag(String tag);

    boolean containsTag(String tag);

    void setEditable(boolean editable);

    boolean isEditable();

    Tag[] getTags();

    Tag[] getSelectedTags();

    Tag[] getNotSelectedTags();

    void addTagListener(TagListener listener);

    void removeTagListener(TagListener listener);

    void addEditableListener(PropertyChangeListener listener);

    void removeEditableListener(PropertyChangeListener listener);
}