package com.example.moodle.Student.Cards;

import com.example.moodle.Entities.Assignment;
import com.example.moodle.Student.StudentCoursesPanel.StudentAvailableCourseCardController;
import com.example.moodle.Student.studentAssigmentPanel.studentAssigmentCardController;
import com.example.moodle.moodleclient.Moodleclient;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class AssigmentCard extends Pane {
    private Assignment assigment;
    private Pane pane;
    public AssigmentCard(Assignment assigment) {
        try {
            FXMLLoader loader = new FXMLLoader(CourseCard.class.getResource("/com/example/moodle/FXML/student_assigmentCard.fxml"));
            this.pane = loader.load();
            studentAssigmentCardController cardController = loader.getController();
            cardController.define(Moodleclient.currentCourse.getFullname(), assigment.getAssignmentname(), new SimpleDateFormat("dd/MM/yyyy HH:mm").format(assigment.getDuedate()), assigment.getAssignmentid());
            getChildren().add(this.pane);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
