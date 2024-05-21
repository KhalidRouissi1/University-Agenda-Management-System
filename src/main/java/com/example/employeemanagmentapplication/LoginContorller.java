package com.example.employeemanagmentapplication;

import com.example.employeemanagmentapplication.database.Database;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginContorller implements Initializable {
    private double x = 0;
    private double y = 0;

    @FXML
    private Button close;

    @FXML
    private Button loginBtn;

    @FXML
    private AnchorPane main_form;

    @FXML
    private PasswordField password;

    @FXML
    private TextField username;

    // Database stuff
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    private String teatcher_role;

    public void loginAdmin() {
        String teacherSql = "SELECT * FROM TEACHER WHERE username = ? AND password = ?";

        connect = Database.connectDb();
        try {


            prepare = connect.prepareStatement(teacherSql);
            prepare.setString(1, username.getText());
            prepare.setString(2, password.getText());
            result = prepare.executeQuery();
            if (result.next()) {
                 teatcher_role = result.getString("role");
                redirectToDashboard(false);
                return;
            }

            showAlert(Alert.AlertType.ERROR, "Error Message", "Wrong Username or Password");

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error Message", "Database Error");
        } finally {
            try {
                if (result != null) result.close();
                if (prepare != null) prepare.close();
                if (connect != null) connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void redirectToDashboard(boolean isAdmin) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
            Parent root = loader.load();
            DashboardController controller = loader.getController();
//            controller.setAdmin(isAdmin);
//            controller.setTeacherId(teacherId);

            if (teatcher_role.equals("admin")) {
                insertAdmin();
            } else {
                deleteAllAdminData();
            }

            System.out.println(teatcher_role);
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(scene);
            stage.show();
            close();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error Message", "Failed to open dashboard");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void close() {
        Stage stage = (Stage) close.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private void insertAdmin() {
        String insertAdminSql = "INSERT INTO ADMIN (name, password) VALUES ('admin', 'admin')";

        try (Connection connect = Database.connectDb();
             PreparedStatement prepare = connect.prepareStatement(insertAdminSql)) {

            prepare.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error Message", "Failed to insert admin data");
        }
    }

    private void deleteAllAdminData() {
        String deleteAdminSql = "DELETE FROM ADMIN";

        try (Connection connect = Database.connectDb();
             PreparedStatement prepare = connect.prepareStatement(deleteAdminSql)) {

            prepare.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error Message", "Failed to delete admin data");
        }
    }
}
