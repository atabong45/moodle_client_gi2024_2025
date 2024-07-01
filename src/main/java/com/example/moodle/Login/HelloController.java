package com.example.moodle.Login;

import static com.example.moodle.moodleclient.Moodleclient.root;
import static com.example.moodle.moodleclient.Moodleclient.user;

import com.example.moodle.HelloApplication;
import com.example.moodle.MainDry.Dry;

import com.example.moodle.moodleclient.client_moodle;
import com.example.moodle.Entities.User;
import com.example.moodle.api.UserHelper;
import com.example.moodle.dao.UsersDAO;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class HelloController implements Initializable {


    @FXML
    private Label errmsg;

    @FXML
    private Label tryconnect;

    @FXML
    public ImageView image1;

    @FXML
    private JFXTextField username1;

    @FXML
    private JFXPasswordField password1;

    @FXML
    private JFXRadioButton teacher1;

    @FXML
    private JFXRadioButton student1;

    public static boolean isTeacher;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        user = new client_moodle();

        this.errmsg.setText("");
        this.errmsg.setVisible(false);
        this.tryconnect.setVisible(false);

    }

    private boolean checkCredentials(String userName, String pass, int isTeacher) {
        ArrayList<User> users = UsersDAO.getUsers();
        System.out.println(users.size());
        if(users.size() == 0) {
            UserHelper userHelper = new UserHelper();
            User user = userHelper.getUser(userName, pass, isTeacher);
            if(user == null) {
                return false;
            }
            UsersDAO.insertUser(user);
            return true;
        }
        User user = UsersDAO.findUser(userName);
        if (user == null) {
            UserHelper userHelper = new UserHelper();
            user = userHelper.getUser(userName, pass, isTeacher);
            if(user == null) {
                return false;
            }
            UsersDAO.insertUser(user);
            return true;
        }
        if(pass.equals(user.getPassword())) {
            return true;
        }
        return false;
    }

    @FXML
    private void handleLoginBtn(ActionEvent event) throws IOException {
        String userName = username1.getText();
        String pass = password1.getText();
        boolean isStudent = student1.isSelected();
        int isTeacher = teacher1.isSelected() ? 1 : 0;

        if (userName.isEmpty() || pass.isEmpty() || (!student1.isSelected() && !teacher1.isSelected())) {
            errmsg.setText("You must enter a username, password, and select a status!");
            errmsg.setVisible(true);
            return;
        }

        errmsg.setVisible(false);

        if (checkCredentials(userName, pass, isTeacher)) {
            tryconnect.setText("Connected successfully!");
            tryconnect.setVisible(true);

            user.setUsername(userName);

            if (student1.isSelected()) {

                user.is_Teacher(false);

                HelloController.isTeacher = false;
                root = new BorderPane();
                Dry.showDashboard(root, false);
                Scene scene = new Scene(root, 1180, 707);

                HelloApplication.stage.setTitle("Moodle Client");
                HelloApplication.stage.setScene(scene);
                HelloApplication.stage.show();
            } else if (teacher1.isSelected()) {

                user.is_Teacher(true);

                HelloController.isTeacher = true;
                root = new BorderPane();
                Dry.showDashboard(root, true);
                Scene scene = new Scene(root, 1180, 707);

                HelloApplication.stage.setTitle("Moodle Client");
                HelloApplication.stage.setScene(scene);
                HelloApplication.stage.show();
            }
        } else {
            errmsg.setText("User does not exist!");
            errmsg.setVisible(true);
        }
    }
}
