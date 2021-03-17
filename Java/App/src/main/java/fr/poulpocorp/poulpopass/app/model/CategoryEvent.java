package fr.poulpocorp.poulpopass.app.model;

import java.util.EventObject;

public class CategoryEvent extends EventObject {

    public static final int NAME = 1;
    public static final int ASSOCIATION = 2;

    private final int type;

    public CategoryEvent(CategoryModel source, int type) {
        super(source);

        this.type = type;
    }

    public int getType() {
        return type;
    }
}