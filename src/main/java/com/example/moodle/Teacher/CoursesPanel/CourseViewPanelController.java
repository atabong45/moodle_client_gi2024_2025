package com.example.moodle.Teacher.CoursesPanel;

import com.example.moodle.Entities.Section;
import com.example.moodle.MainDry.Dry;
import com.example.moodle.Teacher.Cards.SectionCard;
import com.example.moodle.Teacher.entity.Chapter;
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
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.example.moodle.DBConnection.*;
import static com.example.moodle.moodleclient.Moodleclient.currentCourse;
import static com.example.moodle.moodleclient.Moodleclient.root;

public class CourseViewPanelController implements Initializable {

    @FXML
    private Button AssignmentsBtn;

    @FXML
    private VBox ChaptersVbox;

    @FXML
    private Button ChaptersBtn;

    @FXML
    private Button CourseFilesBtn;

    @FXML
    private Label EditCourse;

    @FXML
    private Button ParticipantsBtn;

    @FXML
    private TextArea courseDescription;

    @FXML
    private Label coursename;

    @FXML
    private Label deleteCourse;

    @FXML
    private GridPane gridpane;

    @FXML
    private Label returnArrow;

    @FXML
    private VBox leftbtnMenu;

    @FXML
    private Button newChapBtn;

    @FXML
    private ScrollPane scrollpane;

    @FXML
    private Text chaptersTitle;

    public static List<SectionCard> Chapterslist;

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        coursename.setText(currentCourse.getFullname());
        courseDescription.setText(currentCourse.getSummary());

        Chapterslist = new ArrayList<>();
        ChaptersVbox.getChildren().clear();
        hideAll();
    }

    @FXML
    void handleAssignmentsBtn(ActionEvent event) {
        ChaptersVbox.setVisible(true);
        selectBtn(leftbtnMenu, AssignmentsBtn);
        ChaptersVbox.getChildren().clear();
        Chapterslist.clear();
        newChapBtn.setVisible(false);
        newChapBtn.setDisable(true);

        loadAssignmentsFromDataBase();

    }

    @FXML
    void handleChaptersBtn(ActionEvent event) {
        selectBtn(leftbtnMenu, ChaptersBtn);
        chaptersTitle.setVisible(true);
        ChaptersVbox.setVisible(true);
        newChapBtn.setVisible(true);
        newChapBtn.setDisable(false);
        ChaptersVbox.getChildren().clear();
        Chapterslist.clear();

        loadChaptersFromDatabase();

    }

    @FXML
    void handleCourseFilesBtn(ActionEvent event) {
        ChaptersVbox.setVisible(false);
        selectBtn(leftbtnMenu, CourseFilesBtn);
        ChaptersVbox.getChildren().clear();

    }

    @FXML
    void handleParticipantsBtn(ActionEvent event) {
        ChaptersVbox.setVisible(false);
        selectBtn(leftbtnMenu, ParticipantsBtn);
        ChaptersVbox.getChildren().clear();

    }

    @FXML
    void handleDeleteCourse(MouseEvent event) {

    }

    @FXML
    void handleEditCourse(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/moodle/FXML/EditCourseDialog.fxml"));
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

    @FXML
    void handleCreateChapter(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/moodle/FXML/CreateChapterDialog.fxml"));
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

    public void addChapterCard(SectionCard chap){

        Chapterslist.add(chap);
        ChaptersVbox.getChildren().clear();

        for (SectionCard ch : Chapterslist){
            ChaptersVbox.getChildren().add(ch);
        }
    }

    public void loadChaptersFromDatabase() {
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

    public void loadAssignmentsFromDataBase() {
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
        newChapBtn.setVisible(false);
        newChapBtn.setDisable(true);
    }
}
