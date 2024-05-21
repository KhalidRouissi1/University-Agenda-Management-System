package com.example.employeemanagmentapplication;

import com.example.employeemanagmentapplication.database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.*;
import java.util.Date;

public class DashboardController implements Initializable {

    @FXML
    private TextField addEmpoyee_Search;

    @FXML
    private Button addEmpoyee_addBtn;

    @FXML
    private Button addEmpoyee_clearBtn;

    @FXML
    private TableColumn<employeeData, Integer> addEmpoyee_col_EmployeeId;

    @FXML
    private TableColumn<employeeData, String> addEmpoyee_col_userName;
    @FXML
    private TableColumn<employeeData, String> addEmpoyee_col_Password;
    @FXML
    private TableColumn<employeeData, String> addEmpoyee_col_LastName;

    @FXML
    private TableColumn<employeeData, Date> addEmpoyee_col_Calanders;

    @FXML
    private TableColumn<employeeData, String> addEmpoyee_col_firstName;

    @FXML
    private TableColumn<employeeData, String> addEmpoyee_col_gender;

    @FXML
    private ImageView addEmpoyee_col_image;

    @FXML
    private TableColumn<employeeData, Integer> addEmpoyee_col_phone;

    @FXML
    private TableColumn<employeeData, Integer> addEmpoyee_col_position;

    @FXML
    private Button addEmpoyee_deleteBtn;

    @FXML
    private TextField addEmpoyee_employeeId;

    @FXML
    private TextField addEmpoyee_firstName;
    @FXML
    private TextField addEmpoyee_email;

    @FXML
    private TextField addEmpoyee_userName;
    @FXML
    private TextField addEmpoyee_password;

    @FXML
    private AnchorPane addEmpoyee_form;

    @FXML
    private ComboBox<String> addEmpoyee_gender;

    @FXML
    private Button addEmpoyee_importBtn;

    @FXML
    private TextField addEmpoyee_lastName;

    @FXML
    private TextField addEmpoyee_phone;

    @FXML
    private ComboBox<String> addEmpoyee_position;

    @FXML
    private TableView<employeeData> addEmpoyee_tableView;

    @FXML
    private Button addEmpoyee_updateBtn;

    @FXML
    private Button add_employee_btn;

    @FXML
    private Button home_btn;

    @FXML
    private BarChart<?, ?> home_chart;

    @FXML
    private AnchorPane home_form;

    @FXML
    private Label home_totalEmployee;

    @FXML
    private Label home_totalInactiveEmploye;

    @FXML
    private Label home_totalPresents;

    @FXML
    private Button logout;
    @FXML
    private Button addEmpoyee_clalanderBtn;
    @FXML
    private AnchorPane main_form;

    @FXML
    private Label salary_LastName;

    @FXML
    private Button calandar_btn;

    @FXML
    private Button salary_clearBtn;

    @FXML
    private TableColumn<?, ?> salary_col_employeeId;

    @FXML
    private TableColumn<?, ?> salary_col_firstName;

    @FXML
    private TableColumn<?, ?> salary_col_lastName;

    @FXML
    private TableColumn<?, ?> salary_col_postion;

    @FXML
    private TableColumn<?, ?> salary_col_salary;

    @FXML
    private TextField salary_employeeId;

    @FXML
    private Label salary_firstName;

    @FXML
    private AnchorPane salary_form;

    @FXML
    private Label salary_position;

    @FXML
    private TextField salary_salary;

    @FXML
    private Button salary_updateBtn;

    @FXML
    private Label username;

    @FXML
    void close(ActionEvent event) {

    }

    @FXML
    void minimize(ActionEvent event) {

    }
    public void setTeacherId(int teacherId) {
        this.teatcherId = 0;
    }
    private boolean admin;

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean getIsAdmin() {
        return admin;
    }
    private int teatcherId = 0;


    public int getTeatcherId() {
        return teatcherId;
    }

    private Connection connect;
    private Statement statement;
    private PreparedStatement prepare;
    private ResultSet result;
    private Image image;

    public void addEmployeeInsertImage() {
        FileChooser open = new FileChooser();
        File file = open.showOpenDialog(main_form.getScene().getWindow());
        if (file != null) {
            getData.path = file.getAbsolutePath();
            image = new Image(file.toURI().toString(), 114, 141, false, true);
            addEmpoyee_col_image.setImage(image);
        }
    }

    private String[] positionList = { "Prof1","Prof2","Prof3"};

    public void addEmployeePositionList() {
        List<String> listP = new ArrayList<>();

        for (String data : positionList) {
            listP.add(data);
        }

        ObservableList<String> listData = FXCollections.observableArrayList(listP);
        addEmpoyee_position.setItems(listData);
    }

    private String[] listGender = {"Male", "Female"};

    public void addEmployeeGendernList() {
        List<String> listG = new ArrayList<>();

        for (String data : listGender) {
            listG.add(data);
        }

        ObservableList<String> listData = FXCollections.observableArrayList(listG);
        addEmpoyee_gender.setItems(listData);
    }

    public void addEmployeeAdd() {
        try {
            int employeeId = Integer.parseInt(addEmpoyee_employeeId.getText());
            String firstName = addEmpoyee_firstName.getText();
            String lastName = addEmpoyee_lastName.getText();
            String phone = addEmpoyee_phone.getText();
            String imagePath = getData.path;
            String gender = addEmpoyee_gender.getSelectionModel().getSelectedItem();
            String position = addEmpoyee_position.getSelectionModel().getSelectedItem();
            String email = addEmpoyee_email.getText();
            String username = addEmpoyee_userName.getText();
            String password = addEmpoyee_password.getText();

            if (employeeId <= 0 || firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty() || gender == null || position == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please fill in all the required fields.");
                alert.showAndWait();
                return;
            }


            String sql = "INSERT INTO teacher (teacher_id, firstname, lastname, gender, phonenum, position, email, username, password, image) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = connect.prepareStatement(sql);
            preparedStatement.setInt(1, employeeId);
            preparedStatement.setString(2, firstName);
            preparedStatement.setString(3, lastName);
            preparedStatement.setString(4, gender);
            preparedStatement.setString(5, phone);
            preparedStatement.setString(6, position);
            preparedStatement.setString(7, email);
            preparedStatement.setString(8, username);
            preparedStatement.setString(9, password);
            preparedStatement.setString(10, imagePath);

            preparedStatement.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Employee added successfully!");
            alert.showAndWait();

            addShowEmployee();
            addEmployeeReset();

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a valid employee ID.");
            alert.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addEmployeeUpdate() {
        String uri = getData.path;
        if (uri != null) {
            uri = uri.replace("\\", "\\\\");
        }



        String sql = "UPDATE teacher SET firstName = ?, lastName = ?, gender = ?, phoneNum = ?, position = ?, image = ?, username = ?,password=?,email=? WHERE teacher_id = ?";

        try (Connection connection = Database.connectDb()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, addEmpoyee_firstName.getText());
            preparedStatement.setString(2, addEmpoyee_lastName.getText());
            preparedStatement.setString(3, addEmpoyee_gender.getSelectionModel().getSelectedItem());
            preparedStatement.setString(4, addEmpoyee_phone.getText());
            preparedStatement.setString(5, addEmpoyee_position.getSelectionModel().getSelectedItem());
            preparedStatement.setString(6, uri);
            preparedStatement.setString(7, addEmpoyee_userName.getText());
            preparedStatement.setString(8, addEmpoyee_password.getText());
            preparedStatement.setString(9, addEmpoyee_email.getText());
            preparedStatement.setInt(10, Integer.parseInt(addEmpoyee_employeeId.getText()));
            Alert alert;
            if (addEmpoyee_employeeId.getText().isEmpty() || addEmpoyee_firstName.getText().isEmpty() || addEmpoyee_lastName.getText().isEmpty()
                    || addEmpoyee_gender.getSelectionModel().getSelectedItem() == null || addEmpoyee_phone.getText().isEmpty()
                    || addEmpoyee_position.getSelectionModel().getSelectedItem() == null || getData.path == null || getData.path.isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
            } else {
                preparedStatement.executeUpdate();
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Successfully Updated!");
            }
            alert.showAndWait();

            addShowEmployee();
            addEmployeeReset();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addEmployeeDelete() {
        String sql = "DELETE FROM teacher WHERE teacher_id = ?";

        try (Connection connection = Database.connectDb()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, Integer.parseInt(addEmpoyee_employeeId.getText()));

            Alert alert;
            if (addEmpoyee_employeeId.getText().isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please enter an employee ID to delete.");
            } else {
                preparedStatement.executeUpdate();
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Employee deleted successfully!");
            }
            alert.showAndWait();

            addShowEmployee();
            addEmployeeReset();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addEmployeeReset() {
        addEmpoyee_employeeId.setText("");
        addEmpoyee_firstName.setText("");
        addEmpoyee_lastName.setText("");
        addEmpoyee_gender.getSelectionModel().clearSelection();
        addEmpoyee_phone.setText("");
        addEmpoyee_userName.setText("");
        addEmpoyee_password.setText("");
        addEmpoyee_email.setText("");
        addEmpoyee_position.getSelectionModel().clearSelection();
        addEmpoyee_col_image.setImage(null);
        getData.path = "";
    }

    public ObservableList<employeeData> addEmployeeListData() {
        ObservableList<employeeData> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM teacher";

        connect = Database.connectDb();
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            employeeData employeeD;
            while (result.next()) {
                employeeD = new employeeData(
                        result.getInt("teacher_id"),
                        result.getString("firstname"),
                        result.getString("lastname"),
                        result.getString("gender"),
                        result.getInt("phonenum"),
                        result.getString("position"),
                        result.getString("image"),
                        result.getString("username"),
                        result.getString("email"),
                        result.getString("password")
                );
                listData.add(employeeD);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    private ObservableList<employeeData> addEmployeeList;

    public void addShowEmployee() {
        addEmployeeList = addEmployeeListData();
        addEmpoyee_col_EmployeeId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        addEmpoyee_col_firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        addEmpoyee_col_LastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        addEmpoyee_col_gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        addEmpoyee_col_phone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        addEmpoyee_col_position.setCellValueFactory(new PropertyValueFactory<>("position"));
        //        addEmpoyee_col_Calanders.setCellValueFactory(new PropertyValueFactory<>("date"));
        addEmpoyee_tableView.setItems(addEmployeeList);
    }

    public void addEmployeeSelect() {
        employeeData employeeD = addEmpoyee_tableView.getSelectionModel().getSelectedItem();
        int num = addEmpoyee_tableView.getSelectionModel().getSelectedIndex();

        if (num < 0) {
            return;
        }
        addEmpoyee_employeeId.setText(String.valueOf(employeeD.getEmployeeId()));
        addEmpoyee_firstName.setText(String.valueOf(employeeD.getFirstName()));
        addEmpoyee_lastName.setText(String.valueOf(employeeD.getLastName()));
        addEmpoyee_phone.setText(String.valueOf(employeeD.getPhoneNumber()));
        addEmpoyee_lastName.setText(String.valueOf(employeeD.getLastName()));
        addEmpoyee_lastName.setText(String.valueOf(employeeD.getLastName()));
        addEmpoyee_lastName.setText(String.valueOf(employeeD.getLastName()));
        addEmpoyee_email.setText(String.valueOf(employeeD.getEmail()));
        addEmpoyee_userName.setText(String.valueOf(employeeD.getUserName()));
        addEmpoyee_password.setText(String.valueOf(employeeD.getPassword()));

        String uri = "file:" + employeeD.getImage();
        image = new Image(uri, 114, 141, false, true);
        addEmpoyee_col_image.setImage(image);
    }


    public void switchForm(ActionEvent event) throws IOException {

        if (event.getSource() == home_btn) {
            home_form.setVisible(true);
            addEmpoyee_form.setVisible(false);
            salary_form.setVisible(false);

            home_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #3a4368, #28966c);");
            add_employee_btn.setStyle("-fx-background-color:transparent");
            calandar_btn.setStyle("-fx-background-color:transparent");



        } else if (event.getSource() == add_employee_btn) {
            home_form.setVisible(false);
            addEmpoyee_form.setVisible(true);
            salary_form.setVisible(false);

            add_employee_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #3a4368, #28966c);");
            home_btn.setStyle("-fx-background-color:transparent");
            calandar_btn.setStyle("-fx-background-color:transparent");
            addEmployeeGendernList();
            addEmployeePositionList();



        } else if (event.getSource() == calandar_btn) {
            home_form.setVisible(false);
            addEmpoyee_form.setVisible(false);
            salary_form.setVisible(true);

            calandar_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #3a4368, #28966c);");
            add_employee_btn.setStyle("-fx-background-color:transparent");
            home_btn.setStyle("-fx-background-color:transparent");
            goToCalander();
        }

    }
    private double x= 0;
    private double y= 0;

    public void logout(){


        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confiramtion Message");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to logout?");
        Optional<ButtonType> option = alert.showAndWait();
        try {
            if(option.get().equals(ButtonType.OK)){
                logout.getScene().getWindow().hide();

                Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
                Stage stage = new Stage();
                Scene scene  = new Scene(root);
                root.setOnMousePressed((MouseEvent event)->{
                    x = event.getSceneX();
                    y = event.getSceneY();
                });
                root.setOnMouseDragged((MouseEvent event) ->{
                    stage.setX(event.getScreenX()-x);
                    stage.setY(event.getScreenY()-y);
                });
                stage.initStyle(StageStyle.TRANSPARENT);
                stage.setScene(scene);
                stage.show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void close(){
        System.exit(0);
    }

    public void minimize(){
        Stage stage = (Stage)main_form.getScene().getWindow();
        stage.setIconified(true);
    }
    public void goToCalander() throws IOException {
        employeeData selectedEmployee = addEmpoyee_tableView.getSelectionModel().getSelectedItem();

        if (selectedEmployee == null && getTeatcherId()== 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please select an employee to view the calendar.");
            alert.showAndWait();
            return;
        }

        int selectedEmployeeId= 0;

        addEmpoyee_clalanderBtn.getScene().getWindow().hide();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Calander.fxml"));
        Parent root = loader.load();

        CalendarController controller = loader.getController();
        if(getTeatcherId()== 0){
            selectedEmployeeId = selectedEmployee.getEmployeeId();
            controller.setId(selectedEmployeeId);
            controller.setName(selectedEmployee.getFirstName().charAt(0)+"." +selectedEmployee.getLastName());

        }else {

            controller.setId(0);
            controller.setName("Name orrr");

        }
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        root.setOnMousePressed((MouseEvent event) -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });
        root.setOnMouseDragged((MouseEvent event) -> {
            stage.setX(event.getScreenX() - x);
            stage.setY(event.getScreenY() - y);
        });
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addShowEmployee();
        addEmployeeGendernList();
        addEmployeePositionList();

        ObservableList<employeeData> listData = FXCollections.observableArrayList();

        String sql = "SELECT COUNT(*) AS count FROM admin";

        try {
            Connection connect = Database.connectDb();
            PreparedStatement prepare = connect.prepareStatement(sql);
            ResultSet result = prepare.executeQuery();

            if (result.next() && result.getInt("count") > 0) {
                add_employee_btn.setVisible(true);
            } else {
                add_employee_btn.setVisible(false);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
