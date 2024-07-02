package com.example.moodle.Teacher.CoursesPanel.ChapterCard;

import com.example.moodle.Entities.Module;
import com.example.moodle.Entities.Section;
import com.example.moodle.Teacher.CoursesPanel.CourseViewPanelController;
import com.example.moodle.Teacher.CoursesPanel.DialogWindows.CreateChapterDialogController;
import com.example.moodle.Teacher.entity.Chapter;
import com.example.moodle.Teacher.entity.DocumentFile;
import com.example.moodle.api.FileHelper;
import com.example.moodle.api.ModuleHelper;
import com.example.moodle.dao.CourseDAO;
import com.example.moodle.dao.DocumentsFilesDAO;
import com.example.moodle.dao.FileDAO;
import com.example.moodle.dao.ModuleDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import javafx.scene.paint.Color;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.example.moodle.DBConnection.*;
import static com.example.moodle.moodleclient.Moodleclient.currentCourse;


public class ChapterCardController implements Initializable {

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

    @FXML
    void addCoursefiles(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose files");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All doc files", "*.pdf", "*.PDF", "*.doc", "*.docx", "*.odt", "*.ppt", "*.pptx", "*.xls", "*.xlsx"),
                new FileChooser.ExtensionFilter("PDF files", "*.pdf", "*.PDF"),
                new FileChooser.ExtensionFilter("Word files", "*.doc", "*.docx", "*.odt"),
                new FileChooser.ExtensionFilter("Presentation files", "*.ppt", "*.pptx"),
                new FileChooser.ExtensionFilter("Sheets files", "*.xls", "*.xlsx")
        );

        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(chapterNum.getScene().getWindow());

        if (selectedFiles != null) {

            for (File file : selectedFiles) {
                String fileName = file.getName();
                long fileSize = file.length();
                String readableFileSize = readableFileSize(fileSize);
                String fileType = getFileType(file);
                String filePath = file.getAbsolutePath();

                // Insert the file into the database
                DocumentsFilesDAO.insertDocumentFile(fileName, fileSize, fileType, filePath, (int)section.getCourseid());

                NumFiles++;
                FilesVbox.getChildren().add(docLine(fileName, readableFileSize, fileType, filePath));
            }
            FilesNumber.setText(NumFiles+"");
        }
    }

    @FXML
    void deleteChapter(MouseEvent event) {

        try (Connection conn = connect();
             PreparedStatement statement = conn.prepareStatement("DELETE FROM documents_files WHERE chapterId = ?")) {
            statement.setLong(1, section.getCourseid());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM chapters WHERE id = ?")) {
            pstmt.setLong(1, section.getCourseid());
            pstmt.executeUpdate();
            System.out.println("Chapter deleted successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        currentCourse.setNumsections(currentCourse.getNumsections() - 1);
        CourseDAO.updateCourse((int) currentCourse.getCourseid(), currentCourse.getFullname(), currentCourse.getShortname(), currentCourse.getSummary(), currentCourse.getNumsections(), currentCourse.getNumsections());

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
            if (module.getModplural().equals("Files")) {
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
                    FilesVbox.getChildren().add(docLine(file.getFilename(), readableFileSize(file.getFilesize()), "PDF", file.getFilepath()));
                }

            }
        }
    }

    private String getFileType(File file) {
        String fileName = file.getName().toLowerCase();
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

    public HBox docLine(String fileName, String readableFileSize, String fileType, String filePath){
        HBox line = new HBox();
        Region region = new Region();
        Label path = new Label(filePath);
        Text name = new Text(fileName);

        Label type = new Label(fileType);
        type.setStyle("-fx-background-color:#fff0; -fx-text-fill:#000000;");
        type.setPrefWidth(120);

        Label deldoc = new Label("❌");
        deldoc.setStyle("-fx-background-color:#fff0; -fx-text-fill:#ff5e5e;");


        line.setPrefWidth(FilesVbox.getWidth());
        line.setPrefHeight(HBox.USE_COMPUTED_SIZE);
        line.setSpacing(20);

        HBox.setHgrow(region, Priority.ALWAYS);
        region.setPrefHeight(Region.USE_COMPUTED_SIZE);
        path.setMaxWidth(50);
        path.setVisible(false);

        line.getChildren().addAll(name, path, region, new Text(readableFileSize), type, deldoc);

        line.setOnMouseClicked(event -> handleDocClick(line, event));
        deldoc.setOnMouseClicked(event -> deleteDocFile(name, event));

        return line;
    }

    private void deleteDocFile(Text fileName, MouseEvent event) {
        String query = "DELETE FROM documents_files WHERE fileName = ?";
        try (Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, fileName.getText());
            pstmt.executeUpdate();
            System.out.println("Document deleted successfully.");

            loadDocumentsFilesFromDatabase();

        } catch (SQLException e) {
            e.printStackTrace();
        }

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
