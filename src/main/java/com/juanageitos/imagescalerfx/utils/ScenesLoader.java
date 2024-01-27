package com.juanageitos.imagescalerfx.utils;

import com.juanageitos.imagescalerfx.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
/**
 * Control stages from the main application, you can change the way the stages are loaded.
 * It only works on this exercise!
 * @author juani
 * @version 1.0.0
 **/
public final class ScenesLoader {
    private static String currentPath = "main.fxml";
    private static boolean MODAL = true;

    /**
     * Set mode of the scenes creation
     * @param state
     */
    public static void setModal(boolean state){
        MODAL = state;
    }

    /**
     * Get the mode set
     * @return
     */
    public static boolean getModal(){return MODAL;}

    /**
     * Check if current stage fxml file is the one given on param
     * @param fxmlFrom
     * @return
     */
    public static boolean isCurrent(String fxmlFrom){
        return Objects.equals(fxmlFrom, currentPath);
    }

    /**
     * It creates a new stage or reuses the one given, it depends on the mode
     * @param stage
     * @return
     */
    private static Stage getStage(Stage stage){
        if(!MODAL) {return stage;}

        Stage newStage = new Stage();
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.initOwner(stage);
        return newStage;
    }

    /**
     * Load fxml view
     * @param viewPath
     * @param stage
     * @throws IOException
     */
    private static void loadNewScreen(String viewPath, Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Application.class.getResource(viewPath));
        Scene viewScene = new Scene(root);
        currentPath = viewPath;

        Stage stageGotten = getStage(stage);

        stageGotten.setScene(viewScene);
        stageGotten.show();
    }

    /**
     * Close current stage
     * @param stage
     */
    private static void close(Stage stage){
        stage.close();
    }

    /**
     * Load screen, if the one which is trying to go is main, and the mode is modal close the window, else, load new screen
     * @param viewPath
     * @param stage
     * @throws IOException
     */
    public static void loadScreen(String viewPath, Stage stage) throws IOException
    {
        if(MODAL && Objects.equals(viewPath, "main.fxml")){
            close(stage);
        }else{
            loadNewScreen(viewPath, stage);
        }
    }
}
