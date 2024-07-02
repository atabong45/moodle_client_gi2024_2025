package com.example.moodle.Teacher.Cards;

import com.example.moodle.Entities.User;
import com.example.moodle.Teacher.CoursesPanel.ChapterCard.ChapterCardController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class UserCard extends AnchorPane {
    private AnchorPane pane;
    public UserCard(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(SectionCard.class.getResource("/com/example/moodle/FXML/ChapterCard.fxml"));
            this.pane = loader.load();
            ChapterCardController cardController = loader.getController();
            cardController.define(user.getUsername());
            getChildren().add(this.pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
