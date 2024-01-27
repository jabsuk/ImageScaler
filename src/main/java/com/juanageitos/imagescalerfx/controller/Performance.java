package com.juanageitos.imagescalerfx.controller;

import com.juanageitos.imagescalerfx.models.ImageData;
import com.juanageitos.imagescalerfx.adapters.ImagesAdapter;
import com.juanageitos.imagescalerfx.utils.ScenesLoader;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * Controller of the threads performance view.
 * Here we load the existing values and reload them with events from observable lists.
 * @author juani
 * @version 1.0.0
 **/

public class Performance {
    @FXML
    private BarChart<String, Number> barChart;
    @FXML
    private Text txtStatus;

    private XYChart.Series barData = new XYChart.Series();

    /**
     * Function called on fxml event pressing a button
     * Returns to main view suing ScenesLoader class
     * @param event
     * @throws IOException
     */

    @FXML
    protected void goMain(ActionEvent event) throws IOException {
        ScenesLoader.loadScreen("main.fxml",
                (Stage)((Node) event.getSource()).getScene().getWindow());
    }

    /**
     * Create values when the view is created
     */

    @FXML
    protected void initialize(){
        barData.getData().clear();

        this.setupData(ImagesAdapter.getScalableNames(), ImagesAdapter.getRanTimeLapses());
        barChart.getData().add(barData);


        /**
         * This block of code lets us see data on real time
         */

        ImagesAdapter.scalableImages.addListener(new ListChangeListener<ImageData>() {
            @Override
            public void onChanged(javafx.collections.ListChangeListener.Change<? extends ImageData> c) {
                c.next();
                Platform.runLater(()->{
                    if(ScenesLoader.isCurrent("performance.fxml"))addData(ImagesAdapter.getScalableNames()[c.getFrom()], ImagesAdapter.getRanTimeLapses()[c.getFrom()]);
                });
            }
        });

        ImagesAdapter.messageProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(!Objects.equals(s, "")) txtStatus.setText(s);
            }
        });
    }


    /**
     * Add new column data to the Bar Chart
     * @param fileName
     * @param ms
     */

    private void addData(String fileName, double ms){
        try{
            barData.getData().add(
                new XYChart.Data(
                    fileName, ms
                )
            );
        }catch(Exception ignored){}

    }

    /**
     * Setup all data given
     * @param fileNames
     * @param ms
     */
    private void setupData(String[] fileNames, double[] ms){
        for (int i = 0; i<fileNames.length; i++) {
            addData(fileNames[i], ms[i]);
        }
    }
}
