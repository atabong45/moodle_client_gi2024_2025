package com.example.moodle.Teacher.CoursesPanel.DialogWindows;

import com.example.moodle.api.GradeHelper;
import com.example.moodle.moodleclient.Moodleclient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class GradeAssignmentController {
    private long assignmentid;
    private long studentid;

    @FXML
    private TextField numfield;

    @FXML
    void createGrade(ActionEvent event) {
        GradeHelper gradeHelper = new GradeHelper();
        if(gradeHelper.saveGrade(assignmentid, studentid, Integer.parseInt(numfield.getText()),Moodleclient.token)) {
            System.out.println("Everything went well !");
            Stage stage = (Stage) numfield.getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    void handleCancelAction(ActionEvent event) {
        Stage stage = (Stage) numfield.getScene().getWindow();
        stage.close();
    }

    @FXML
    void handleCancelEvent(MouseEvent event) {
        Stage stage = (Stage) numfield.getScene().getWindow();
        stage.close();
    }

    public void define(long assignmentid, long studentid) {
        this.assignmentid = assignmentid;
        this.studentid = studentid;
    }

}
