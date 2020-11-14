package fr.poulpocorp.poulpopass.core;

public final class Categories {
    static public final class Category {
        public String name;

        private Category(String name) {this.name = name;}
    }

    static public Category NewCategory(String name) {
        return new Category(name);
    }

    private Categories() {}

    static private Categories instance = new Categories();


    static public Categories getInstance() {
        return instance;
    }
}
