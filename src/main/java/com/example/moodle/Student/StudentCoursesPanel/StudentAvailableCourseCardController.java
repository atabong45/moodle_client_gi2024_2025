package com.example.moodle.Student.StudentCoursesPanel;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.example.moodle.Teacher.CoursesPanel.CourseViewPanelController;
import com.example.moodle.Entities.Course;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import static com.example.moodle.moodleclient.Moodleclient.currentCourse;
import static com.example.moodle.moodleclient.Moodleclient.root;

public class StudentAvailableCourseCardController implements Initializable{
    @FXML
    private Label ChaptersNumber;

    @FXML
    private Label courseDesc;

    @FXML
    private Label courseName;

    private Course cours;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    void handleCourse(MouseEvent event) throws IOException {
        currentCourse = cours;

        FXMLLoader contentLoader = new FXMLLoader(StudentAvailableCourseCardController.class.getResource("/com/example/moodle/FXML/student_CourseViewPanel_updated.fxml"));
        AnchorPane content = contentLoader.load();

        Label name = (Label) contentLoader.getNamespace().get("coursename");
        TextArea desc = (TextArea) contentLoader.getNamespace().get("courseDescription");

        name.setText(courseName.getText());
        desc.setText(courseDesc.getText());
        root.setCenter(content);
    }

    public void define(Course course) {
        this.courseName.setText(course.getFullname());
        this.courseDesc.setText(course.getSummary());
        this.courseDesc.setStyle("-fx-text-fill: black");
        this.ChaptersNumber.setText(course.getNumsections() + " Chapters");
        this.ChaptersNumber.setStyle("-fx-text-fill: black");
        this.cours = course;
    }
}
