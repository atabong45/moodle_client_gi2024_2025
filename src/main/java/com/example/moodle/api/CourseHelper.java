package com.example.moodle.api;

import com.example.moodle.Entities.Course;
import com.example.moodle.Entities.User;
import com.example.moodle.moodleclient.Moodleclient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;

public class CourseHelper {
    private static final String COURSES_ENROLLED = "core_enrol_get_users_courses";
    private static final String COURSE_CONTENT = "core_course_get_contents";
    private static final String VIEW_PARTICIPANTS = "core_enrol_get_enrolled_users";
    private static final String CREATE_COURSE = "core_course_create_courses";

    public CourseHelper() {
    }

    public ArrayList<Course> getEnrolledCourses(long userid) {
        SectionHelper sectionHelper = new SectionHelper();
        ArrayList<Course> courses = new ArrayList<>();
        String urlStr = Moodleclient.serverAddress + "webservice/rest/server.php?wstoken=" + Moodleclient.superToken + "&wsfunction=" + COURSES_ENROLLED + "&moodlewsrestformat=json&userid=" + userid;
        try {
            String res = RequestHelper.formRequest(urlStr);
            if(!res.isEmpty()) {
                JSONParser parser = new JSONParser();
                JSONArray data = (JSONArray) parser.parse(res);


                for(int i = 0; i < data.size(); i++) {
                    JSONObject jsonObject = (JSONObject) data.get(i);
                    Course course = new Course();

                    course.setCourseid((Long) jsonObject.get("id"));
                    course.setShortname(jsonObject.get("shortname").toString());
                    course.setFullname(jsonObject.get("fullname").toString());
                    course.setSummary(jsonObject.get("summary").toString());
                    course.setUpdated((Long) jsonObject.get("timemodified"));
                    course.setStartdate((Long) jsonObject.get("startdate"));
                    course.setEnddate((Long) jsonObject.get("enddate"));
                    course.setStudentid(userid);
                    course.setNumsections(sectionHelper.getSections(course.getCourseid()).size());

                    courses.add(course);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return courses;
    }

    public ArrayList<User> getParticipants(long courseid) {
        UserHelper userHelper = new UserHelper();
        ArrayList<User> users = new ArrayList<>();
        String urlStr = Moodleclient.serverAddress + "webservice/rest/server.php?wstoken=" + Moodleclient.superToken + "&wsfunction=" + VIEW_PARTICIPANTS + "&moodlewsrestformat=json&courseid=" + courseid;

        try {
            String res = RequestHelper.formRequest(urlStr);
            JSONParser parser = new JSONParser();
            JSONArray data = (JSONArray) parser.parse(res);
            for (int i = 0; i < data.size(); i++) {
                JSONObject jsonObject = (JSONObject) data.get(i);

                User user = new User();
                user.setUserid((Long)jsonObject.get("id"));
                user.setUsername(jsonObject.get("username").toString());

                JSONArray roles = (JSONArray) jsonObject.get("roles");
                JSONObject role = (JSONObject) roles.get(0);

                long statut = (Long) role.get("roleid");
                if(statut == 3) {
                    user.setRole(1); // Teacher
                } else if(statut == 5) {
                    user.setRole(0); // Student
                }
                users.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    public boolean createCourse(Course course) {
        String urlStr = Moodleclient.serverAddress + "webservice/rest/server.php?wstoken=" + Moodleclient.superToken + "&wsfunction=" + CREATE_COURSE + "&moodlewsrestformat=json" +
                "&courses[0][fullname]=" + course.getFullname() + "&courses[0][shortname]=" + course.getShortname() + "&categoryid=1&courses[0][summary]=" +course.getSummary() +
                "&courses[0][numsections]=" + course.getNumsections() + "&courses[0][maxbytes]=32768";
        try {
            String res = RequestHelper.formRequest(urlStr);
            JSONParser parser = new JSONParser();
            JSONArray data = (JSONArray) parser.parse(res);
            JSONObject tab = (JSONObject) data.get(0);

            if(tab.keySet().contains("id")) return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}