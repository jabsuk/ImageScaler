package com.juanageitos.imagescalerfx;

import com.juanageitos.imagescalerfx.adapters.ImagesAdapter;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
/**
 * This application manages images loaded from default or custom location,
 * then you can start resizing each one of them. All the operations
 * are made with threads, when one is finished it reload the list on the
 * layout, so you can see progress and be able to select the finished images.
 * Also, you can view the time that took for every image.
 *
 * @author juani
 * @version 1.0.0
 **/
public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        ImagesAdapter.initialize();

        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 852, 427);
        stage.setTitle("ImageScalerFx");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}