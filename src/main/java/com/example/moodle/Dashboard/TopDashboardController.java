package com.example.moodle.Dashboard;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


import com.example.moodle.HelloApplication;
import com.example.moodle.Login.HelloController;

import com.example.moodle.Privatefiles.PrivateFile;
import com.example.moodle.Entities.Course;
import com.example.moodle.Teacher.entity.CoursePull;
import com.example.moodle.api.CourseHelper;
import com.example.moodle.api.RequestHelper;
import com.example.moodle.dao.CourseDAO;
import com.example.moodle.moodleclient.Moodleclient;
import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import static com.example.moodle.DBConnection.*;
import static com.example.moodle.HelloApplication.stage;
import static com.example.moodle.moodleclient.Moodleclient.*;
import static com.example.moodle.moodleclient.client_moodle.username;

public class TopDashboardController implements Initializable{

        @FXML
        private Circle connectionIndic;

        @FXML
        private CustomMenuItem editProfileMenu;

        @FXML
        private CustomMenuItem logOutMenu;

        @FXML
        private Label loginIndic;

        @FXML
        private HBox moodleLayout;

        @FXML
        private Label notifIndic;

        @FXML
        private MenuButton profileBtn;

        @FXML
        private ImageView profileImg;

        @FXML
        private ImageView profileImg_in_menu;

        @FXML
        private ProgressBar progressBar;

        @FXML
        private Button syncBtn;

        @FXML
        private ImageView syncImg;

        @FXML
        private CustomMenuItem syncMenu;

        @FXML
        private Label teacherLabel;

        @FXML
        private Tooltip tipIndic;

        @FXML
        private Label username;

    private static ObservableList<Course> courList = FXCollections.observableArrayList();
    static List<PrivateFile> privateFiles = new ArrayList<>();
    static List<Course> userCourses = new ArrayList<>();

    private static final String SERVER_ADDRESS = "http://localhost/";

    private static final String REQUEST_URL = "http://localhost/login/token.php?username=admin&password=Admin@123&service=moodle";

    @Override
        public void initialize(URL url, ResourceBundle rb) {
            if (!HelloController.isTeacher) teacherLabel.setText("Student");
            username.setText(user.getUsername());
        }

        @FXML
        void handleLogOutMenu(ActionEvent event) throws IOException {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/moodle/FXML/LOgOutDialog.fxml"));
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
        void handleProfileEdit(ActionEvent event) {
            try {
                // Load the FXML for the edit profile dialog
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/moodle/FXML/EditProfileDialog.fxml"));
                Parent root = loader.load();

                // Create a new stage for the dialog
                Stage stage = new Stage();
                stage.setTitle("Edit Profile");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(new Scene(root));
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @FXML
        void handleSyncBtn(ActionEvent event) {
            //pull des fichier des cour au quel le user est inscrit
            getCoursesOfUser();
            getdownloadfileAndStoreDB();

        //action de syncronisatiion du cours
            //SyncroniserCour();
            //action de syncronisation des fichier privés
            List<PrivateFile> privatesyncrofiles = readPrivateFilessyncro();
            for(PrivateFile prifiles: privatesyncrofiles){
                String draftId = uploadFileToDraftArea(prifiles.getFilePath());
                if (draftId != null) {
                    moveToUserPrivateFiles(draftId);
                }
            }
            // rotation du bouton
            RotateTransition btnrot = new RotateTransition(Duration.seconds(2), syncBtn);
            btnrot.setByAngle(360);
            btnrot.setCycleCount(1);
            btnrot.setAutoReverse(false);
            btnrot.play();

            // defilement de la progressbar

        }

    public static void readCoursessyncro() {
        ArrayList<Course> courses = CourseDAO.getEnrolledCourses(user.getId());
        if(courses.size() != 0) {
            for(Course course: courses) {
                courList.add(course);
            }
        }
//        String query = "SELECT id,courseName,courseAbr,courseDescription,nbChapters,nbAssignments FROM Course";
//        try (Connection connection = DriverManager.getConnection(JDBC_URL,JDBC_USERNAME, JDBC_PASSWORD);
//             Statement statement = connection.createStatement();
//             ResultSet resultSet = statement.executeQuery(query)) {
//            while (resultSet.next()) {
//                int id = resultSet.getInt("id");
//                String courseName = resultSet.getString("courseName");
//                String courseAbr = resultSet.getString("courseAbr");
//                String courseDescription = resultSet.getString("courseDescription");
//                int nbChapters = resultSet.getInt("nbChapters");
//                int nbAssignments = resultSet.getInt("nbAssignments");
//
//                System.out.println("ID: " + id + ", Course Name: " + courseName + ", Course Abbreviation: " + courseAbr +
//                        ", Description: " + courseDescription + ", Chapters: " + nbChapters + ", Assignments: " + nbAssignments);
//                courList.add(new Course( id, courseName, courseAbr,courseDescription,nbChapters,nbAssignments));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }
    public static List<PrivateFile> readPrivateFilessyncro() {
        String query = "SELECT * FROM private_files";
        try (Connection connection = DriverManager.getConnection(JDBC_URL,JDBC_USERNAME, JDBC_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String fileName = resultSet.getString("fileName");
                long fileSize = resultSet.getLong("fileSize");
                String fileType = resultSet.getString("fileType");
                String filePath = resultSet.getString("filePath");
                privateFiles.add(new PrivateFile(id, fileName, fileSize, fileType, filePath));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return privateFiles;
    }

    public void SyncroniserCour() {
        CourseHelper courseHelper = new CourseHelper();
        ArrayList<Course> coursesFromServer = courseHelper.getEnrolledCourses(user.getId());
        readCoursessyncro();
        List<PrivateFile> privatesyncrofiles = readPrivateFilessyncro();
        for(PrivateFile prifiles:privatesyncrofiles){
            String draftId = uploadFileToDraftArea(prifiles.getFilePath());
            if (draftId != null) {
                moveToUserPrivateFiles(draftId);
            }
        }

        String moodleUrl = "http://localhost/webservice/rest/server.php";
        String token = Moodleclient.token;
        String categoryid = "1";

        if(coursesFromServer.size() != 0) {
            for (Course courseFrom: coursesFromServer) {
                for (Course courseToSend: courList) {
                    if(courseFrom.getCourseid() == courseToSend.getCourseid()) {
                        courList.remove(courseToSend);
                    }
                }
            }
        }

        for (Course cour : courList) {
            System.out.println("ID: " + cour.getCourseid() + ", Course Name: " + cour.getFullname() + ", Course Abbreviation: " + cour.getShortname());
            try {
                    URL url = new URL(moodleUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    conn.setDoOutput(true);

                    String params = "moodlewsrestformat=json&wsfunction=core_course_create_courses&wstoken=" + token +
                            "&courses[0][fullname]=" + cour.getFullname() +
                            "&courses[0][shortname]=" + cour.getShortname() +
                            "&courses[0][categoryid]=" + categoryid +
                            "&courses[0][summary]=" + cour.getSummary();

                    try (OutputStream os = conn.getOutputStream()) {
                        byte[] input = params.getBytes(StandardCharsets.UTF_8);
                        os.write(input, 0, input.length);
                    }

                    int responseCode = conn.getResponseCode();
                    System.out.println("Response Code : " + responseCode);

                    if (responseCode == HttpURLConnection.HTTP_OK) {

                        System.out.println("Course synchronized: " + cour.getFullname());
                    } else {
                        System.out.println("Failed to synchronize course: " + cour.getFullname());
                    }

                    conn.disconnect();

                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }

    public static String executeRequest() throws Exception {
        URL url = new URL(REQUEST_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Extract the token from the response
            String responseStr = response.toString();
            return extractTokenFromResponse(responseStr);
        } else {
            throw new RuntimeException("Failed : HTTP error code : " + responseCode);
        }
    }

    private static String extractTokenFromResponse(String response) {
        // Assuming the response is in JSON format: {"token":"yourtoken"}
        // You can use a JSON library like org.json to parse the response
        try {
            org.json.JSONObject jsonObject = new org.json.JSONObject(response);
            return jsonObject.getString("token");
        } catch (org.json.JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String uploadFileToDraftArea(String filePath) {
        File file = new File(filePath);
        if (!file.exists() || !file.canRead()) {
            System.err.println("Cannot access file: " + filePath);
            return null;
        }

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost uploadFile = new HttpPost(SERVER_ADDRESS + "webservice/upload.php?token=" + token);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addBinaryBody("file_1", file, ContentType.APPLICATION_OCTET_STREAM, file.getName());
            HttpEntity multipart = builder.build();
            uploadFile.setEntity(multipart);

            HttpResponse response = httpClient.execute(uploadFile);
            HttpEntity responseEntity = response.getEntity();
            String responseString = EntityUtils.toString(responseEntity);

            System.out.println("Upload response: " + responseString);

            if (responseString.startsWith("[")) {
                JSONArray jsonResponse = new JSONArray(responseString);
                JSONObject jsonObject = jsonResponse.getJSONObject(0);
                return String.valueOf(jsonObject.getLong("itemid"));
            } else if (responseString.startsWith("{")) {
                JSONObject jsonResponse = new JSONObject(responseString);
                return String.valueOf(jsonResponse.getLong("itemid"));
            } else {
                System.err.println("Unexpected response format: " + responseString);
                return null;
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void moveToUserPrivateFiles(String draftId) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(SERVER_ADDRESS + "webservice/rest/server.php?moodlewsrestformat=json");
            List<NameValuePair> urlParameters = new ArrayList<>();
            urlParameters.add(new BasicNameValuePair("draftid", draftId));
            urlParameters.add(new BasicNameValuePair("wsfunction", "core_user_add_user_private_files"));
            urlParameters.add(new BasicNameValuePair("wstoken", token));

            post.setEntity(new UrlEncodedFormEntity(urlParameters));
            HttpResponse response = httpClient.execute(post);
            HttpEntity responseEntity = response.getEntity();
            String responseString = EntityUtils.toString(responseEntity);

            System.out.println("Move to private files response: " + responseString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //PULL DES COURS

    public void getCoursesOfUser() {
        CourseHelper courseHelper = new CourseHelper();
        try {
            int userId = getUserId(user.getUsername());
            if (userId != -1) {
                ArrayList<Course> coursesFromDB = CourseDAO.getEnrolledCourses(userId);
                userCourses = courseHelper.getEnrolledCourses(userId);
                if(userCourses.size() != 0) {
                    for (Course course: userCourses) {
                        boolean found = false;
                        for (Course courseFromDB: coursesFromDB) {
                            if(course.getCourseid() == courseFromDB.getCourseid()) {
                                found = true;
                                continue;
                            }
                        }
                        if(!found) CourseDAO.insertCourse(course);
                    }
                }
//                userCourses = getUserEnrolledCourses(userId);
//
//                // Print user enrolled courses
//                for (CoursePull course : userCourses) {
//                    System.out.println("Course ID: " + course.id + ", Course Name: " + course.getFullname());
//                }
            } else {
                System.out.println("User not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getdownloadfileAndStoreDB() {
        final String COURSE_CONTENT = "core_course_get_contents";
        final String GET_ASSIGNMENTS = "mod_assign_get_assignments";
        try {

            // For each course, retrieve and process course contents
            for (Course course : userCourses) {
                com.example.moodle.Entities.File file = null;
                String urlStr = Moodleclient.serverAddress + "webservice/rest/server.php?wstoken=" + Moodleclient.superToken + "&wsfunction=" + COURSE_CONTENT + "&moodlewsrestformat=json&courseid=" + course.getCourseid();
                String urlStr2 = Moodleclient.serverAddress + "webservice/rest/server.php?wstoken=" + Moodleclient.superToken + "&wsfunction=" + GET_ASSIGNMENTS + "&moodlewsrestformat=json&courseids[0]=" + course.getCourseid();
                String res = RequestHelper.formRequest(urlStr);
                JSONParser parser = new JSONParser();
                org.json.simple.JSONArray data = (org.json.simple.JSONArray) parser.parse(res);
                for(int j = 0; j < data.size(); j++) {
                    org.json.simple.JSONObject section = (org.json.simple.JSONObject) data.get(j);
                    org.json.simple.JSONArray jsonArray = (org.json.simple.JSONArray) section.get("modules");
                    for (int i = 0; i < jsonArray.size(); i++) {
                        org.json.simple.JSONObject module = (org.json.simple.JSONObject) jsonArray.get(i);
                        if ((Long) module.get("downloadcontent") == 1) {
                            file = new com.example.moodle.Entities.File();
                            org.json.simple.JSONArray contents = (org.json.simple.JSONArray) module.get("contents");
                            if (contents != null) {
                                for(int k = 0; k < contents.size(); k++) {
                                    org.json.simple.JSONObject content = (org.json.simple.JSONObject) contents.get(k);

                                    file.setFilename(content.get("filename").toString());
                                    file.setModuleid((Long) module.get("id"));
                                    file.setFilepath(content.get("filepath").toString());
                                    file.setFilesize((Long) content.get("filesize"));
                                    file.setFileurl(content.get("fileurl").toString());
                                    file.setCreated((Long) content.get("timecreated"));
                                    file.setUpdated((Long) content.get("timemodified"));
                                    file.setRepositorytype("");
                                    file.setMimetype(content.get("mimetype").toString());
                                }
                                String filePath = downloadFile(file.getFileurl(), file.getFilename());

                                // Store the file path in the database
                                storeFilePathInDatabase((int) course.getCourseid(), file.getModuleid() + "", file.getFilename(), filePath);

                                System.out.println("    File: " + file.getFilename() + " downloaded to " + filePath);

                            } else if (module.get("modname").toString().equals("assign")) {
                                try {
                                    String res2 = RequestHelper.formRequest(urlStr2);
                                    JSONParser parser2 = new JSONParser();
                                    org.json.simple.JSONObject data2 = (org.json.simple.JSONObject) parser2.parse(res2);
                                    org.json.simple.JSONArray courses = (org.json.simple.JSONArray) data2.get("courses");
                                    org.json.simple.JSONObject course2 = (org.json.simple.JSONObject) courses.get(0);
                                    org.json.simple.JSONArray array = (org.json.simple.JSONArray) course2.get("assignments");
                                    for (int k = 0; k < array.size(); k++) {
                                        org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) array.get(k);
                                        file = new com.example.moodle.Entities.File();
                                        org.json.simple.JSONArray filesTab = (org.json.simple.JSONArray) jsonObject.get("introattachments");
                                        org.json.simple.JSONObject fileObject = (org.json.simple.JSONObject) filesTab.get(0);
                                        file.setFilename(fileObject.get("filename").toString());
                                        file.setModuleid((Long) jsonObject.get("cmid"));
                                        file.setFilepath(fileObject.get("filepath").toString());
                                        file.setFilesize((Long) fileObject.get("filesize"));
                                        file.setFileurl(fileObject.get("fileurl").toString());
                                        file.setUpdated((Long) fileObject.get("timemodified"));
                                        file.setMimetype(fileObject.get("mimetype").toString());
                                    }
                                    // Download the file
                                String filePath = downloadFile(file.getFileurl(), file.getFilename());

                                // Store the file path in the database
                                storeFilePathInDatabase((int) course.getCourseid(), file.getModuleid() + "", file.getFilename(), filePath);

                                System.out.println("    File: " + file.getFilename() + " downloaded to " + filePath);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
//                JSONArray courseContents = getCourseContents(course.getCourseid());
//
//                for (int i = 0; i < courseContents.length(); i++) {
//                    JSONObject topic = courseContents.getJSONObject(i);
//                    String topicName = topic.getString("name");
//                    System.out.println("Topic: " + topicName);
//
//                    JSONArray modules = topic.getJSONArray("modules");
//                    for (int j = 0; j < modules.length(); j++) {
//                        JSONObject module = modules.getJSONObject(j);
//                        if (module.has("contents")) {
//                            JSONArray contents = module.getJSONArray("contents");
//
//                            for (int k = 0; k < contents.length(); k++) {
//                                JSONObject content = contents.getJSONObject(k);
//                                String fileName = content.getString("filename");
//                                String fileUrl = content.getString("fileurl");
//
//                                // Download the file
//                                String filePath = downloadFile(fileUrl, fileName);
//
//                                // Store the file path in the database
//                                storeFilePathInDatabase((int) course.getCourseid(), topicName, fileName, filePath);
//
//                                System.out.println("    File: " + fileName + " downloaded to " + filePath);
//                            }
//                        }
//                    }
//                }
//            }
//
        } catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private static int getUserId(String username) throws Exception {
        String urlString = "http://localhost/webservice/rest/server.php?moodlewsrestformat=json&wsfunction=core_user_get_users&wstoken=" + superToken + "&criteria[0][key]=username&criteria[0][value]=" + user.getUsername();
        JSONObject response = getJsonObjectFromUrl(urlString);
        JSONArray usersArray = response.getJSONArray("users");

        if (usersArray.length() > 0) {
            JSONObject userJson = usersArray.getJSONObject(0);
            return userJson.getInt("id");
        } else {
            return -1; // User not found
        }
    }

    private static List<CoursePull> getUserEnrolledCourses(int userId) throws Exception {
        String urlString = "http://localhost/webservice/rest/server.php?moodlewsrestformat=json&wsfunction=core_enrol_get_users_courses&wstoken=" + superToken + "&userid=" + userId;
        JSONArray coursesArray = getJsonArrayFromUrl(urlString);

        List<CoursePull> courses = new ArrayList<>();
        for (int i = 0; i < coursesArray.length(); i++) {
            JSONObject courseJson = coursesArray.getJSONObject(i);
            CoursePull course = new CoursePull(
                    courseJson.getInt("id"),
                    courseJson.getString("fullname"),
                    courseJson.getString("shortname"),
                    courseJson.getLong("startdate"),
                    courseJson.getLong("enddate")

            );
            courses.add(course);
        }
        return courses;
    }

    private static JSONObject getJsonObjectFromUrl(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }

        in.close();
        conn.disconnect();

        return new JSONObject(content.toString());
    }

    private static JSONArray getJsonArrayFromUrl(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }

        in.close();
        conn.disconnect();

        return new JSONArray(content.toString());
    }


    private static JSONArray getCourseContents(long courseId) throws Exception {
        String urlString = "http://localhost/webservice/rest/server.php?moodlewsrestformat=json&wsfunction=core_course_get_contents&wstoken=" + superToken + "&courseid=" + courseId;
        return getJsonArrayFromUrl(urlString);
    }

    private static String downloadFile(String fileUrl, String fileName) throws Exception {
        URL url = new URL(fileUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        String filePath = "D:\\downloadedmoddlefiles\\" + fileName;  // Define your file path here
        try (InputStream in = conn.getInputStream();
             FileOutputStream out = new FileOutputStream(filePath)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
        conn.disconnect();
        return filePath;
    }

    private static void storeFilePathInDatabase(int courseId, String topicName, String fileName, String filePath) throws SQLException {
        Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);
        String query = "INSERT INTO downloaded_files (course_id, topic_name, file_name, file_path) VALUES (?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, courseId);
            preparedStatement.setString(2, topicName);
            preparedStatement.setString(3, fileName);
            preparedStatement.setString(4, filePath);
            preparedStatement.executeUpdate();
        }
    }

    private static boolean saveFileToCourseModule(String draftItemId, int contextId, int moduleId, String fileName, String filePath) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost postRequest = new HttpPost(SERVER_ADDRESS + "webservice/rest/server.php?wstoken=" + superToken + "&moodlewsrestformat=json&wsfunction=core_files_upload");

            // Construct the JSON payload
            JSONObject jsonPayload = new JSONObject();
            jsonPayload.put("contextid", contextId);
            jsonPayload.put("component", "mod_resource");
            jsonPayload.put("filearea", "content");
            jsonPayload.put("itemid", draftItemId);
            jsonPayload.put("filepath", filePath);
            jsonPayload.put("filename", fileName);
            jsonPayload.put("contextlevel", "module");
            jsonPayload.put("instanceid", moduleId);

            StringEntity entity = new StringEntity(jsonPayload.toString());
            postRequest.setEntity(entity);
            postRequest.setHeader("Content-Type", "application/json");

            CloseableHttpResponse response = httpClient.execute(postRequest);
            String responseString = EntityUtils.toString(response.getEntity());
            System.out.println("Move file response: " + responseString);

            JSONObject jsonResponse = new JSONObject(responseString);
            return jsonResponse.has("contextid");

        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return false;
        }
    }


    }
