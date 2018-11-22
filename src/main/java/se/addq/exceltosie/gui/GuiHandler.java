package se.addq.exceltosie.gui;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.addq.exceltosie.error.BadDataException;
import se.addq.exceltosie.file.FileCreator;
import se.addq.exceltosie.file.FileData;
import se.addq.exceltosie.file.FileGenerator;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GuiHandler {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String DEFAULT_START_FOLDER_FOR_FILE_LOAD = (System.getProperty("user.dir") + System.getProperty("file.separator") + "infiler");

    private File excelFile;

    private Text text = new Text();

    private Hyperlink link;

    private FileCreator fileCreator;

    private FileGenerator fileGenerator;
    private FileChooser fileChooser;

    private Button createButton;

    public GuiHandler(FileCreator fileCreator, FileGenerator fileGenerator, FileChooser fileChooser) {
        this.fileCreator = fileCreator;
        this.fileGenerator = fileGenerator;
        this.fileChooser = fileChooser;
    }


    public Group getGroupNode() {
        Group root = new Group();
        root.getChildren().add(getLoadFileButton(10, 10));
        root.getChildren().add(setFileLoadText("Ingen fil inläst...", 10, 60));
        root.getChildren().add(getCreateFileButton(10, 90));
        root.getChildren().add(getHyperlink("Ingen fil skapad...", 10, 140));
        return root;
    }


    private Node getCreateFileButton(int x, int y) {
        createButton = new Button("Skapa SIE (.si) fil");
        createButton.setId("CreateButton");
        createButton.setLayoutX(x);
        createButton.setLayoutY(y);
        createButton.setDisable(true);
        createButton.setOnAction(arg0 -> {
            FileData fileData = new FileData();
            try {
                fileData = fileCreator.getAccountingDataFromExcel(excelFile);
            } catch (BadDataException e) {
                LOG.error("Could not find data", e);
                AlertHandler.alertError(e.getMessage(), e);
            }

            if (fileData.getCompanyName() == null) {
                return;
            }
            String fileName = fileGenerator.createFile(excelFile.getParent(), fileData);
            link.setText(fileName);
            link.setOnAction(e -> {
                try {
                    Desktop.getDesktop().open(new File(fileName));
                } catch (IOException exception) {
                    LOG.error("Cant access file {}", fileName, exception);
                    link.setText("Fel - gick inte att öppna fil");
                }
            });
        });
        return createButton;
    }

    private Node getHyperlink(String text, int x, int y) {
        link = new Hyperlink();
        link.setText(text);
        link.setLayoutX(x);
        link.setLayoutY(y);
        link.getStyleClass().add("copyable-label");
        return link;
    }

    private Node setFileLoadText(String text, int x, int y) {
        this.text.setLayoutX(x);
        this.text.setLayoutY(y);
        this.text.setText(text);
        return this.text;
    }

    private Button getLoadFileButton(int x, int y) {
        Button loadButton = new Button("Ladda excel fil");
        loadButton.setId("LoadButton");
        loadButton.setLayoutX(x);
        loadButton.setLayoutY(y);
        loadButton.setOnAction(arg0 -> {

            File file = getStartFolderForFile();
            fileChooser.setInitialDirectory(file);
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel files (*.xls, *.xlsx)", "*.xlsx", "*.xls");
            fileChooser.getExtensionFilters().add(extFilter);
            excelFile = fileChooser.showOpenDialog(null);
            if (excelFile != null) {
                LOG.info("File set:{}", excelFile.toString());
                setFileLoadText("Infil: " + excelFile.getName(), 10, 60);
                createButton.setDisable(false);
            } else {
                setFileLoadText("Kunde inte läsa fil...", 10, 60);
                createButton.setDisable(true);
            }
        });
        return loadButton;
    }

    private File getStartFolderForFile() {
        Path path = Paths.get(DEFAULT_START_FOLDER_FOR_FILE_LOAD);
        if (Files.exists(path)) {
            LOG.info("Directory already exists {}", DEFAULT_START_FOLDER_FOR_FILE_LOAD);
        } else {
            try {
                Files.createDirectories(path);
                LOG.info("Created new directory {}", DEFAULT_START_FOLDER_FOR_FILE_LOAD);

            } catch (IOException e) {
                LOG.info("Can't create default directory {}", DEFAULT_START_FOLDER_FOR_FILE_LOAD);
            }
        }
        return new File(DEFAULT_START_FOLDER_FOR_FILE_LOAD);
    }
}


