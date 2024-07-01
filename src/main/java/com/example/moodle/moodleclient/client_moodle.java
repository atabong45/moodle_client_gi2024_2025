package com.example.moodle.moodleclient;

public class client_moodle {
    public long id;
    public static String username;
    public boolean status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void  is_Teacher(boolean isTeacher) {
        status = isTeacher;
    }

    public boolean is_Teacher(){
        return status;
    }
}
