package com.example.employeemanagmentapplication;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;

public  class CalendarActivity {
    private LocalTime date;
    private String clientName;
    private String activityName;

    public CalendarActivity(LocalTime date, String clientName, String activityName) {
        this.date = date;
        this.clientName = clientName;
        this.activityName = activityName;
    }

    public LocalTime getDate() {
        return date;
    }

    public void setDate(LocalTime date) {
        this.date = date;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    @Override
    public String toString() {
        return "CalendarActivity{" +
                "date=" + date +
                ", clientName='" + clientName + '\'' +
                ", activityName='" + activityName + '\'' +
                '}';
    }
}