package com.example.semitext;

import javafx.application.Application;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage){
        CreateStage createStage = new CreateStage(stage);
        createStage.setStage();
    }

    public static void main(String[] args){
        launch();
    }
}