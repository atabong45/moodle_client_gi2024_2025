package com.example.moodle.moodleclient;

import com.example.moodle.Entities.Course;
import javafx.scene.layout.BorderPane;

public class Moodleclient {
    public static BorderPane root;
    public static client_moodle user;
    public static String serverAddress = "http://localhost/";
    public static String superToken = "3aa242ca65df48c7f36f35de206846c0";
    public static String token = "";

    public static Course currentCourse;
}

