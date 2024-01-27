package com.juanageitos.imagescalerfx.adapters;

import com.juanageitos.imagescalerfx.utils.IOUtils;
import com.juanageitos.imagescalerfx.models.ImageData;
import com.juanageitos.imagescalerfx.utils.ImagesImporter;
import com.juanageitos.imagescalerfx.threads.ImagesServiceLogger;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringPropertyBase;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Adapter that manages all images that can be scaled, are scaled, location where you can import images and save them.
 * @author juani
 * @version 1.0.0
 **/

public final class ImagesAdapter {

    // Services
    private static ImagesServiceLogger serviceLogger;
    // Objects list
    public static ObservableList<ImageData> scalableImages = FXCollections.observableList(new ArrayList<>());
    public static final ObservableList<ImageData> scaledImages = FXCollections.observableList(new ArrayList<>());

    // Info, settings, etc
    private static File location;
    public static String currentLocation;
    private static ReadOnlyStringProperty value;
    private static ThreadPoolExecutor executor;
    private static int threads = 1;
    private static final List<Runnable> runs = new ArrayList<>();
    private static List<Double> analysisData;
    private static boolean isRunning = false;


    /**
     * Check if its running
     * @return
     */
    public static boolean checkRunning(){
        return isRunning;
    }

    /**
     * Run all runnable from local variable, at the same time it starts the message updater thread and clears the runnable!
     */
    public static void run() {
        serviceLogger.reset();
        runs.forEach(executor::execute);
        serviceLogger.start();
        runs.clear();
    }

    /**
     * Initializes the executor
     */
    private static void createExecutor() {
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(threads);
    }

    /**
     * Return current executor
     * @return
     */
    public static ThreadPoolExecutor getExecutor() {
        return executor;
    }

    /**
     * Get the observable value of status
     * @return
     */
    public static ReadOnlyStringProperty messageProperty() {
        return value;
    }

    /**
     * Set the limit of threads and at the same time re creates executor so it uses the given threads
     * @param n_threads
     */
    public static void setThreads(int n_threads) {
        threads = n_threads;
        createExecutor();
    }

    /**
     * Returns the timelapse ms of every image
     * @return
     */
    public static double[] getRanTimeLapses() {
        double[] data = new double[analysisData.size()];
        for (int i = 0; i < analysisData.size(); i++) {
            data[i] = analysisData.get(i);
        }
        return data;
    }

    /**
     * Returns the names of the images that finished loading
     * @return
     */
    public static String[] getScalableNames() {
        return scalableImages.stream().map(ImageData::toString).toList().toArray(new String[0]);
    }

    /**
     * Sets an image as runned, it storages the image on ran images and storages how long it took
     * @param img
     * @param value
     */
    public static void setAsRan(ImageData img, double value) {
        analysisData.add(value);
        addScalableImage(img);
    }


    /**
     * Create all required values only one time, it must be ran only when application loads
     */
    public static void initialize() {
        serviceLogger = new ImagesServiceLogger();
        value = serviceLogger.messageProperty();
        analysisData = new ArrayList<>();
        initializeDirectory(new File("src/main/resources/images"));
        setThreads(Runtime.getRuntime().availableProcessors());

        serviceLogger.setDelay(Duration.millis(500)); // Will start after 0.5s
        serviceLogger.setPeriod(Duration.seconds(1)); // Runs every second after
    }


    /**
     * Add runnable to be executed when all the images have been processed
     * @param r
     */
    public static void setOnSucceeded(Runnable r){
        serviceLogger.setOnSucceeded(e->{
            if(serviceLogger.getValue()){
                isRunning = false;
                r.run();
                serviceLogger.cancel();
            }
        });
    }

    /**
     * Set directory where we are working
     * @param directory
     */
    public static void initializeDirectory(File directory) {
        location = directory;
        currentLocation = location.getPath();
    }

    /**
     * Add image that can be scaled
     * @param img
     */
    public static void addScalableImage(ImageData img) {
        scalableImages.add(img);
    }

    /**
     * Set the the scaled images, it only needs the location string path to load all the images inside
     * @param imagesFolder
     * @throws IOException
     */
    public static void setScaledImages(String imagesFolder) throws IOException {
        scaledImages.clear();
        scaledImages.addAll(ImagesImporter.getImagesFrom(new File(location + "/" + imagesFolder)));
    }

    /**
     * Get all images from the current working location
     * @return
     * @throws IOException
     */
    public static List<ImageData> getImagesFromLocation() throws IOException {
        return ImagesImporter.getImagesFrom(location);
    }

    /**
     * Clear all the last process data
     */
    public static void clear(){
        analysisData.clear();
        scalableImages = FXCollections.observableList(new ArrayList<>());
    }

    /**
     * Start processing all data
     * @throws IOException
     */
    public static void start() throws IOException {
        createExecutor();

        isRunning = true;

        for (ImageData img : getImagesFromLocation()) {
            runs.add(() -> {
                double started_nano = System.nanoTime();
                String from = img.getPath().toString() + "/" + img.getName();
                String to = img.getPath().toString() + "/" + img.getName().split("\\.")[0] + "/";
                Path pathTo = Path.of(to);

                try {
                    IOUtils.deleteDirectory(pathTo);
                } catch (IOException ignored) {}

                try {
                    Files.createDirectory(pathTo);
                } catch (IOException ignored) {}

                for (int i = 1; i < 10; i++) {
                    try {
                        IOUtils.resize(from, to + (i * 10 + "_" + img.getName()), (double) i / 10);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                Platform.runLater(() -> setAsRan(img, System.nanoTime() - started_nano));
            });
        }
        run();
    }
}
