package com.juanageitos.imagescalerfx.models;

import java.nio.file.Path;
/**
 * Model class of an image, it contains the name and the full path.
 * @author juani
 * @version 1.0.0
 **/
public class ImageData {
    private final String name;
    private final Path path;


    /**
     * Return current name, it also returns the extension!
     * @return
     */
    public String getName(){return this.name;}

    /**
     * Return current path
     * @return
     */
    public Path getPath(){return this.path;}

    /**
     * Constructor
     * @param name
     * @param path
     */
    public ImageData(String name, Path path) {
        this.name = name;
        this.path = path;
    }

    /**
     * Return name as toString
     * @return
     */
    public String toString(){
        return this.getName();
    }
}
