package com.example.moodle.Student.StudentCoursesPanel;

import com.example.moodle.Entities.User;
import com.example.moodle.MainDry.Dry;
import com.example.moodle.Entities.Section;
import com.example.moodle.Student.Cards.SectionCard;
import com.example.moodle.Student.Cards.UserCard;
import com.example.moodle.api.CourseHelper;
import com.example.moodle.api.SectionHelper;
import com.example.moodle.dao.SectionDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.example.moodle.DBConnection.*;
import static com.example.moodle.moodleclient.Moodleclient.currentCourse;
import static com.example.moodle.moodleclient.Moodleclient.root;

public class StudentCourseViewController implements Initializable {

    @FXML
    private VBox ChaptersVbox;

    @FXML
    private Button ChaptersBtn;

    @FXML
    private Button ParticipantsBtn;

    @FXML
    private TextArea courseDescription;

    @FXML
    private Label coursename;

    @FXML
    private Label returnArrow;

    @FXML
    private VBox leftbtnMenu;

    @FXML
    private ScrollPane scrollpane;

    @FXML
    private Text chaptersTitle;

    public static List<SectionCard> Chapterslist;
    public static List<UserCard> UsersList;

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        coursename.setText(currentCourse.getFullname());
        courseDescription.setText(currentCourse.getSummary());

        Chapterslist = new ArrayList<>();
        UsersList = new ArrayList<>();
        ChaptersVbox.getChildren().clear();
        hideAll();
    }


    @FXML
    void handleChaptersBtn(ActionEvent event) {
        selectBtn(leftbtnMenu, ChaptersBtn);
        courseDescription.setVisible(false);
        chaptersTitle.setVisible(true);
        ChaptersVbox.setVisible(true);
        ChaptersVbox.getChildren().clear();
        Chapterslist.clear();

        loadSectionsFromDatabase();

    }

    @FXML
    void handleParticipantsBtn(ActionEvent event) {
        ChaptersVbox.setVisible(true);
        selectBtn(leftbtnMenu, ParticipantsBtn);
        courseDescription.setVisible(false);
        chaptersTitle.setVisible(false);
        ChaptersVbox.getChildren().clear();
        UsersList.clear();

        getParticipants();

    }

    @FXML
    void handleReturn(MouseEvent event) {
        try {
            FXMLLoader contentLoader = new FXMLLoader(Dry.class.getResource("/com/example/moodle/FXML/CoursePanel_updated.fxml"));
            AnchorPane content = contentLoader.load();
            root.setCenter(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addChapterCard(SectionCard chap){

        Chapterslist.add(chap);
        ChaptersVbox.getChildren().clear();

        for (SectionCard ch : Chapterslist){
            ChaptersVbox.getChildren().add(ch);
        }
    }

    public  void addUserCard(UserCard userCard) {
        UsersList.add(userCard);
        ChaptersVbox.getChildren().clear();

        for (UserCard ch : UsersList){
            ChaptersVbox.getChildren().add(ch);
        }
    }

    public void getParticipants() {
        CourseHelper courseHelper = new CourseHelper();
        ArrayList<User> users = courseHelper.getParticipants(currentCourse.getCourseid());
        for (User user: users) {
            ChaptersVbox.getChildren().clear();
            addUserCard(new UserCard(user));
        }

    }

    public void loadSectionsFromDatabase() {
        ArrayList<Section> sections = SectionDAO.getSections(currentCourse.getCourseid());
        if(sections.size() == 0) {
            SectionHelper sectionHelper = new SectionHelper();
            sections = sectionHelper.getSections(currentCourse.getCourseid());
            if(sections.size() != 0) {
                for(Section section: sections) {
                    SectionDAO.insertSection(section);
                }
            }
        }
        for (Section section: sections) {
            ChaptersVbox.getChildren().clear();
            addChapterCard(new SectionCard(section));
        }
    }

    void selectBtn(VBox VB, Button button) {
        button.getStyleClass().add("focused");
        for (Node node : VB.getChildren()) {
            if(node instanceof Button) {
                Button btn = (Button) node;
                if(!btn.equals(button)) {
                    btn.getStyleClass().remove("focused");
                }
            }
        }
    }

    public int getChaptersCount() {
        return Chapterslist.size();
    }

    void hideAll() {
        ChaptersVbox.setVisible(false);
        chaptersTitle.setVisible(false);
    }
}
