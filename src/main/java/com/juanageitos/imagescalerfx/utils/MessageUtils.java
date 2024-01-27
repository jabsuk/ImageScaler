package com.juanageitos.imagescalerfx.utils;

import javafx.scene.control.Alert;
/**
 * Display messages as info or errors using javafx alerts
 * @author juani
 * @version 1.0.0
 **/
public final class MessageUtils {
    /**
     * Shows alert generic with type given, title header and content
     * @param type
     * @param title
     * @param header
     * @param content
     */
    public static void showAlert(Alert.AlertType type, String title, String header, String content){
        System.out.println("["+type+"] "+header + ":::" + content);
        Alert alert = new Alert(type);
        alert.setTitle(title);

        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Shows error on alert window
     * @param header
     * @param message
     */
    public static void showError(String header, String message){
        showAlert(Alert.AlertType.ERROR, header, header, message);
    }

    /**
     * Shows error with current exception
     * @param header
     * @param e
     */
    public static void showError(String header, Exception e){
        e.printStackTrace();
        showAlert(Alert.AlertType.ERROR, header, header, e.toString());
    }

    /**
     * Show message as info
     * @param header
     * @param message
     */
    public static void showMessage(String header, String message){
        showAlert(Alert.AlertType.INFORMATION, header, header, message);
    }
}
