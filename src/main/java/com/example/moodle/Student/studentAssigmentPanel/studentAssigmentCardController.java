package com.example.moodle.Student.studentAssigmentPanel;

import com.example.moodle.Login.HelloController;
import com.example.moodle.MainDry.Dry;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Date;
import static com.example.moodle.moodleclient.Moodleclient.root;

public class studentAssigmentCardController {

    @FXML
    private Label ChaptersNumber;

    @FXML
    private Label deadLine;

    @FXML
    private VBox assigment;

    @FXML
    private Label AssigmentName;

    @FXML
    private Label courseName;
    private long assignmentid;

    @FXML
    private void initialize() {
        AssigmentName.setStyle("-fx-text-fill: black");
        deadLine.setStyle("-fx-text-fill: red");
    }

    public void define(String assigmentCourseName, String assigmentName, String Deadline, long id) {
        this.courseName.setText(assigmentCourseName + " / ");
        this.AssigmentName.setText(assigmentName);
        this.deadLine.setText(Deadline);
        this.assignmentid = id;
    }

    @FXML
    void handleAssigment(MouseEvent event) {
        try {
            FXMLLoader contentLoader = new FXMLLoader(Dry.class.getResource("/com/example/moodle/FXML/student_assigmentViewPanel.fxml"));
            AnchorPane content = contentLoader.load();
            StudentAssigmentViewPanelController controller = contentLoader.getController();
            controller.setAssignmentid(assignmentid);
            root.setCenter(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
