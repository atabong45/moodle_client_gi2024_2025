package com.example.moodle.moodleclient;

import com.example.moodle.Entities.Course;
import javafx.scene.layout.BorderPane;

public class Moodleclient {
    public static BorderPane root;
    public static client_moodle user;
    public static String serverAddress = "http://localhost/";
    public static String superToken = "f1579cf1651a83cabfcc6791f43df37c";

    public static Course currentCourse;
}

