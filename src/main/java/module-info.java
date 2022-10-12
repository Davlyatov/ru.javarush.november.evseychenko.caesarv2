module com.example.ceasarv2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.ceasarv2 to javafx.fxml;
    exports com.example.ceasarv2;
}