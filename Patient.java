package com.learnJBDC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {

    // Database connection object
    private Connection connection;
    
    // Scanner object for reading user input
    private Scanner scanner;
    
    // Constructor to initialize connection and scanner
    public Patient(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }
    
    /**
     * Method to add a new patient into the database.
     * It takes patient details from the user and inserts them into the 'patients' table.
     */
    public void addPatient() {
        // Asking user for patient details
        System.out.println("Enter Patient's Name");
        String name = scanner.next();
        
        System.out.println("Enter Patient's Age");
        int age = scanner.nextInt();
        
        System.out.println("Enter Patient's Gender");
        String gender = scanner.next();
        
        // SQL INSERT query to add the new patient
        try {
            String query = "INSERT INTO patients(name, age, gender) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            
            // Setting the values for the query
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);
            preparedStatement.setString(3, gender);
            
            // Execute the update and check if the record was inserted
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Patient Added Successfully");
            } else {
                System.out.println("Failed to add patient!");
            }
        } catch (SQLException e) {
            // Prints any SQL-related errors
            e.printStackTrace();
        }
    }
    
    /**
     * Method to view all patients from the 'patients' table.
     * It retrieves all records and displays them in a table format.
     */
    public void viewPatients() {
        String query = "SELECT * FROM patients";
        
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultset = preparedStatement.executeQuery();
            
            // Printing table header
            System.out.println("Patients: ");
            System.out.println("+--------+--------------+------+-----+---------+----------------+");
            System.out.println("|Patients ID  | Name               | Age  | Gender   |");
            System.out.println("+--------+--------------+------+-----+---------+----------------+");
            
            // Looping through all records and printing each patient
            while (resultset.next()) {
                int id = resultset.getInt("id");
                String name = resultset.getString("name");
                int age = resultset.getInt("age");
                String gender = resultset.getString("gender");
                
                // Print each record in formatted style
                System.out.printf("|%-13s|%-21s|%-6s|%-10s|\n", id, name, age, gender);
                System.out.println("+--------+--------------+------+-----+---------+----------------+");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Method to check if a patient exists in the 'patients' table using their ID.
     * 
     * @param id - Patient ID to search for.
     * @return true if patient exists, false otherwise.
     */
    public boolean getPatientByID(int id) {
        String query = "SELECT * FROM patients WHERE ID=?";
        
        try {
            PreparedStatement preparedstatement = connection.prepareStatement(query);
            preparedstatement.setInt(1, id);
            ResultSet resultset = preparedstatement.executeQuery();
            
            // If a record is found, return true
            if (resultset.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // Return false if no patient found or error occurred
        return false;
    }
}
