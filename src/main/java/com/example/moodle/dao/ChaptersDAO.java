package com.example.moodle.dao;

// import com.example.moodle.Student.Entities.Chapter;

import java.sql.*;
import java.util.ArrayList;

public class ChaptersDAO {
//    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/moodleclient";
//    private static final String USERNAME = "root";
//    private static final String PASSWORD = "681503533";
//
//    public static void main(String[] args) {
//        try {
//            Class.forName("com.mysql.cj.Driver");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static ArrayList<Chapter> getChapters (int courseId) {
//        ArrayList<Chapter> chapters = new ArrayList<>();
//        String query = "SELECT * FROM Chapters WHERE courseId = ?";
//        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
//            PreparedStatement statement = connection.prepareStatement(query)) {
//            statement.setInt(1, courseId);
//            ResultSet resultSet = statement.executeQuery();
//            while(resultSet.next()) {
//                int id = resultSet.getInt("id");
//                String title = resultSet.getString("title");
//                int num = resultSet.getInt("num");
//                String content = resultSet.getString("content");
//                int course = resultSet.getInt("courseId");
//                Chapter chapter = new Chapter(id, title, num, content, course);
//                chapters.add(chapter);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return chapters;
//    }
//
//    public static void insertChapter(String chaptertitle, int chapterNum, String content, int courseId) {
//        String query = "INSERT INTO Chapters (title, num, content, courseId) VALUES (?, ?, ?, ?)";
//        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
//             PreparedStatement statement = connection.prepareStatement(query)) {
//            statement.setString(1, chaptertitle);
//            statement.setInt(2, chapterNum);
//            statement.setString(3, content);
//            statement.setInt(4, courseId);
//            statement.executeUpdate();
//            System.out.println("Chapter inserted successfully.");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    // Méthode pour supprimer un chapitre
//    public static void deleteChapter(int id) {
//        String query = "DELETE FROM Course WHERE id = ?";
//        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
//             PreparedStatement statement = connection.prepareStatement(query)) {
//            statement.setInt(1, id);
//            statement.executeUpdate();
//            System.out.println("Chapter deleted successfully.");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
}
