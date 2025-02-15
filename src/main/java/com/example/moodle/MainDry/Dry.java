package com.example.moodle.MainDry;

import com.example.moodle.moodleclient.Moodleclient;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;

import java.io.IOException;
public class Dry {
    public static FXMLLoader contentLoader;

    public Dry() {

    }

    public static void showDashboard(BorderPane root, boolean isTeacher) throws IOException {
        if (isTeacher) {
            FXMLLoader loader = new FXMLLoader(Dry.class.getResource("/com/example/moodle/FXML/topDashboard_updated.fxml"));
            AnchorPane topMenu = loader.load();

            Label username = (Label) loader.getNamespace().get("username");
            Circle circle = (Circle) loader.getNamespace().get("connectionIndic");
            Tooltip tooltip = (Tooltip) loader.getNamespace().get("tipIndic");
            if(!Moodleclient.token.isEmpty()) {
                circle.setStyle("-fx-fill: green;");
                tooltip.setText("Online");
            }
            //username.setText(moodleclient.Moodleclient.user.getUsername());

            root.setTop(topMenu);


            FXMLLoader leftLoader = new FXMLLoader(Dry.class.getResource("/com/example/moodle/FXML/leftDashboard_updated.fxml"));
            AnchorPane leftMenu = leftLoader.load();
            root.setLeft(leftMenu);
            // AnchorPane rightMenu =  (AnchorPane)FXMLLoader.load(Dry.class.getResource("/SDashboard/rightDashboard.fxml"));
            // root.setRight(rightMenu);

            //Le leftMenu se charge de mettre à jour le contenu central donc, plus besoin de ceci.
            FXMLLoader contentLoader = new FXMLLoader(Dry.class.getResource("/com/example/moodle/FXML/CoursePanel_updated.fxml"));
            AnchorPane content = contentLoader.load();
            root.setCenter(content);
        } else {
            FXMLLoader loader = new FXMLLoader(Dry.class.getResource("/com/example/moodle/FXML/topDashboard_updated.fxml"));
            AnchorPane topMenu = loader.load();

            Label username = (Label) loader.getNamespace().get("username");
            Circle circle = (Circle) loader.getNamespace().get("connectionIndic");
            Tooltip tooltip = (Tooltip) loader.getNamespace().get("tipIndic");
            if(!Moodleclient.token.isEmpty()) {
                circle.setStyle("-fx-fill: green;");
                tooltip.setText("Online");
            }

            //username.setText(moodleclient.Moodleclient.user.getUsername());

            root.setTop(topMenu);


            FXMLLoader leftLoader = new FXMLLoader(Dry.class.getResource("/com/example/moodle/FXML/student_leftDashboard_updated.fxml"));
            AnchorPane leftMenu = leftLoader.load();
            root.setLeft(leftMenu);
            // AnchorPane rightMenu =  (AnchorPane)FXMLLoader.load(Dry.class.getResource("/SDashboard/rightDashboard.fxml"));
            // root.setRight(rightMenu);

            //Le leftMenu se charge de mettre à jour le contenu central donc, plus besoin de ceci.
            FXMLLoader contentLoader = new FXMLLoader(Dry.class.getResource("/com/example/moodle/FXML/student_CoursesPanel_updated.fxml"));
            AnchorPane content = contentLoader.load();
            root.setCenter(content);
        }
    }
}
       // FXMLLoader leftLoader = new FXMLLoader(Dry.class.getResource("/com/example/moodle/FXML/leftDashboard_updated.fxml"));
        //AnchorPane leftMenu = leftLoader.load();
        //root.setLeft(leftMenu);
        // AnchorPane rightMenu =  (AnchorPane)FXMLLoader.load(Dry.class.getResource("/SDashboard/rightDashboard.fxml"));
        // root.setRight(rightMenu);
        
        //Le leftMenu se charge de mettre à jour le contenu central donc, plus besoin de ceci.

        //FXMLLoader leftLoader = new FXMLLoader(Dry.class.getResource("/com/example/moodle/FXML/leftDashboard_updated.fxml"));
        //AnchorPane leftMenu = leftLoader.load();
        //root.setLeft(leftMenu);
        //// AnchorPane rightMenu =  (AnchorPane)FXMLLoader.load(Dry.class.getResource("/SDashboard/rightDashboard.fxml"));
        //// root.setRight(rightMenu);
        //
        ////Le leftMenu se charge de mettre à jour le contenu central donc, plus besoin de ceci.
        //FXMLLoader contentLoader = new FXMLLoader(Dry.class.getResource("/com/example/moodle/FXML/CoursesPanel_updated.fxml"));
        //AnchorPane content = contentLoader.load();
        //root.setCenter(content);


       // FXMLLoader contentLoader = new FXMLLoader(Dry.class.getResource("/com/example/moodle/FXML/CoursesPanel_updated.fxml"));
        //AnchorPane content = contentLoader.load();
        //root.setCenter(content);

       /* FXMLLoader contentLoader = new FXMLLoader(Dry.class.getResource("/com/example/moodle/FXML/CoursePanel_updated.fxml"));

        AnchorPane content = contentLoader.load();
        root.setCenter(content);*/




