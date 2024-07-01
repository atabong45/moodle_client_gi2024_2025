package com.example.moodle.Teacher.CoursesPanel.DialogWindows;

import com.example.moodle.Teacher.Cards.CourseCard;
import com.example.moodle.Teacher.CoursesPanel.CourseViewPanelController;
import com.example.moodle.Teacher.CoursesPanel.CoursesPanelController;
import com.example.moodle.Teacher.entity.Course;
import com.example.moodle.dao.CourseDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import static com.example.moodle.moodleclient.Moodleclient.currentCourse;
import static com.example.moodle.moodleclient.Moodleclient.root;

public class EditCourseDialogController implements Initializable {

    @FXML
    private TextArea descriptionfield;

    @FXML
    private TextField namefield;

    @FXML
    private TextField shortnamefield;

    @FXML
    private Label alertMsg;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        namefield.setText(currentCourse.getFullname());
        shortnamefield.setText(currentCourse.getShortname());
        descriptionfield.setText(currentCourse.getSummary());
    }

    @FXML
    void editCourse(ActionEvent event) {

        if (namefield.getText().isEmpty() || shortnamefield.getText().isEmpty() || descriptionfield.getText().isEmpty()) {
            alertMsg.setVisible(true);
            return;
        }

        try {
            currentCourse.setFullname(namefield.getText());
            currentCourse.setShortname(shortnamefield.getText());
            currentCourse.setSummary(descriptionfield.getText());

            CourseDAO.updateCourse((int) currentCourse.getCourseid(), namefield.getText(), shortnamefield.getText(), descriptionfield.getText(), currentCourse.getNumsections(), currentCourse.getNumsections());
            System.out.println("Course created successfully.");

            FXMLLoader courseviewloader = new FXMLLoader(CreateCourseDialogController.class.getResource("/com/example/moodle/FXML/CourseViewPanel_updated.fxml"));
            AnchorPane courseview = courseviewloader.load();

            root.setCenter(courseview);

        } catch (Exception e) {
            e.printStackTrace();
        }

        Stage stage = (Stage) namefield.getScene().getWindow();
        stage.close();
    }

    @FXML
    void handleCancelAction(ActionEvent event) {
        Stage stage = (Stage) namefield.getScene().getWindow();
        stage.close();
    }

    @FXML
    void handleCancelEvent(MouseEvent event) {
        Stage stage = (Stage) namefield.getScene().getWindow();
        stage.close();
    }

}
