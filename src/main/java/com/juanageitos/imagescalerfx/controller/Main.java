package com.juanageitos.imagescalerfx.controller;

import com.juanageitos.imagescalerfx.adapters.ImagesAdapter;
import com.juanageitos.imagescalerfx.models.ImageData;
import com.juanageitos.imagescalerfx.utils.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Objects;

/**
 * Controller of the main view, we make use of the ImagesAdapter from here with a cool layout
 * and when the adapter returns errors or messages we shows them here.
 *
 * @author juani
 * @version 1.0.0
 **/
public class Main {

    @FXML
    public ListView listScaled;
    @FXML
    public ListView listScalable;
    @FXML
    private Text txtStatusMain;
    @FXML
    private Label txtImagesLocation;
    @FXML
    private Tooltip toolImagesLocation;
    @FXML
    private ImageView imgPreview;
    @FXML
    private Button btnStart;
    @FXML
    private Button btnBrowse;
    @FXML
    private CheckBox bxModal;


    /**
     * Functions called on events
     */


    /**
     * Set modal, from checkbox
     *
     * @param event
     */
    @FXML
    protected void setModal(ActionEvent event) {
        ScenesLoader.setModal(bxModal.isSelected());
    }

    /**
     * When the view is initialize bind text, set button status, show images that can be scaled,
     * initialize the data that was already set
     */

    @FXML
    protected void initialize() {
        ImagesAdapter.setOnSucceeded(() -> setButton(false));

        this.setScalableImages();

        try {
            this.reloadLocation();
        } catch (Exception ignored) {
        }

        setButton(ImagesAdapter.checkRunning());
        ImagesAdapter.messageProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (!Objects.equals(s, "")) txtStatusMain.setText(s);
            }
        });

        bxModal.setSelected(ScenesLoader.getModal());
    }

    /**
     * Choose the path to the working directory
     *
     * @param event
     */
    @FXML
    protected void selectDirectory(ActionEvent event) {
        File directory = this.getImagesLocation(event);

        if (directory != null) {
            try {
                ImagesAdapter.initializeDirectory(directory);
            } catch (Exception e) {
                MessageUtils.showError("Error opening directory", e.toString());
            }
            this.reloadLocation();
        }
    }

    /**
     * Start the process of scale the images
     */
    @FXML
    protected void startScale() {
        setButton(true);

        ImagesAdapter.clear();
        this.setScalableImages();

        try {
            ImagesAdapter.start();
        } catch (Exception e) {
            MessageUtils.showError("Start scaling error", e.toString());
        }
    }

    /**
     * Go to Performance view with the use of ScenesLoader class
     *
     * @param event
     */
    @FXML
    protected void goCharts(ActionEvent event) {
        try {
            ScenesLoader.loadScreen("performance.fxml",
                    this.getStage(event));
        } catch (Exception e) {
            MessageUtils.showError("Error loading stage", e);
        }
    }

    /**
     * Select scalable item << LEFT LIST
     */
    @FXML
    protected void selectScalable() {
        this.selectScalable(listScalable);
    }

    /**
     * Select scaled item >> RIGHT LIST
     */
    @FXML
    protected void selectScaled() {
        this.setImgPreviewFromList(listScaled);
    }

    /**
     * Set current buttons status, if disabled, it cannot be pressed
     *
     * @param disable
     */
    protected void setButton(boolean disable) {
        btnStart.setDisable(disable);
        btnBrowse.setDisable(disable);
    }

    /**
     * Reload location label and the images scalable << LEFT LIST
     */
    private void reloadLocation() {
        this.setScalableImages();
        this.reloadLocationLabel();
    }

    /**
     * Reload left label to contain the location but only a fraction of the string, the rest display it on the
     * text displayed when the mouse if over
     */
    public void reloadLocationLabel() {
        String location = ImagesAdapter.currentLocation;
        if (location.length() > 25)
            location = "..." + location.substring(location.length() - 25, location.length() - 1);
        txtImagesLocation.setText("Images: " + location);
        toolImagesLocation.setText(ImagesAdapter.currentLocation);
    }

    /**
     * Return stage of current view
     *
     * @param event
     * @return
     */
    public Stage getStage(ActionEvent event) {
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }

    /**
     * Show dialog to make the user select the working directory
     *
     * @param event
     * @return
     */
    public File getImagesLocation(ActionEvent event) {
        DirectoryChooser dir = new DirectoryChooser();
        dir.setTitle("Select folder");
        File file;
        while (true) {
            file = dir.showDialog(this.getStage(event));
            if (file == null || file.isDirectory()) break;
            MessageUtils.showMessage("Wrong directory", "You can only select directories!");
        }
        return file;
    }

    /**
     * Set the observable list of the scalable list << LEFT LIST
     */
    public void setScalableImages() {
        listScalable.setItems(ImagesAdapter.scalableImages);
    }

    /**
     * Set the observable list of the scaled list >> RIGHT LIST
     */
    public void setScaledImages() {
        listScaled.setItems(ImagesAdapter.scaledImages);
    }


    /**
     * Sets the current image being displayed
     *
     * @param image
     */
    protected void setImagePreview(ImageData image) {
        try {
            this.imgPreview.setImage(new Image(image.getPath().toString() + "/" + image.getName()));
        } catch (Exception e) {
            MessageUtils.showError("Error image", "Error while loading image: " + e);
        }
    }

    /**
     * With the list, get selected and call setImagePreview
     *
     * @param list
     */
    private void setImgPreviewFromList(ListView list) {
        Object selected = list.getSelectionModel().getSelectedItem();

        if (selected != null) {
            this.setImagePreview((ImageData) selected);
        }
    }

    /**
     * With the list, get selected and set the scaled items on the right list
     *
     * @param list
     */
    private void selectScalable(ListView list) {
        Object selected = list.getSelectionModel().getSelectedItem();

        if (selected != null) {
            try {
                ImagesAdapter.setScaledImages(((ImageData) selected).getName().split("\\.")[0]);
            } catch (Exception e) {
                MessageUtils.showError("Select Scalable error", e.toString());
            }

            this.setScaledImages();
        }
    }


}