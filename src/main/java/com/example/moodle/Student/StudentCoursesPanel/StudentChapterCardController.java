package com.example.moodle.Student.StudentCoursesPanel;

import com.example.moodle.Entities.Module;
import com.example.moodle.Entities.Section;
import com.example.moodle.api.FileHelper;
import com.example.moodle.api.ModuleHelper;
import com.example.moodle.dao.CourseDAO;
import com.example.moodle.dao.DocumentsFilesDAO;
import com.example.moodle.dao.FileDAO;
import com.example.moodle.dao.ModuleDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.example.moodle.DBConnection.*;
import static com.example.moodle.moodleclient.Moodleclient.currentCourse;


public class StudentChapterCardController implements Initializable {

    @FXML
    private Label FilesNumber;

    @FXML
    private VBox FilesVbox;

    @FXML
    private Text chapterName;

    @FXML
    private Label chapterNum;

    public Section section;

    private int NumFiles;

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        NumFiles = 0;
        FilesVbox.getChildren().clear();

    }

    private void loadDocumentsFilesFromDatabase() {
        ArrayList<com.example.moodle.Entities.File> files = new ArrayList<>();
        ArrayList<Module> modules = ModuleDAO.getModules(section.getSectionid());
        if(modules.size() == 0) {
            ModuleHelper moduleHelper = new ModuleHelper();
            modules = moduleHelper.getModules(currentCourse.getCourseid(), section.getSectionid());
            if(modules.size() != 0) {
                for (Module module: modules) {
                    ModuleDAO.insertModule(module);
                }
            }
        }

        NumFiles = 0;
        FilesVbox.getChildren().clear();

        for (Module module: modules) {
            if (module.getModplural().equals("Files") || module.getModname().equals("assign")) {
                com.example.moodle.Entities.File file = FileDAO.getFile(module.getCmid());
                if(file == null) {
                    FileHelper fileHelper = new FileHelper();
                    file = fileHelper.getFile(currentCourse.getCourseid(), section.getSectionid(), module.getCmid());
                    if(file != null) {
                        FileDAO.insertFile(file);
                    }
                }
                if(file != null) {
                    NumFiles++;
                    FilesNumber.setText(NumFiles+"");
                    if (module.getModname().equals("assign")) {
                        FilesVbox.getChildren().add(docLine(file.getFilename(), readableFileSize(file.getFilesize()), getFileType(file.getFilename()), file.getFilepath(), true));
                    } else {
                        FilesVbox.getChildren().add(docLine(file.getFilename(), readableFileSize(file.getFilesize()), getFileType(file.getFilename()), file.getFilepath(), false));
                    }
                }

            }
        }
    }

    private String getFileType(String fileName) {
        if (fileName.endsWith(".pdf")) {
            return "PDF Document";
        } else if (fileName.endsWith(".doc") || fileName.endsWith(".docx")) {
            return "Word Document";
        } else if (fileName.endsWith(".ppt") || fileName.endsWith(".pptx")) {
            return "Presentation Doc";
        } else if (fileName.endsWith(".xls") || fileName.endsWith(".xlsx")) {
            return "Sheet Document";
        } else {
            return "Other";
        }
    }

    private String readableFileSize(long size) {
        if (size <= 0) return "0 B";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public void define(String name, String num, int nbfichiers, Section section) {
        this.chapterName.setText(name);
        this.chapterNum.setText(num);
        this.chapterNum.setStyle("-fx-text-fill: black");
        this.FilesNumber.setText(nbfichiers+"");
        this.FilesNumber.setStyle("-fx-text-fill: black");
        this.section = section;

        loadDocumentsFilesFromDatabase();
    }

    public void define(String name) {
        this.chapterName.setText(name);
    }

    public HBox docLine(String fileName, String readableFileSize, String fileType, String filePath, boolean isAssignment){
        HBox line = new HBox();
        Region region = new Region();
        Label path = new Label(filePath);
        Label before = new Label("\uD83D\uDCDD");
        before.setStyle("-fx-background-color:#e2e2e2; -fx-text-fill:#303030; -fx-border-radius: 50;");

        Text name = new Text(fileName);

        Label type = new Label(fileType);
        type.setStyle("-fx-background-color:#fff0; -fx-text-fill:#000000;");
        type.setPrefWidth(120);


        line.setPrefWidth(FilesVbox.getWidth());
        line.setPrefHeight(HBox.USE_COMPUTED_SIZE);
        line.setSpacing(20);

        HBox.setHgrow(region, Priority.ALWAYS);
        region.setPrefHeight(Region.USE_COMPUTED_SIZE);
        path.setMaxWidth(50);
        path.setVisible(false);

        if(!isAssignment) {
            before.setText("📕");
        }

        line.getChildren().addAll(before, name, path, region, new Text(readableFileSize), type);

        line.setOnMouseClicked(event -> handleDocClick(line, event));

        return line;
    }

    private void handleDocClick(HBox hbox, MouseEvent event) {
        if (event.getClickCount() == 2) {
            Optional<Node> path = hbox.getChildren().stream()
                    .filter(node -> node instanceof Label)
                    .findFirst();
            if(path.isPresent()) {
                Label label = (Label) path.get();
                openFile(label.getText());
            }
        }

    }

    private void openFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            try {
                Desktop.getDesktop().open(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
