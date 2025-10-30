package com.learnJBDC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class HospitalManagement {

    // Database connection details
    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String username = "root";
    private static final String password = "Varunteja@17";

    public static void main(String[] args) {
        // Load MySQL JDBC driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Scanner for user input
        Scanner scanner = new Scanner(System.in);

        try {
            // Create a connection to the database
            Connection connection = DriverManager.getConnection(url, username, password);

            // Create Patient and Doctor objects for operations
            Patient patient = new Patient(connection, scanner);
            Doctor doctor = new Doctor(connection);

            // Infinite loop for menu-driven program
            while (true) {
                // Display main menu
                System.out.println("Hospital Management System");
                System.out.println("1. Add Patient");
                System.out.println("2. View Patients");
                System.out.println("3. View Doctors");
                System.out.println("4. Book Appointment");
                System.out.println("5. Exit");
                System.out.println("Please Enter Your Choice");

                // Get user's menu choice
                int choice = scanner.nextInt();

                // Perform the chosen operation
                switch (choice) {
                    case 1:
                        // Add new patient
                        patient.addPatient();
                        System.out.println();
                        break;

                    case 2:
                        // View all patients
                        patient.viewPatients();
                        System.out.println();
                        break;

                    case 3:
                        // View all doctors
                        doctor.viewDoctors();
                        System.out.println();
                        break;

                    case 4:
                        // Book an appointment
                        bookAppointment(patient, doctor, connection, scanner);
                        System.out.println();
                        break;

                    case 5:
                        // Exit the program
                        System.out.println("THANK YOU FOR USING HOSPITAL MANAGEMENT SYSTEM");
                        return;

                    default:
                        // Invalid choice
                        System.out.println("Please Enter Valid Input");
                        break;
                }
            }
        } catch (SQLException e) {
            // Handle SQL connection errors
            e.printStackTrace();
        }
    }

    /**
     * Method to book an appointment.
     * Checks if patient and doctor exist, verifies doctor's availability, 
     * and inserts appointment into the database.
     */
    public static void bookAppointment(Patient patient, Doctor doctor, Connection connection, Scanner scanner) {
        // Get appointment details from user
        System.out.println("Please Enter Patient ID: ");
        int patients_id = scanner.nextInt();

        System.out.println("Please Enter Doctor ID: ");
        int doctors_id = scanner.nextInt();

        System.out.println("Please Enter Appointment Date (YYYY-MM-DD): ");
        String appointment_date = scanner.next();

        // Check if patient and doctor exist
        if (patient.getPatientByID(patients_id) && doctor.getDoctorByID(doctors_id)) {
            // Check if doctor is available on the given date
            if (checkDoctorAvailability(doctors_id, appointment_date, connection)) {
                String appointmentQuery = "INSERT INTO appointments (patients_id, doctors_id, appointment_date) VALUES (?, ?, ?)";
                try {
                    // Insert new appointment record
                    PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery);
                    preparedStatement.setInt(1, patients_id);
                    preparedStatement.setInt(2, doctors_id);
                    preparedStatement.setString(3, appointment_date);

                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Appointment Booked");
                    } else {
                        System.out.println("Appointment could not be booked");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Doctor not available on this date!!");
            }
        } else {
            System.out.println("Either doctor or patient doesn't exist!!!");
        }
    }

    /**
     * Method to check if a doctor is available on a given date.
     * Returns true if no appointment exists for that doctor on the given date.
     */
    public static boolean checkDoctorAvailability(int doctors_id, String appointment_date, Connection connection) {
        String query = "SELECT COUNT(*) FROM appointments WHERE doctors_id=? AND appointment_date=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, doctors_id);
            preparedStatement.setString(2, appointment_date);
            ResultSet resultset = preparedStatement.executeQuery();

            if (resultset.next()) {
                int count = resultset.getInt(1); // Get count of existing appointments
                if (count == 0) {
                    return true; // Doctor is available
                } else {
                    return false; // Doctor already booked
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Default: doctor unavailable if query fails
    }
}
