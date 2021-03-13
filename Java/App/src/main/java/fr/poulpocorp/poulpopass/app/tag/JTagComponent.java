package fr.poulpocorp.poulpopass.app.tag;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class JTagComponent extends JComponent implements TagModel {

    private TagModel model;

    private final JComboBox<Tag> box;

    private boolean fire = true;

    public JTagComponent() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 2, 2));

        model = new DefaultTagModel();

        box = new JComboBox<>();
        box.setSelectedItem(null);
        box.setRenderer(new CellRenderer());
        box.addItemListener((e) -> {
            if (e.getItem() != null && fire && model.isEditable()) {
                addTagToList(((Tag) e.getItem()).getName());
            }
        });

        model.addTagListener(this::stateChanged);
        model.addEditableListener(this::editStateChanged);

        add(box);
    }

    protected void editStateChanged(PropertyChangeEvent event) {
        box.setVisible((boolean) event.getNewValue());
    }

    protected void stateChanged(TagEvent event) {
        int order = getComponentZOrder(box);

        fire = false;

        switch (event.getType()) {
            case TagEvent.TAG_ADDED_TO_COMBO:
                box.addItem(event.getItem());
                break;
            case TagEvent.TAG_ADDED_TO_LIST:
                add(event.getItem().getButton(), order);
                break;
            case TagEvent.TAG_REMOVED:
                box.removeItem(event.getItem());
                remove(event.getItem().getButton());
                break;
            case TagEvent.TAG_MOVED_TO_COMBO:
                remove(event.getItem().getButton());
                box.addItem(event.getItem());
                break;
            case TagEvent.TAG_MOVED_TO_LIST:
                box.removeItem(event.getItem());
                add(event.getItem().getButton(), order);
                break;
            default:
                return;
        }

        box.setSelectedItem(null);

        fire = true;

        revalidate();
        repaint();
    }

    @Override
    public Tag addTagToComboBox(String tag) {
        return model.addTagToComboBox(tag);
    }

    @Override
    public Tag addTagToList(String tag) {
        return model.addTagToList(tag);
    }

    @Override
    public Tag removeTag(String tag) {
        return model.removeTag(tag);
    }

    @Override
    public boolean removeTag(Tag tag) {
        return model.removeTag(tag);
    }

    @Override
    public Tag getTag(String tag) {
        return model.getTag(tag);
    }

    @Override
    public boolean containsTag(String tag) {
        return model.containsTag(tag);
    }

    @Override
    public Tag[] getTags() {
        return model.getTags();
    }

    @Override
    public Tag[] getSelectedTags() {
        return model.getSelectedTags();
    }

    @Override
    public Tag[] getNotSelectedTags() {
        return model.getNotSelectedTags();
    }

    @Override
    public void addTagListener(TagListener listener) {
        model.addTagListener(listener);
    }

    @Override
    public void removeTagListener(TagListener listener) {
        model.removeTagListener(listener);
    }

    @Override
    public void addEditableListener(PropertyChangeListener listener) {
        model.addEditableListener(listener);
    }

    @Override
    public void removeEditableListener(PropertyChangeListener listener) {
        model.removeEditableListener(listener);
    }

    public boolean isEditable() {
        return model.isEditable();
    }

    public void setEditable(boolean editable) {
        model.setEditable(editable);
    }

    private static class CellRenderer extends BasicComboBoxRenderer {

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof Tag) {
                setText(((Tag) value).getName());
            }

            return this;
        }
    }
}