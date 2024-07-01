package com.example.moodle.dao;

import com.example.moodle.Entities.Module;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;

import static com.example.moodle.DBConnection.*;

public class ModuleDAO {
    private static Connection connect() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);
    }

    public static ArrayList<Module> getModules(long sectionid) {
        ArrayList<Module> modules = new ArrayList<>();
        String query = "SELECT * FROM module WHERE sectionid = ?";
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, sectionid);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                Module module = new Module();
                module.setCmid(resultSet.getLong("cmid"));
                module.setSectionid(sectionid);
                module.setName(resultSet.getString("name"));
                module.setModname(resultSet.getString("modname"));
                module.setModplural(resultSet.getString("modplural"));
                module.setDownloadcontent(resultSet.getLong("downloadcontent"));

                modules.add(module);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return modules;
    }

    public static void insertModule(Module module) {
        String query = "INSERT INTO module (cmid, sectionid, name, modname, modplural, downloadcontent) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = connect();
            PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, module.getCmid());
            statement.setLong(2, module.getSectionid());
            statement.setString(3, module.getName());
            statement.setString(4, module.getModname());
            statement.setString(5, module.getModplural());
            statement.setLong(6, module.getDownloadcontent());
            statement.executeUpdate();
            System.out.println("Module inserted successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
