package com.example.moodle.Student.Cards;

import com.example.moodle.Entities.Course;
import com.example.moodle.Student.StudentCoursesPanel.StudentAvailableCourseCardController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class CourseCard extends Pane {
    private Course course;
    private Pane pane;

    public CourseCard(Course course) {
        try {
            FXMLLoader loader = new FXMLLoader(CourseCard.class.getResource("/com/example/moodle/FXML/student_AvailableCourseCard_updated.fxml"));
            this.pane = loader.load();
            StudentAvailableCourseCardController cardController = loader.getController();
            cardController.define(course);
            getChildren().add(this.pane);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
