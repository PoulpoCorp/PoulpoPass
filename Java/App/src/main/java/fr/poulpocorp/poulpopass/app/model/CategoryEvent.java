package fr.poulpocorp.poulpopass.app.model;

import java.util.EventObject;

public class CategoryEvent extends EventObject {

    public static final int NAME = 1;
    public static final int ASSOCIATION = 2;
    public static final int ASSOCIATION_NAME = 4;

    private final int type;

    public CategoryEvent(CategoryModel source, int type) {
        super(source);

        this.type = type;
    }

    public CategoryEvent(PasswordModel source) {
        super(source);

        this.type = ASSOCIATION_NAME;
    }

    public int getType() {
        return type;
    }
}