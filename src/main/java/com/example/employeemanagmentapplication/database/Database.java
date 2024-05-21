package com.example.employeemanagmentapplication.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {

    private static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/employee";
    private static final String DATABASE_USER = "postgres";
    private static final String DATABASE_PASSWORD = "root";

    public static Connection connectDb(){
        try{
            Class.forName("org.postgresql.Driver");
            Connection connect = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
            return connect;
        } catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
