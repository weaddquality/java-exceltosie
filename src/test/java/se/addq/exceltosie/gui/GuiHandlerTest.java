package se.addq.exceltosie.gui;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.framework.junit5.Start;
import se.addq.exceltosie.file.FileCreator;
import se.addq.exceltosie.file.FileGenerator;

import java.util.function.Predicate;

//Tests using TestFX framework
@ExtendWith(ApplicationExtension.class)
class GuiHandlerTest extends ApplicationTest {

    @Mock
    private FileCreator fileCreator;

    @Mock
    private FileGenerator fileGenerator;

    private FileChooser fileChooser = new FileChooser();

    private static final int WIDTH = 600;
    private static final int HEIGHT = 200;


    @Start
    void onStart(Stage stage) {
        MockitoAnnotations.initMocks(this);
        GuiHandler guiHandler = new GuiHandler(fileCreator, fileGenerator, fileChooser);
        Scene scene = new Scene(guiHandler.getGroupNode(), WIDTH, HEIGHT);
        stage.setScene(scene);
        stage.show();
        stage.toFront();
    }


    @Test
    void shouldBePossibleToClickOnLoadButton(FxRobot robot) {
        Platform.runLater(() -> robot.clickOn("#LoadButton"));
    }

    @Test
    void shouldBeDisabledCreateButtonWhenNoFileLoaded() {
        FxAssert.verifyThat("#CreateButton", (Predicate<Button>) Node::isDisabled);
    }


}