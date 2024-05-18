module com.example.agendams {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.agendams to javafx.fxml;
    exports com.example.agendams;
}