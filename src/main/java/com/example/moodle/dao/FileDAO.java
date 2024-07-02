package com.example.moodle.dao;

import com.example.moodle.Entities.File;

import java.sql.*;

import static com.example.moodle.DBConnection.*;

public class FileDAO {
    private static Connection connect() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);
    }

    public static File getFile(long moduleid) {
        File file = null;
        String query = "SELECT * FROM file WHERE moduleid = ?";
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, moduleid);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                file = new File();
                file.setModuleid(resultSet.getLong("moduleid"));
                file.setFilename(resultSet.getString("filename"));
                file.setFilepath(resultSet.getString("filepath"));
                file.setFilesize(resultSet.getLong("filesize"));
                file.setFileurl(resultSet.getString("fileurl"));
                file.setMimetype(resultSet.getString("mimetype"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    public static void insertFile(File file) {
        String query = "INSERT INTO file (moduleid, filename, filepath, filesize, fileurl, mimetype) values (?, ?, ?, ?, ?, ?)";
        try (Connection connection = connect();
            PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, file.getModuleid());
            statement.setString(2, file.getFilename());
            statement.setString(3, "D:\\downloadedmoddlefiles\\" + file.getFilename());
            statement.setLong(4, file.getFilesize());
            statement.setString(5, file.getFileurl());
            statement.setString(6, file.getMimetype());
            statement.executeUpdate();
            System.out.println("File added");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
