package com.example.moodle.moodleclient;

import com.example.moodle.Entities.Course;
import javafx.scene.layout.BorderPane;

public class Moodleclient {
    public static BorderPane root;
    public static client_moodle user;
    public static String serverAddress = "http://localhost/";
    public static String superToken = "821e727e3278eeba54df0d42fb481048";
    public static String token = "";

    public static Course currentCourse;
}

