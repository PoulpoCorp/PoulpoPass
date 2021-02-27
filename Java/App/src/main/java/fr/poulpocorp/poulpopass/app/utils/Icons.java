package fr.poulpocorp.poulpopass.app.utils;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;

public class Icons {

    public static final FlatSVGIcon PASSWORD = new FlatSVGIcon("password_icon.svg");

    public static final FlatSVGIcon CATEGORY = new FlatSVGIcon("category_icon.svg");

    public static final FlatSVGIcon WEBSITE = new FlatSVGIcon("website_icon.svg");

    private static final HashMap<String, ImageIcon> ICONS = new HashMap<>();

    /**
     * Fetch favicon of the specified website
     */
    public static Icon fetch(String url, int size) {
        if (ICONS.containsKey(url)) {
            return ICONS.get(url);
        } else {
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

                InputStream is = new URL(link).openStream();
                BufferedImage image = Imaging.getBufferedImage(is);
                is.close();

                if (size > 0) {
                    image = toBufferedImage(image.getScaledInstance(size, size, Image.SCALE_SMOOTH));
                }

                ImageIcon icon = new ImageIcon(image);

                ICONS.put(url, icon);

                return icon;
            } catch (IOException | ImageReadException e) {
                e.printStackTrace();

                return null;
            }
        }
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

    public static BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }

        BufferedImage bi = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics2D = bi.createGraphics();
        graphics2D.drawImage(image, 0, 0, null);
        graphics2D.dispose();

        return bi;
    }
}