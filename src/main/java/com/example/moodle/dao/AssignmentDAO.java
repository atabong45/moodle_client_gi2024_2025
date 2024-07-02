package com.example.moodle.dao;


import com.example.moodle.DBConnection;
import com.example.moodle.Entities.Assignment;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.example.moodle.DBConnection.*;

public class AssignmentDAO {





    public static void main(String[] args) {
        try {
            // Charger le driver JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Insérer un devoir
            // insertAssignment("Assignment 1", Date.valueOf("2024-06-21"), Date.valueOf("2024-06-30"), "Course 1", "in progress");

            // Lire tous les devoirs
            // readAssignments();

            // Mettre à jour un devoir
            // updateAssignment(1, "Updated Assignment 1", Date.valueOf("2024-06-21"), Date.valueOf("2024-07-01"), "Course 1", "finish");

            // Lire tous les devoirs
            // readAssignments();

            // Supprimer un devoir
            deleteAssignment(1);

            // Lire tous les devoirs
            //readAssignments();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Méthode pour insérer un devoir
    public static void insertAssignment(com.example.moodle.Entities.Assignment assignment) {
        String query = "INSERT INTO assignment (assignmentid, moduleid, assignmentname, created, duedate) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);

             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, assignment.getAssignmentid());
            statement.setLong(2, assignment.getModuleid());
            statement.setString(3, assignment.getAssignmentname());
            statement.setLong(4, assignment.getCreated());
            statement.setLong(5, assignment.getDuedate());
            statement.executeUpdate();
            System.out.println("Assignment inserted successfully");
        } catch (SQLException e) {
            System.out.println("Error inserting assignment");
            e.printStackTrace();
        }
    }


    public static ArrayList<com.example.moodle.Entities.Assignment> readAssignments(long moduleid) {
        ArrayList<Assignment> assignments = new ArrayList<>();
        String query = "SELECT * FROM assignment WHERE moduleid = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, moduleid);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                com.example.moodle.Entities.Assignment assignment = new com.example.moodle.Entities.Assignment();
                assignment.setAssignmentid(resultSet.getInt("assignmentid"));
                assignment.setAssignmentname(resultSet.getString("assignmentname"));
                assignment.setCreated(resultSet.getLong("created"));
                assignment.setDuedate(resultSet.getLong("duedate"));
                assignment.setModuleid(resultSet.getLong("moduleid"));
                assignments.add(assignment);
            }
        } catch (SQLException e) {
            System.out.println("Error reading assignments");
            e.printStackTrace();
        }
        return assignments;

    }
    // Méthode pour mettre à jour un devoir
    public static void updateAssignment(int id, String assignmentName, Date createdDate, Date limitedDate, String courseName, String statut) {
        String query = "UPDATE assignments SET assignmentName = ?, createdDate = ?, limitedDate = ?, courseName = ?, statut = ? WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, assignmentName);
            statement.setDate(2, createdDate);
            statement.setDate(3, limitedDate);
            statement.setString(4, courseName);
            statement.setString(5, statut);
            statement.setInt(6, id);
            statement.executeUpdate();
            System.out.println("Assignment updated successfully");
        } catch (SQLException e) {
            System.out.println("Error updating assignment");
            e.printStackTrace();
        }
    }

    // Méthode pour supprimer un devoir
    public static void deleteAssignment(int id) {
        String query = "DELETE FROM assignments WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
            System.out.println("Assignment deleted successfully");
        } catch (SQLException e) {
            System.out.println("Error deleting assignment");
            e.printStackTrace();
        }
    }

}

