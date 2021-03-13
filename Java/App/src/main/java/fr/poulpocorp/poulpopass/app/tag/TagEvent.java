package fr.poulpocorp.poulpopass.app.tag;

import java.util.EventObject;

public class TagEvent extends EventObject {

    public static final int TAG_ADDED_TO_COMBO = 1;
    public static final int TAG_ADDED_TO_LIST = 2;
    public static final int TAG_REMOVED = 3;
    public static final int TAG_MOVED_TO_COMBO = 4;
    public static final int TAG_MOVED_TO_LIST = 5; // TODO: list is unclear
    public static final int CLEAR = 6; // TODO: list is unclear

    private final Tag item;
    private final int type;

    public TagEvent(TagModel source, Tag item, int type) {
        super(source);
        this.item = item;
        this.type = type;
    }

    public Tag getItem() {
        return item;
    }

    public int getType() {
        return type;
    }
}