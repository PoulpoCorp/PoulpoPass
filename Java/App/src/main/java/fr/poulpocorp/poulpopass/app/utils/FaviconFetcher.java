package fr.poulpocorp.poulpopass.app.utils;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.ImagingConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class FaviconFetcher {

    private static final Logger LOGGER = LogManager.getLogger(FaviconFetcher.class);

    private static final ThreadPoolExecutor executor = new ThreadPoolExecutor(8, 8, 1L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), new NamedThreadFactory("FaviconFetcher"));
    private static final HashMap<String, Icon> ICONS = new HashMap<>();

    public static final Path ICON_CACHE_PATH = Cache.of("icons");

    public static void fetch(String url, int size, Consumer<Icon> setter) {
        if (ICONS.containsKey(url)) {
            runSetter(setter, ICONS.get(url));

            return;
        }

        executor.submit(() -> {
            BufferedImage image = getCachedIcon(url);

            if (image != null) {
                runSetter(setter, new ImageIcon(image));

                LOGGER.info("Checking for icon update for {}", url);
            }

            // Check for icon update
            image = fetch(url, size);

            Icon icon = image == null ? Icons.WEBSITE : new ImageIcon(image);
            runSetter(setter, icon);
            ICONS.put(url, icon);

            if (image != null) {
                cacheImage(image, url);
            }
        });
    }

    private static void runSetter(Consumer<Icon> setter, Icon icon) {
        EventQueue.invokeLater(() -> { // run in swing thread
            setter.accept(icon);
        });
    }

    private static BufferedImage fetch(String url, int size) {
        LOGGER.info("Fetching favicon at {}", url);

        BufferedImage image;

        try {
            Document document = Jsoup.connect(url).get();

            String link = findIconLinkInDocument(document);

            if (link == null) {
                link = getFaviconURL(url);

                if (link == null) {
                    return null;
                }
            }

            if (link.startsWith("//")) {
                link = "https:" + link;
            }

            String fileName = link.substring(link.lastIndexOf('/') + 1);

            Map<String, Object> params = new HashMap<>();
            params.put(ImagingConstants.PARAM_KEY_FILENAME, fileName);

            InputStream is = new URL(link).openStream();
            image = Imaging.getBufferedImage(is, params);
            is.close();

            if (size > 0) {
                image = toBufferedImage(image.getScaledInstance(size, size, Image.SCALE_SMOOTH));
            }
        } catch (IOException e) {
            LOGGER.warn("Failed to get favicon", e);

            image = null;
        } catch (ImageReadException e) {
            LOGGER.warn("Cannot read favicon", e);

            image = null;
        }

        return image;
    }

    private static String getFaviconURL(String url) {
        try {
            URI uri = new URI(url);

            String scheme = uri.getScheme();
            if (uri.getScheme() == null) {
                scheme = "https";
            }

            return String.format("%s://%s/favicon.ico", scheme, uri.getHost());
        } catch (URISyntaxException e) {
            LOGGER.info("User url is probably incorrect", e);

            return null;
        }
    }

    private static String findIconLinkInDocument(Document document) {
        Element element = document.select("link[href][rel=\"icon\"]").first();

        String link;
        if (element == null) {
            element = document.select("link[href][rel=\"shortcut icon\"]").first();

            if (element == null) {
                return null;
            } else {
                link = element.attr("href");
            }
        } else {
            link = element.attr("href");
        }

        return link;
    }

    private static BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }

        BufferedImage bi = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics2D = bi.createGraphics();
        graphics2D.drawImage(image, 0, 0, null);
        graphics2D.dispose();

        return bi;
    }

    private static BufferedImage getCachedIcon(String url) {
        String file = StringUtil.toHexString(url.getBytes(StandardCharsets.UTF_8));

        Path path = ICON_CACHE_PATH.resolve(file);

        if (Files.exists(path)) {
            LOGGER.info("Reading icon ({}) from cache: {}", url, file);

            try {
                return ImageIO.read(path.toFile());
            } catch (IOException e) {
                LOGGER.info("Failed to get cached icon");
            }
        }

        return null;
    }


    private static void cacheImage(BufferedImage image, String url) {
        String hex = StringUtil.toHexString(url.getBytes(StandardCharsets.UTF_8));

        LOGGER.info("Caching image ({}) at icons/{}", url, hex);

        try {
            if (!Files.exists(ICON_CACHE_PATH)) {
                Files.createDirectories(ICON_CACHE_PATH);
            }

            ImageIO.write(image, "png", Cache.of("icons/" + hex).toFile());
        } catch (IOException e) {
            LOGGER.warn("Failed to cache icon image ({}) to icons/{}", url, hex, e);
        }
    }
}