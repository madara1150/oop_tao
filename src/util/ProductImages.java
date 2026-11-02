package util;

import java.io.File;
import java.net.URL;
import javax.swing.ImageIcon;

/**
 * Loads drink images by file name.
 *
 * Images bundled with the app live on the classpath under /products. Images
 * uploaded from the admin screen are copied to src/products at runtime, which
 * is not on the classpath until the project is rebuilt, so we fall back to the
 * file system for those.
 */
public final class ProductImages {

    public static final String UPLOAD_DIR = "src/products";
    private static final String PLACEHOLDER = "/GUI/icons/imageSoda.png";

    private ProductImages() {
    }

    public static ImageIcon load(String fileName) {
        if (fileName != null && !fileName.isBlank()) {
            URL resource = ProductImages.class.getResource("/products/" + fileName);
            if (resource != null) {
                return new ImageIcon(resource);
            }
            File uploaded = new File(UPLOAD_DIR, fileName);
            if (uploaded.isFile()) {
                return new ImageIcon(uploaded.getAbsolutePath());
            }
        }
        URL placeholder = ProductImages.class.getResource(PLACEHOLDER);
        return placeholder != null ? new ImageIcon(placeholder) : new ImageIcon();
    }
}
