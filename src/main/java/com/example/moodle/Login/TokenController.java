package com.example.moodle.Login;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class TokenController implements Initializable {
    public Label TokenLabel;
    public TextField Token;
    public Button save;
    public Button cancel1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void handleCancelEvent(MouseEvent event) {
        Stage stage = (Stage) Token.getScene().getWindow();
        stage.close();
    }
     public void handleCancelAction(ActionEvent event) {
         Stage stage = (Stage) Token.getScene().getWindow();
         stage.close();
     }

    public void handleSave(ActionEvent actionEvent) {

    }


}
