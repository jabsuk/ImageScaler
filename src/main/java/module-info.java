module com.juanageitos.imagescalerfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.juanageitos.imagescalerfx to javafx.fxml;
    exports com.juanageitos.imagescalerfx;
    exports com.juanageitos.imagescalerfx.controller;
    opens com.juanageitos.imagescalerfx.controller to javafx.fxml;
}