package com.learnJBDC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Doctor {
    
    // Connection object to interact with the database
    private Connection connection;

    // Constructor to initialize the connection
    public Doctor(Connection connection) {
        this.connection = connection;
    }

    // Method to view all doctors from the 'doctors' table
    public void viewDoctors() {
        // SQL query to select all doctors
        String query = "SELECT * FROM doctors";
        
        try {
            // Prepare the SQL statement
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Execute the query and get the result set
            ResultSet resultset = preparedStatement.executeQuery();

            // Print table header
            System.out.println("Doctors: ");
            System.out.println("+--------+--------------+------+-----+---------+----------------+");
            System.out.println("|Doctor ID    | Name                | Specialization  |");
            System.out.println("+--------+--------------+------+-----+---------+----------------+");

            // Loop through the results and print each doctor's details
            while (resultset.next()) {
                int id = resultset.getInt("id"); // Get doctor ID
                String name = resultset.getString("name"); // Get doctor name
                String Specialization = resultset.getString("Specialization"); // Get specialization

                // Print each row in formatted table style
                System.out.printf("|%-13s|%-21s|%-6s|\n", id, name, Specialization);
                System.out.println("+--------+--------------+------+-----+---------+----------------+");
            }
        } catch (SQLException e) {
            // Handle SQL errors
            e.printStackTrace();
        }
    }

    // Method to check if a doctor exists in the table by ID
    public boolean getDoctorByID(int id) {
        // SQL query to find a doctor by ID
        String query = "SELECT * FROM doctors WHERE ID=?";
        
        try {
            // Prepare the SQL statement
            PreparedStatement preparedstatement = connection.prepareStatement(query);
            
            // Set the parameter value for the query
            preparedstatement.setInt(1, id);

            // Execute the query
            ResultSet resultset = preparedstatement.executeQuery();

            // If a result is found, return true, else false
            if (resultset.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            // Handle SQL errors
            e.printStackTrace();
        }

        // Return false if an error occurs or doctor not found
        return false;
    }
}
