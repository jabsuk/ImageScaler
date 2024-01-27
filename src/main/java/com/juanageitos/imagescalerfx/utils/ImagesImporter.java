package com.juanageitos.imagescalerfx.utils;

import com.juanageitos.imagescalerfx.models.ImageData;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
/**
 * Load ImageData lists from location given and check for images
 * @author juani
 * @version 1.0.0
 **/
public final class ImagesImporter {
    /**
     * Check if its an image
     * @param file
     * @return
     * @throws IOException
     */
    public static boolean isImage(File file) throws IOException {
        String mimetype = Files.probeContentType(file.toPath());
        return mimetype != null && mimetype.split("/")[0].equals("image");
    }

    /**
     * Return all images from given location
     * @param folder
     * @return
     * @throws IOException
     */
    public static List<ImageData> getImagesFrom(File folder) throws IOException {
        List<ImageData> files = new ArrayList<>();

        for (final File fileEntry : folder.listFiles()) {

            if (fileEntry.isFile() && isImage(fileEntry)) {
                files.add(new ImageData(
                        fileEntry.getName(),
                        folder.getAbsoluteFile().toPath()));
            }
        }
        return files;
    }
}
