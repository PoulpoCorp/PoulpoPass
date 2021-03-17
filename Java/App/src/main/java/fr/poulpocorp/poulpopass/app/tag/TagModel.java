package fr.poulpocorp.poulpopass.app.tag;

import java.beans.PropertyChangeListener;

public interface TagModel {

    Tag addTagToComboBox(String tag);

    Tag addTagToList(String tag);

    void moveTag(String tag);

    void moveTag(Tag tag);

    Tag removeTag(String tag);

    boolean removeTag(Tag tag);

    Tag removeTagAt(int index);

    Tag removeTagInComboBoxAt(int index);

    Tag removeTagInListAt(int index);

    void removeAllTags();

    Tag getTag(String tag);

    Tag getTagInComboBoxAt(int index);

    Tag getTagInListAt(int index);

    Tag getTagAt(int index);

    boolean containsTag(String tag);

    void setEditable(boolean editable);

    boolean isEditable();

    Tag[] getTags();

    Tag[] getSelectedTags();

    Tag[] getNotSelectedTags();

    int selected();

    int notSelected();

    int length();

    void addTagListener(TagListener listener);

    void removeTagListener(TagListener listener);

    void addEditableListener(PropertyChangeListener listener);

    void removeEditableListener(PropertyChangeListener listener);
}