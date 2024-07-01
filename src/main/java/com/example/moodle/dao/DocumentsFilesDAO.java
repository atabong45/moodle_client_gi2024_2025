package com.example.moodle.dao;

import java.sql.*;

import static com.example.moodle.DBConnection.*;


public class DocumentsFilesDAO {



    public static void main(String[] args) {
        try {
            // Charger le driver JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void insertDocumentFile(String fileName, long fileSize, String fileType, String filePath, int chapterId) {
        String query = "INSERT INTO documents_files (fileName, fileSize, fileType, filePath, chapterId) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, fileName);
            statement.setLong(2, fileSize);
            statement.setString(3, fileType);
            statement.setString(4, filePath);
            statement.setInt(5, chapterId);
            statement.executeUpdate();
            System.out.println("Private file inserted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour supprimer un fichier
    public static void deleteDocumentFile(int id) {
        String query = "DELETE FROM documents_files WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
            System.out.println("Document deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
