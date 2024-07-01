package com.example.moodle.Teacher.CoursesPanel.DialogWindows;

import com.example.moodle.Teacher.Cards.CourseCard;
import com.example.moodle.Entities.Course;
import com.example.moodle.Teacher.CoursesPanel.CoursesPanelController;
import com.example.moodle.dao.CourseDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import static com.example.moodle.moodleclient.Moodleclient.root;

public class CreateCourseDialogController implements Initializable {

    @FXML
    private TextArea descriptionfield;

    @FXML
    private TextField namefield;

    @FXML
    private TextField shortnamefield;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

//    @FXML
//    void createCourse(ActionEvent event) {
//
//        int nbChapters = 0;
//        int nbAssignments = 0;
//
//        if (namefield.getText().isEmpty() || shortnamefield.getText().isEmpty() || descriptionfield.getText().isEmpty()) {
//            System.out.println("All fields must be filled out!");
//            return;
//        }
//
//        try {
//            CourseDAO.insertCourse(namefield.getText(), shortnamefield.getText(), descriptionfield.getText(), nbChapters, nbAssignments);
//            System.out.println("Course created successfully.");
//
//            FXMLLoader coursesloader = new FXMLLoader(CreateCourseDialogController.class.getResource("/com/example/moodle/FXML/CoursePanel_updated.fxml"));
//            AnchorPane courses = coursesloader.load();
//
//            CoursesPanelController CourseCtrler = coursesloader.getController();
//            Course course = new Course();
//            course.ge
//            CourseCtrler.addCourseCard(new CourseCard(new Course(
//                    CourseCtrler.getCoursesCount(),
//                    namefield.getText(),
//                    shortnamefield.getText(),
//                    descriptionfield.getText(),
//                    nbChapters,
//                    nbAssignments
//            )));
//
//            root.setCenter(courses);
//
////            CourseDAO.insertCourse(courseName, courseAbr, courseDescription, nbChapters, nbAssignments);
////            System.out.println("Course created successfully.");
////
////            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/moodle/FXML/AvailableCourseCard_updated.fxml"));
////            Pane courseCard = loader.load();
////
////            AvailableCourseCardController controller = loader.getController();
////            controller.setCourseDetails(courseName, courseDescription, nbChapters);
////
////            if (coursesPanelController != null) {
////                coursesPanelController.addCourseCard(courseCard);
////            }
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        Stage stage = (Stage) namefield.getScene().getWindow();
//        stage.close();
//    }

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

    public String getCourseName() {
        return namefield.getText();
    }
}
