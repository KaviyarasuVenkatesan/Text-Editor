package com.example.semitext;

import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.HashMap;
import java.util.Optional;

// creating tabs for the tab pane in stage
public class CreateTab {

    private final HashMap<Tab, TextArea> TabAndTextAreaMap = new HashMap<>();

    private final HashMap<Tab, File> TabAndFileMap = new HashMap<>();

    private final FileChooser fileChooser = new FileChooser();

    private File file;

    private final TabPane tabpane;

    public CreateTab(TabPane tabPane)
    {
        this.tabpane = tabPane;
    }

    public File getFile() {
        return file;
    }

    // creating tab contains border pane
    // In border pane textarea at center and menubar at top
    // menubar contains menu
    public Tab createNewTab(Stage stage) {
        BorderPane tabContent = new BorderPane();
        TextArea textarea = new TextArea();

        Tab newTab = new Tab("untitled", tabContent);

        TabAndTextAreaMap.put(newTab , textarea);

        MenuBar menubar = new MenuBar();
        Menu menu = new Menu("file");

        // preparing menu items
        prepareNewTabFile(menu , stage);
        prepareNewWindowFile(menu);
        prepareOpenFile(menu , stage);
        prepareSaveFile(menu , stage);
        prepareSaveAsFile(menu , stage);
        prepareClearFile(menu);
        prepareExitFile(menu);

        menubar.getMenus().addAll(menu);

        tabContent.setCenter(textarea);
        tabContent.setTop(menubar);

        return newTab;

    }

    // preparing new tab menu item
    private void prepareNewTabFile(Menu menu, Stage stage) {
        MenuItem newTabMenu = new MenuItem("New Tab           Control+N");
        newTabMenu.setOnAction(actionEvent -> {
            Tab newTab = createNewTab(stage);
            tabpane.getTabs().add(newTab);
            newTab.setText("untitled");
        });
        menu.getItems().add(newTabMenu);
    }

    // preparing new window menu item
    private void prepareNewWindowFile(Menu menu) {
        MenuItem newWindowMenu = new MenuItem("New Window      Control+Shift+N");
        newWindowMenu.setOnAction(actionEvent -> {
            try {
                new HelloApplication().start(new Stage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        menu.getItems().add(newWindowMenu);
    }

    //preparing open file menu item
    private void prepareOpenFile(Menu menu , Stage stage){
        MenuItem openMenu = new MenuItem("Open                Control+O");
        openMenu.setOnAction(actionEvent -> openFile(stage));
        menu.getItems().add(openMenu);
    }

    //preparing save file menu item
    private void prepareSaveFile(Menu menu , Stage stage){
        MenuItem saveMenu = new MenuItem("Save                 Control+S");
        saveMenu.setOnAction(actionEvent -> saveFile(file , stage));
        menu.getItems().add(saveMenu);
    }

    //preparing saveAs file menu item
    private void prepareSaveAsFile(Menu menu , Stage stage) {
        MenuItem saveAsMenu = new MenuItem("Save As             Control+Shift+S");
        saveAsMenu.setOnAction(actionEvent -> saveAsFile(stage));
        menu.getItems().add(saveAsMenu);
    }

    // preparing clear menu item
    // we select the tab to clear the textarea
    private void prepareClearFile(Menu menu) {
        MenuItem clearMenu = new MenuItem("Clear                  Control+C");
        clearMenu.setOnAction(actionEvent -> clearFile());
        menu.getItems().add(clearMenu);
    }

    // preparing exit menu item
    private void prepareExitFile(Menu menu){
        MenuItem exitMenu = new MenuItem("Exit                      ESC");
        exitMenu.setOnAction(actionEvent -> exit());
        menu.getItems().addAll(new SeparatorMenuItem() , exitMenu);
    }

    // open file menu item's function
    public void openFile(Stage stage){

        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        this.file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                StringBuilder sc = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    sc.append(line).append("\n");
                }
                Tab newTab = createNewTab(stage);
                TextArea newTextArea = TabAndTextAreaMap.get(newTab);
                newTextArea.setText(sc.toString());
                newTab.setText(file.getName());

                tabpane.getTabs().add(newTab);
                TabAndFileMap.put(newTab, file);

                tabpane.getSelectionModel().select(newTab);

            } catch (IOException e) {
                e.getStackTrace();
            }
        }
    }

    // save file menu item's function
    public void saveFile(File file , Stage stage) {
        Tab checker = tabpane.getSelectionModel().getSelectedItem();
        TextArea checkerTextArea = TabAndTextAreaMap.get(checker);
        File checkerMatchingFile = TabAndFileMap.get(checker);
        if (checkerMatchingFile == null) {
            if ( checkerTextArea.getText().isEmpty()) {
                showPopupAlert(stage);
            }
            else {
                saveAsFile(stage);
            }
        }
        if (file != null) {
            try {
                Tab selectedTab = tabpane.getSelectionModel().getSelectedItem();
                TextArea selectedTextArea = TabAndTextAreaMap.get(selectedTab);
                File matchingfile = TabAndFileMap.get(selectedTab);
                BufferedWriter bw = new BufferedWriter(new FileWriter(matchingfile));
                bw.write(selectedTextArea.getText());
                bw.close();
            } catch (IOException e) {
                e.getStackTrace();
            }
        }
    }

    // saveAs file menu item's function
    public void saveAsFile(Stage stage) {

        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try {
                Tab selectedTab = tabpane.getSelectionModel().getSelectedItem();
                TextArea selectedTextArea = TabAndTextAreaMap.get(selectedTab);
                BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                bw.write(selectedTextArea.getText());
                bw.close();
                TabAndFileMap.put(selectedTab, file);
            } catch (IOException ignored) {
            }
        }
    }

    // clear menu item's function
    public void clearFile(){
        Tab selectedTab = tabpane.getSelectionModel().getSelectedItem();
        TextArea selectedTextArea = TabAndTextAreaMap.get(selectedTab);
        selectedTextArea.clear();
    }

    // exit menu item's function
    public void exit()
    {
        System.exit(0);
    }

    // preparing Alert method
    private Alert showAlert(Alert.AlertType type , String title , String header , String content)
    {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert;
    }

    // preparing PopupAlert message for save an empty file
    private void showPopupAlert(Stage stage) {

        Alert alert = showAlert(Alert.AlertType.CONFIRMATION , "Empty File" , null , "Do you want to save an Empty File");

        ButtonType buttonTypeOK = new ButtonType("OK");
        ButtonType buttonTypeCancel = new ButtonType("Cancel");

        alert.getButtonTypes().setAll(buttonTypeOK, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == buttonTypeOK) {
            saveAsFile(stage);
        }
    }
}
