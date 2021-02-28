package fr.poulpocorp.poulpopass.app.utils;

import java.io.File;
import java.nio.file.Path;

public class Cache {

    public static Path ROOT;

    public static void setRoot(Path root) {
        ROOT = root;
    }

    public static void setRoot(File root) {
        ROOT = root.toPath();
    }

    public static void setRoot(String root) {
        ROOT = Path.of(root);
    }

    public static void setRoot(String root, String... more) {
        ROOT = Path.of(root, more);
    }

    public static Path of(String path) {
        return ROOT.resolve(path);
    }

    public static Path of(String path, String more) {
        return resolve(Path.of(path, more));
    }

    public static Path resolve(Path path) {
        return ROOT.resolve(path);
    }

    static {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            ROOT = Path.of(System.getenv("APPDATA") + "/PoulpoPass");
        } else {
            ROOT = Path.of(System.getProperty("user.home") + "/PoulpoPass");
        }
    }
}