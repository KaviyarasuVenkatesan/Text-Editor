module com.example.semitext {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.semitext to javafx.fxml;
    exports com.example.semitext;
}