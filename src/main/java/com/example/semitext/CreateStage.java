package com.example.semitext;

import javafx.collections.ListChangeListener;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class CreateStage
{
    private final Stage stage;
    private TabPane tabPane;
    public CreateStage(Stage stage)
    {
        this.stage = stage;
    }

    public void setStage()
    {
        tabPane = new TabPane();
        CreateTab createTab = new CreateTab(tabPane);
        tabPane.getTabs().add(createTab.createNewTab(stage));

        Scene scene = createScene(createTab);
        stage.setScene(scene);
        stage.setTitle("Text Editor");
        stage.show();

        tabPane.getTabs().addListener((ListChangeListener<Tab>)change ->
        {
            if(tabPane.getTabs().isEmpty())
            {
                stage.close();
            }
        });
    }

    private Scene createScene(CreateTab createTab){
        Scene scene = new Scene(tabPane, 800, 600);

        scene.setOnKeyPressed(event -> {
            if (event.isControlDown() && event.getCode() == KeyCode.N) {
                tabPane.getTabs().add(createTab.createNewTab(stage));
                event.consume();
            }
            if (event.isControlDown() && event.isShiftDown() && event.getCode() == KeyCode.N) {
                new HelloApplication().start(new Stage());
            }
            if (event.isControlDown() && event.getCode() == KeyCode.O) {
                createTab.openFile(stage);
                event.consume();
            }
            if (event.isControlDown() && event.getCode() == KeyCode.S && !event.isShiftDown()) {
                createTab.saveFile(createTab.getFile() , stage);
                event.consume();
            }
            if (event.isControlDown() && event.isShiftDown() && event.getCode() == KeyCode.S){
                createTab.saveAsFile(stage);
                event.consume();
            }
            if (event.isControlDown() && event.getCode() == KeyCode.C){
                createTab.clearFile();
                event.consume();
            }
            if (event.getCode() == KeyCode.ESCAPE){
                System.exit(1);
            }
        });
        return scene;
    }
}
