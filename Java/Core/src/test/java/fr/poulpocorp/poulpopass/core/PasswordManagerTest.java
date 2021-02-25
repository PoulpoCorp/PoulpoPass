package fr.poulpocorp.poulpopass.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class PasswordManagerTest {

    @Test
    void readAndWrite() {
        char[] password = "Hello world".toCharArray();
        Path path = Path.of("test.pass");

        PasswordManager manager = new PasswordManager(password);
        manager.setPath(path);

        Password gmail = manager.getOrCreatePassword("Gmail", "gmail".toCharArray());
        gmail.addURL("gmail.com");

        Password amazon = manager.getOrCreatePassword("Amazon", "amazon".toCharArray());
        amazon.addURL("amazon.com");
        Password alibaba = manager.getOrCreatePassword("Alibaba", "alibaba".toCharArray());
        alibaba.addURL("alibaba.com");

        Password youtube = manager.getOrCreatePassword("Youtube", "youtube".toCharArray());
        youtube.addURL("youtube.com");
        Password twitch = manager.getOrCreatePassword("Twitch", "twitch".toCharArray());
        twitch.addURL("twitch.tv");

        Category onlineSales = manager.getOrCreateCategory("Online sales");
        onlineSales.associateWith(amazon);
        onlineSales.associateWith(alibaba);

        Category video = manager.getOrCreateCategory("Video");
        youtube.associateWith(video);
        twitch.associateWith(video);

        Category google = manager.getOrCreateCategory("Google");
        google.associateWith(gmail);
        google.associateWith(youtube);

        Password blizzard = manager.getOrCreatePassword("Blizzard", "blizzard".toCharArray());
        blizzard.addURL("blizzard.com");

        try {
            Assertions.assertDoesNotThrow(manager::save);

            manager = Assertions.assertDoesNotThrow(() -> PasswordManager.newPasswordManager(path, password));

            checkPassword(manager.getPasswordIfExists("Gmail"), "Gmail", "gmail".toCharArray(), "gmail.com");
            checkPassword(manager.getPasswordIfExists("Amazon"), "Amazon", "amazon".toCharArray(), "amazon.com");
            checkPassword(manager.getPasswordIfExists("Alibaba"), "Alibaba", "alibaba".toCharArray(), "alibaba.com");
            checkPassword(manager.getPasswordIfExists("Youtube"), "Youtube", "youtube".toCharArray(), "youtube.com");
            checkPassword(manager.getPasswordIfExists("Twitch"), "Twitch", "twitch".toCharArray(), "twitch.tv");
            checkPassword(manager.getPasswordIfExists("Blizzard"), "Blizzard", "blizzard".toCharArray(), "blizzard.com");

            checkCategory(manager.getCategoryIfExists("Online sales"), "Online sales", manager.getPasswordIfExists("Amazon"), manager.getPasswordIfExists("Alibaba"));
            checkCategory(manager.getCategoryIfExists("Google"), "Google", manager.getPasswordIfExists("Gmail"), manager.getPasswordIfExists("Youtube"));
            checkCategory(manager.getCategoryIfExists("Video"), "Video", manager.getPasswordIfExists("Youtube"), manager.getPasswordIfExists("Twitch"));

        } finally {
            try {
                Files.delete(path);
            } catch (IOException ignored) {}
        }
    }

    private void checkPassword(Password password, String name, char[] passwordArray, String... urls) {
        Assertions.assertNotNull(password);
        Assertions.assertEquals(password.getName(), name);
        assertEquals(passwordArray, password.getPassword());
        assertEquals(urls, password.getURLs());
    }

    private void checkCategory(Category category, String name, Password... passwords) {
        Assertions.assertNotNull(category);
        Assertions.assertEquals(category.getName(), name);
        assertEquals(category.getPasswords().toArray(new Password[0]), passwords);
    }

    private void assertEquals(char[] a, char[] b) {
        Assertions.assertEquals(a.length, b.length);

        for (int i = 0; i < a.length; i++) {
            Assertions.assertEquals(a[i], b[i]);
        }
    }

    private void assertEquals(Object[] a, Object[] b) {
        Assertions.assertEquals(a.length, b.length);

        for (int i = 0; i < a.length; i++) {
            Assertions.assertEquals(a[i], b[i]);
        }
    }
}