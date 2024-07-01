package com.example.moodle.Teacher.CoursesPanel;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.example.moodle.Entities.Course;
import com.example.moodle.Teacher.Cards.CourseCard;
import com.example.moodle.api.CourseHelper;
import com.example.moodle.dao.CourseDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import static com.example.moodle.DBConnection.*;
import static com.example.moodle.moodleclient.Moodleclient.user;

public class CoursesPanelController implements Initializable {

    @FXML
    private Label labelNewCourse;

    @FXML
    private Label returnArrow;

    @FXML
    private ScrollPane scrollpane;

    @FXML
    private GridPane gridpane;

    private static ArrayList<CourseCard> list;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        list = new ArrayList<>();
        loadCoursesFromDatabase();

    }

    @FXML
    void handleNewCourse(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/moodle/FXML/CreateCourseDialog.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(new Scene(root));
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addCourseCard(CourseCard courseCard) {
        list.add(courseCard);
        gridpane.getChildren().clear();

        int col=0, row=0;
        for (CourseCard card : list) {
            gridpane.add(card, col, row);
            col++;

            int columns = 4; //(int) Math.floor(scrollpane.getPrefWidth() / 10);
            if (col >= columns) {
                col = 0;
                row++;
            }
        }
    }

    private void loadCoursesFromDatabase() {
        ArrayList<com.example.moodle.Entities.Course> courses = CourseDAO.getEnrolledCourses(user.getId());
        if(courses.size() == 0) {
            CourseHelper courseHelper = new CourseHelper();
            courses = courseHelper.getEnrolledCourses(user.getId());
            for(Course course: courses) {
                CourseDAO.insertCourse(course);
            }
        }
        for (com.example.moodle.Entities.Course course: courses) {
            addCourseCard(new CourseCard(course));
        }
    }

    public int getCoursesCount() {
        return list.size();
    }
}
