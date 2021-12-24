package com.example.assignmentjava;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    public static void main(String[] args) {
        System.out.println("Hello world");
        try {
            Connection cnn = DriverManager.getConnection("jdbc:mysql://127.0.0.1/assignment-java", "root", "");
            Statement stt = cnn.createStatement();

            System.out.println("Process success!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
