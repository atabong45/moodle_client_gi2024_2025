package com.example.moodle.dao;

import com.example.moodle.Entities.Course;

import java.sql.*;
import java.util.ArrayList;

import static com.example.moodle.DBConnection.*;
import static com.example.moodle.moodleclient.Moodleclient.user;

public class CourseDAO {



    public static void main(String[] args) {
        try {
            // Charger le driver JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Insérer un cours
            // insertCourse("Mathematics", "MATH101", "Basic Mathematics", 10, 5);

            // Lire tous les cours
            readCourses();

            // Mettre à jour un cours
            updateCourse(1, "Mathematics", "MATH102", "Advanced Mathematics", 15, 7);

            // Lire tous les cours
            readCourses();

            // Supprimer un cours
            deleteCourse(1);

            // Lire tous les cours
            readCourses();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Méthode pour insérer un cours
    public static void insertCourse(Course course) {
        String query = "INSERT INTO course (courseid, fullname, shortname, summary, numsections, startdate, enddate, updated, studentid, teacherid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, course.getCourseid());
            statement.setString(2, course.getFullname());
            statement.setString(3, course.getShortname());
            statement.setString(4, course.getSummary());
            statement.setInt(5, course.getNumsections());
            statement.setLong(6, course.getStartdate());
            statement.setLong(7, course.getEnddate());
            statement.setLong(8, course.getUpdated());
            statement.setLong(9, user.getId());
            statement.setLong(10, 2);
            statement.executeUpdate();
            System.out.println("Course inserted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean isCourse(long id) {
        Course course = null;
        String query = "SELECT * FROM course WHERE courseid = ?";
        try(Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);
            PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static ArrayList<Course> getEnrolledCourses(long id) {
        ArrayList<Course> courses = new ArrayList<>();
        String query = "SELECT * FROM course WHERE studentid = ?";
        try (Connection connection = DriverManager.getConnection(JDBC_URL,JDBC_USERNAME, JDBC_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query);
             ) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Course course = new Course();
                course.setCourseid(resultSet.getLong("courseid"));
                course.setFullname(resultSet.getString("fullname"));
                course.setShortname(resultSet.getString("shortname"));
                course.setSummary(resultSet.getString("summary"));
                course.setNumsections(resultSet.getInt("numsections"));
                course.setStartdate(resultSet.getLong("startdate"));
                course.setEnddate(resultSet.getLong("enddate"));
                course.setUpdated(resultSet.getLong("updated"));

                courses.add(course);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return courses;
    }

    // Méthode pour lire tous les cours
    public static void readCourses() {
        String query = "SELECT * FROM Course";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String courseName = resultSet.getString("courseName");
                String courseAbr = resultSet.getString("courseAbr");
                String courseDescription = resultSet.getString("courseDescription");
                int nbChapters = resultSet.getInt("nbChapters");
                int nbAssignments = resultSet.getInt("nbAssignments");
                System.out.println("ID: " + id + ", Course Name: " + courseName + ", Course Abbreviation: " + courseAbr +
                        ", Description: " + courseDescription + ", Chapters: " + nbChapters + ", Assignments: " + nbAssignments);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour mettre à jour un cours
    public static void updateCourse(int id, String courseName, String courseAbr, String courseDescription, int nbChapters, int nbAssignments) {
        String query = "UPDATE Course SET courseName = ?, courseAbr = ?, courseDescription = ?, nbChapters = ?, nbAssignments = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, courseName);
            statement.setString(2, courseAbr);
            statement.setString(3, courseDescription);
            statement.setInt(4, nbChapters);
            statement.setInt(5, nbAssignments);
            statement.setInt(6, id);
            statement.executeUpdate();
            System.out.println("Course updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour supprimer un cours
    public static void deleteCourse(int id) {
        String query = "DELETE FROM Course WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
            System.out.println("Course deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertCourse(String text, String text1, String text2, int nbChapters, int nbAssignments) {
    }
}
