package com.example.moodle.Teacher.CoursesPanel.DialogWindows;

import com.example.moodle.dao.CourseDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import static com.example.moodle.moodleclient.Moodleclient.currentCourse;
import static com.example.moodle.moodleclient.Moodleclient.root;

public class CreateChapterDialogController implements Initializable {


    @FXML
    private TextField namefield;

    @FXML
    private TextField numfield;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    void createChapter(ActionEvent event) {

        if (namefield.getText().isEmpty()) {
            System.out.println("New chapter must have a name !");
            return;
        }

        try {
            // ChaptersDAO.insertChapter(namefield.getText(), Integer.parseInt(numfield.getText()), "", currentCourse.getId());
            currentCourse.setNumsections(currentCourse.getNumsections() + 1);
            CourseDAO.updateCourse((int) currentCourse.getCourseid(),currentCourse.getFullname(), currentCourse.getShortname(), currentCourse.getShortname(), currentCourse.getNumsections(), currentCourse.getNumsections());
            System.out.println("Course created successfully.");

            FXMLLoader coursVloader = new FXMLLoader(CreateChapterDialogController.class.getResource("/com/example/moodle/FXML/CourseViewPanel_updated.fxml"));
            AnchorPane coursV = coursVloader.load();

            root.setCenter(coursV);

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

    public String getChapName() {
        return namefield.getText();
    }
}
