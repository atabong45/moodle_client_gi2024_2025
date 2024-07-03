package com.example.moodle.Teacher.AssignmentPanel;


import com.example.moodle.Entities.Assignment;
import com.example.moodle.Entities.Submission;
import com.example.moodle.Teacher.CoursesPanel.DialogWindows.CreateCourseDialogController;
import com.example.moodle.Teacher.AssignmentPanel.TAssignmentCardController;


import com.example.moodle.api.AssignmentHelper;
import com.example.moodle.api.SubmissionHelper;
import com.example.moodle.dao.AssignmentDAO;
import com.example.moodle.dao.SubmissionDAO;
import com.example.moodle.moodleclient.Moodleclient;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class TAssignmentPanelController implements Initializable {

    @FXML
    private Label newAssignLabel;

    @FXML
    private Label returnArrow;

    @FXML
    private ScrollPane assignScrollPane;

    @FXML
    private GridPane gridpane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        AssignmentHelper assignmentHelper = new AssignmentHelper();
        ArrayList<Assignment> assignments =  assignmentHelper.getAssignments(Moodleclient.currentCourse.getCourseid());
        for (Assignment assignment: assignments) {
            loadAssignments(assignment.getAssignmentid());
        }
    }

    @FXML

    private void handleNewAssign(MouseEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/moodle/FXML/CreateAssignmentDialog.fxml"));
            Parent root = loader.load();

            CreateAssignmentDialogController createAssignDialogController = loader.getController();
            createAssignDialogController.setAssignPanelController(this);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void addAssignCardToPanel(AnchorPane assignCard) {
        int count = gridpane.getChildren().size(); // Get the current number of cards
        gridpane.add(assignCard, 0, count+1); // Add the card to the gridpane
        GridPane.setMargin(assignCard, new javafx.geometry.Insets(15, 20, 5, 10));
    }

    public void loadAssignments(long assignmentid) {
        SubmissionHelper submissionHelper = new SubmissionHelper();
        ArrayList<Submission> submissions = submissionHelper.getAllSubmissions(assignmentid);
        gridpane.getChildren().clear();
        for(int i = 0; i < submissions.size(); i++) {
            if(submissions.get(i).getStudentid() != Moodleclient.user.getId()) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/moodle/FXML/TeacherAssignmentCard.fxml"));

                    AnchorPane assignCard = loader.load();

                    TAssignmentCardController controller = loader.getController();
                    controller.setAssignDetails(submissions.get(i));

                    gridpane.add(assignCard, 0, i+1);
                    GridPane.setMargin(assignCard, new javafx.geometry.Insets(15, 20, 5, 10));

                } catch (IOException e) {
                    // Handle the exception (e.g., display error message)
                    System.err.println("Error loading assignments: " + e.getMessage());

                }
            }

        }
    }
}
