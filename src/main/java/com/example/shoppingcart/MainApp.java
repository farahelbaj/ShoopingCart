package com.example.shoppingcart;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.NodeOrientation;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Locale locale = Locale.US;
        ResourceBundle bundle = ResourceBundle.getBundle("i18n.MessagesBundle", locale);

        URL fxmlLocation = getClass().getResource("/com/example/shoppingcart/main-view.fxml");

        FXMLLoader loader = new FXMLLoader(fxmlLocation, bundle);
        Scene scene = new Scene(loader.load(), 700, 500);

        if ("ar".equals(locale.getLanguage())) {
            scene.getRoot().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        } else {
            scene.getRoot().setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        }

        stage.setTitle("Farah - Shopping Cart");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}