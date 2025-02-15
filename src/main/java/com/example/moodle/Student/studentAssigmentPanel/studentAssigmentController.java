package com.example.moodle.Student.studentAssigmentPanel;

import com.example.moodle.Student.Cards.AssigmentCard;

import com.example.moodle.Entities.Assignment;

import com.example.moodle.api.AssignmentHelper;
import com.example.moodle.moodleclient.Moodleclient;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.ArrayList;
import java.util.Date;


public class studentAssigmentController {

    @FXML
    private Button Finished;

    @FXML
    private HBox buttomAssigmentStatut;

    @FXML
    private Button inProgress;

    @FXML
    private Label returnArrow;

    @FXML
    private ScrollPane scrollpane;

    @FXML
    private VBox assigmentList;

    @FXML
    private void initialize() {
        inProgress.setVisible(false);
        Finished.setVisible(false);

        AssignmentHelper assignmentHelper = new AssignmentHelper();
        ArrayList<Assignment> assigments = assignmentHelper.getAssignments(Moodleclient.currentCourse.getCourseid());
//        assigments.add(new Assigment("Architecture des ordinateurs","Devoir 1", "inProgress",new Date(2024, 6, 26, 12, 30, 0), new Date(2024, 4, 12, 12, 30, 0)));
//        assigments.add(new Assigment("Réseaux mobiles et intelligents", "Devoir 1","inProgress", new Date(2024, 6, 26, 12, 30, 0), new Date(2024, 4, 12, 12, 30, 0)));
//        assigments.add(new Assigment("Management", "Devoir 1","inProgress", new Date(2024, 6, 26, 12, 30, 0), new Date(2024, 4, 12, 12, 30, 0)));
//        assigments.add(new Assigment("Systèmes multi-agents", "Devoir 1","finished", new Date(2024, 6, 26, 12, 30, 0), new Date(2024, 4, 12, 12, 30, 0)));
//        assigments.add(new Assigment("Analyse numérique", "Devoir 1","finished", new Date(2024, 6, 26, 12, 30, 0), new Date(2024, 4, 12, 12, 30, 0)));
//        assigments.add(new Assigment("Systèmes formels", "Devoir 1","finished", new Date(2024, 6, 26, 12, 30, 0), new Date(2024, 4, 12, 12, 30, 0)));

        int count = 0;
        for(int i = 0; i < (int)Math.ceil(assigments.size()); i++) {
            assigmentList.getChildren().add(i, new AssigmentCard(assigments.get(count)));
            count++;
        }

    }

}
