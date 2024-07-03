package com.example.moodle.Teacher.AssignmentPanel;

import com.example.moodle.Entities.Assignment;
import com.example.moodle.Entities.Grade;
import com.example.moodle.Entities.Submission;
import com.example.moodle.Teacher.CoursesPanel.DialogWindows.GradeAssignmentController;
import com.example.moodle.api.GradeHelper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

public class TAssignmentDetailsController implements Initializable {

    @FXML
    private Label modifyLabel;
    @FXML
    private Label deleteLabel;
    @FXML
    private Label assignName;
    @FXML
    private Label openDate;
    @FXML
    private Text lblGrade;
    @FXML
    private Label lblStatus;
    @FXML
    private Label dueDate;
    @FXML
    private Button viewSubmissionBtn;
    @FXML
    private Button gradleBtn;
    @FXML
    private ScrollPane submissionScrollPane;
    private Submission submission;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void handleModify() {

    }

    public void handleDelete() {

    }

    public void handleViewSubmission() {

    }

    public void setGrade() {
        GradeHelper gradeHelper = new GradeHelper();
        Grade grade = gradeHelper.getGrade(submission.getAssignmentid(), submission.getAssignmentid(), submission.getStudentid());
        // System.out.println(grade.getGrade());
        if(grade != null) {
            lblGrade.setText(grade.getGrade() + "");
        }
        lblStatus.setText(submission.getStatus());
    }

    public void handleGradle() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/moodle/FXML/CreateGradeDialog.fxml"));
            Parent root = loader.load();
            GradeAssignmentController controller = loader.getController();

            controller.define(submission.getAssignmentid(), submission.getStudentid());

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(new Scene(root));
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setAssignDetails(Assignment assign, Submission submission) {
        this.assignName.setText(assign.getAssignmentname());
        this.openDate.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date(assign.getCreated())));
        // this.dueDate.setText(assign.getDuedate() + "");
        this.submission = submission;
    }

}
