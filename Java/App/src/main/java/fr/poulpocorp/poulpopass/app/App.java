package fr.poulpocorp.poulpopass.app;

import fr.poulpocorp.poulpopass.app.viewer.PasswordExplorer;
import fr.poulpocorp.poulpopass.core.Category;
import fr.poulpocorp.poulpopass.core.Password;
import fr.poulpocorp.poulpopass.core.PasswordManager;

import javax.swing.*;
import java.nio.file.Path;

public class App extends JFrame  {

    public App() {
        super("PoulpoPass");

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initComponents();

        pack();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        char[] password = "Hello world".toCharArray();
        Path path = Path.of("test.pass");

        PasswordManager manager = new PasswordManager(password);
        manager.setPath(path);

        Password gmail = manager.getOrCreatePassword("Gmail", "gmail".toCharArray());
        gmail.addURL("https://www.gmail.com");

        Password amazon = manager.getOrCreatePassword("Amazon", "amazon".toCharArray());
        amazon.addURL("https://amazon.com/truck");
        Password alibaba = manager.getOrCreatePassword("Alibaba", "alibaba".toCharArray());
        alibaba.addURL("https://alibaba.com");

        Password youtube = manager.getOrCreatePassword("Youtube", "youtube".toCharArray());
        youtube.addURL("https://youtube.com");
        Password twitch = manager.getOrCreatePassword("Twitch", "twitch".toCharArray());
        twitch.addURL("https://twitch.tv");

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
        blizzard.addURL("https://blizzard.com");

        Password openGL = manager.getOrCreatePassword("OpenGL tutorial", "opengl tutorial".toCharArray());
        openGL.addURL("http://www.opengl-tutorial.org/intermediate-tutorials/tutorial-16-shadow-mapping/");

        PasswordExplorer explorer = new PasswordExplorer(manager);
        explorer.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        add(explorer);
    }
}