package com.example.employeemanagmentapplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class CalendarController implements Initializable {
    private int Id;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    ZonedDateTime dateFocus;
    private boolean isAdmin ;

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    ZonedDateTime today;

    @FXML
    private Text year;

    @FXML
    private Text month;

    @FXML
    private FlowPane calendar;

    // Map to store activities by date
    private Map<Integer, List<CalendarActivity>> calendarActivityMap = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateFocus = ZonedDateTime.now();
        today = ZonedDateTime.now();
        drawCalendar();
    }

    @FXML
    void backOneMonth(ActionEvent event) {
        dateFocus = dateFocus.minusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    @FXML
    void forwardOneMonth(ActionEvent event) {
        dateFocus = dateFocus.plusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    private void drawCalendar() {
        year.setText(String.valueOf(dateFocus.getYear()));
        month.setText(String.valueOf(dateFocus.getMonth()));

        double calendarWidth = calendar.getPrefWidth();
        double calendarHeight = calendar.getPrefHeight();
        double strokeWidth = 1;
        double spacingH = calendar.getHgap();
        double spacingV = calendar.getVgap();

        // Get activities for the current month
        calendarActivityMap = getCalendarActivitiesMonth(dateFocus);

        int monthMaxDate = dateFocus.getMonth().maxLength();
        if (dateFocus.getYear() % 4 != 0 && monthMaxDate == 29) {
            monthMaxDate = 28;
        }
        int dateOffset = ZonedDateTime.of(dateFocus.getYear(), dateFocus.getMonthValue(), 1, 0, 0, 0, 0, dateFocus.getZone()).getDayOfWeek().getValue();

        for (int currentDate = 1; currentDate <= monthMaxDate; currentDate++) {
            int i = (currentDate + dateOffset - 1) / 7;
            int j = (currentDate + dateOffset - 1) % 7;

            StackPane stackPane = new StackPane();
            stackPane.setOnMouseClicked(this::handleCalendarCellClick);

            Rectangle rectangle = new Rectangle();
            rectangle.setFill(Color.TRANSPARENT);
            rectangle.setStroke(Color.BLACK);
            rectangle.setStrokeWidth(strokeWidth);
            double rectangleWidth = (calendarWidth / 7) - strokeWidth - spacingH;
            rectangle.setWidth(rectangleWidth);
            double rectangleHeight = (calendarHeight / 6) - strokeWidth - spacingV;
            rectangle.setHeight(rectangleHeight);
            stackPane.getChildren().add(rectangle);

            Text date = new Text(String.valueOf(currentDate));
            double textTranslationY = -(rectangleHeight / 2) * 0.75;
            date.setTranslateY(textTranslationY);
            stackPane.getChildren().add(date);

            List<CalendarActivity> calendarActivities = calendarActivityMap.get(currentDate);
            if (calendarActivities != null) {
                createCalendarActivity(calendarActivities, rectangleHeight, rectangleWidth, stackPane);
            }

            if (today.getYear() == dateFocus.getYear() && today.getMonth() == dateFocus.getMonth() && today.getDayOfMonth() == currentDate) {
                rectangle.setStroke(Color.BLUE);
            }

            calendar.getChildren().add(stackPane);
        }
    }

    private void createCalendarActivity(List<CalendarActivity> calendarActivities, double rectangleHeight, double rectangleWidth, StackPane stackPane) {
        VBox calendarActivityBox = new VBox();
        for (CalendarActivity activity : calendarActivities) {
            String activityDetails = "Date: " + activity.getDate() + "\n" +
                    "Client: " + activity.getClientName() + "\n" +
                    "Activity: " + activity.getActivityName();
            Text activityText = new Text(activityDetails);
            calendarActivityBox.getChildren().add(activityText);
        }
        calendarActivityBox.setTranslateY((rectangleHeight / 2) * 0.20);
        calendarActivityBox.setMaxWidth(rectangleWidth * 0.8);
        calendarActivityBox.setMaxHeight(rectangleHeight * 0.65);
        calendarActivityBox.setStyle("-fx-background-color:GRAY");
        stackPane.getChildren().add(calendarActivityBox);
    }

    private Map<Integer, List<CalendarActivity>> createCalendarMap(List<CalendarActivity> calendarActivities) {
        Map<Integer, List<CalendarActivity>> calendarActivityMap = new HashMap<>();

        for (CalendarActivity activity : calendarActivities) {
            int activityDate = activity.getDate().getHour() * 100 + activity.getDate().getMinute();
            if (!calendarActivityMap.containsKey(activityDate)) {
                calendarActivityMap.put(activityDate, new ArrayList<>(List.of(activity)));
            } else {
                List<CalendarActivity> oldListByDate = calendarActivityMap.get(activityDate);
                List<CalendarActivity> newList = new ArrayList<>(oldListByDate);
                newList.add(activity);
                calendarActivityMap.put(activityDate, newList);
            }
        }
        return calendarActivityMap;
    }

    private Map<Integer, List<CalendarActivity>> getCalendarActivitiesMonth(ZonedDateTime dateFocus) {
        List<CalendarActivity> calendarActivities = new ArrayList<>();
        int year = dateFocus.getYear();
        int month = dateFocus.getMonthValue();

        Random random = new Random();
        for (int i = 0; i < 50; i++) {
            ZonedDateTime time = ZonedDateTime.of(year, month, random.nextInt(27) + 1, random.nextInt(24), random.nextInt(60), 0, 0, dateFocus.getZone());
            //calendarActivities.add(new CalendarActivity(time, "Hans", "111111"));
        }

        return createCalendarMap(calendarActivities);
    }

    private boolean dialogOpened = false;

    @FXML
    private void handleCalendarCellClick(MouseEvent event) {
        if (dialogOpened) return;

        dialogOpened = true;

        // Ensure the clicked node is indeed a StackPane
        Node clickedNode = (Node) event.getSource();
        if (!(clickedNode instanceof StackPane)) {
            dialogOpened = false;
            return;
        }

        StackPane stackPane = (StackPane) clickedNode;
        Text dateText = (Text) stackPane.getChildren().get(1);
        int activityDay = Integer.parseInt(dateText.getText());
        List<CalendarActivity> activities = calendarActivityMap.get(activityDay);
        CalendarActivity existingActivity = activities != null && !activities.isEmpty() ? activities.get(0) : null;

        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Activity Details");
        dialog.setHeaderText(null);

        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType deleteButtonType = new ButtonType("Delete", ButtonBar.ButtonData.NO);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL, deleteButtonType);

        TextField activityNameField = new TextField();
        activityNameField.setPromptText("Activity Name");
        TextField timeField = new TextField();
        timeField.setPromptText("HH:mm");

        if (existingActivity != null) {
            activityNameField.setText(existingActivity.getActivityName());
            timeField.setText(String.format("%02d:%02d", existingActivity.getDate().getHour(), existingActivity.getDate().getMinute()));
        }

        GridPane gridPane = new GridPane();
        gridPane.add(new Label("Activity:"), 0, 0);
        gridPane.add(activityNameField, 1, 0);
        gridPane.add(new Label("Time:"), 0, 1);
        gridPane.add(timeField, 1, 1);
        dialog.getDialogPane().setContent(gridPane);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return new Pair<>(activityNameField.getText(), timeField.getText());
            } else if (dialogButton == deleteButtonType) {
                return new Pair<>("DELETE", null);
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();
        result.ifPresent(activityDetails -> {
            String activityName = activityDetails.getKey();
            if ("DELETE".equals(activityName)) {
                calendarActivityMap.remove(activityDay);
                stackPane.getChildren().clear();
                Rectangle rectangle = new Rectangle(stackPane.getWidth(), stackPane.getHeight());
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(Color.BLACK);
                stackPane.getChildren().add(rectangle);
                Text date = new Text(String.valueOf(activityDay));
                stackPane.getChildren().add(date);
            } else {
                String timeString = activityDetails.getValue();
                try {
                    LocalTime time = LocalTime.parse(timeString, DateTimeFormatter.ofPattern("HH:mm"));
                    String clientName = getName();

                    CalendarActivity newActivity = new CalendarActivity(time, clientName, activityName);
                    setId(getId() + 1);

                    List<CalendarActivity> calendarActivities = new ArrayList<>();
                    calendarActivities.add(newActivity);
                    calendarActivityMap.put(activityDay, calendarActivities);

                    stackPane.getChildren().clear();
                    Rectangle rectangle = new Rectangle(stackPane.getWidth(), stackPane.getHeight());
                    rectangle.setFill(Color.TRANSPARENT);
                    rectangle.setStroke(Color.BLACK);
                    stackPane.getChildren().add(rectangle);
                    Text date = new Text(String.valueOf(activityDay));
                    stackPane.getChildren().add(date);
                    createCalendarActivity(calendarActivityMap.get(activityDay), stackPane.getHeight(), stackPane.getWidth(), stackPane);
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid time format. Please use HH:mm.");
                }
            }
        });

        dialogOpened = false;
    }
    private double x = 0;
    private double y = 0;
    @FXML
    private Button backBtn;
    public void goToCalander() throws IOException {

        backBtn.getScene().getWindow().hide();
        Parent root = FXMLLoader.load(getClass().getResource("dashboard.fxml"));
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
}