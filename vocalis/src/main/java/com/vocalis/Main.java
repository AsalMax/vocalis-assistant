package com.vocalis;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Assistant assistant = new Assistant();
        assistant.startMorningRoutine();
        TrayManager.createTrayIcon(assistant);
    }
}