package com.example.moodle.Teacher.AssignmentPanel;


import com.example.moodle.Entities.Assignment;
import com.example.moodle.Entities.Submission;
import com.example.moodle.Entities.User;
import com.example.moodle.api.AssignmentHelper;
import com.example.moodle.api.UserHelper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static com.example.moodle.moodleclient.Moodleclient.currentCourse;
import static com.example.moodle.moodleclient.Moodleclient.root;

public class TAssignmentCardController implements Initializable {
    @FXML
    private Label assignNameLabel;
    @FXML
    private Label courseNameLabel;
    @FXML
    private Button detailsBtn;

    private Assignment assignment;
    private Submission submission;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void handleDetails() throws IOException {
        System.out.println("You clicked");

        FXMLLoader detailsPaneLoader = new FXMLLoader(getClass().getResource( "/com/example/moodle/FXML/TeacherAssignmentDetails.fxml"));
        AnchorPane detailsPane = detailsPaneLoader.load();

       TAssignmentDetailsController detailsController = detailsPaneLoader.getController();
       detailsController.setAssignDetails(assignment, submission);
       detailsController.setGrade();

        root.setCenter(detailsPane);
    }

    public void setAssignDetails(Submission submission) {
        this.submission = submission;
        AssignmentHelper assignmentHelper = new AssignmentHelper();
        UserHelper userHelper = new UserHelper();
        ArrayList<Assignment> assignments = assignmentHelper.getAssignments(currentCourse.getCourseid());
        for(Assignment assignment: assignments) {
            if(assignment.getAssignmentid() == submission.getAssignmentid()) {
                this.assignment = assignment;
                this.assignNameLabel.setText(assignment.getAssignmentname() + "");
                this.courseNameLabel.setText(submission.getStudentid() + "");
            }
        }
    }
}
