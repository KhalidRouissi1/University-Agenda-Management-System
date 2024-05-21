module com.example.employeemanagmentapplication {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.employeemanagmentapplication to javafx.fxml;
    exports com.example.employeemanagmentapplication;
}