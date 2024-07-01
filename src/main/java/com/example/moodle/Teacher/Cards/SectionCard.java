package com.example.moodle.Teacher.Cards;

import com.example.moodle.Entities.Section;
import com.example.moodle.Teacher.CoursesPanel.ChapterCard.ChapterCardController;
import com.example.moodle.Teacher.entity.Chapter;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class SectionCard extends AnchorPane {
    private Chapter chap;
    private AnchorPane pane;

    public SectionCard(Section section) {
        try {
            FXMLLoader loader = new FXMLLoader(SectionCard.class.getResource("/com/example/moodle/FXML/ChapterCard.fxml"));
            this.pane = loader.load();
            ChapterCardController cardController = loader.getController();
            cardController.define(section.getSectionname(), section.getSectionid()+"", 0, section);
            getChildren().add(this.pane);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
