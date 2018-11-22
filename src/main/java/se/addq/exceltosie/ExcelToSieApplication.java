package se.addq.exceltosie;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import se.addq.exceltosie.companydata.AccountingDataToCompanyDataMapper;
import se.addq.exceltosie.companydata.SieFileDataToCompanyDataMapper;
import se.addq.exceltosie.excel.ExcelFileReader;
import se.addq.exceltosie.file.CompanyDataToFileDataMapper;
import se.addq.exceltosie.file.FileCreator;
import se.addq.exceltosie.file.FileGenerator;
import se.addq.exceltosie.gui.GuiHandler;
import se.addq.exceltosie.utils.FileHandler;

public class ExcelToSieApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private static final int WIDTH = 600;
    private static final int HEIGHT = 200;

    @Override
    public void start(Stage primaryStage) {
        GuiHandler guiHandler = new GuiHandler(new FileCreator(new ExcelFileReader(new XSSFWorkbook()), new CompanyDataToFileDataMapper(), new AccountingDataToCompanyDataMapper(new SieFileDataToCompanyDataMapper())), new FileGenerator(new FileHandler()), new FileChooser());
        Scene scene = new Scene(guiHandler.getGroupNode(), WIDTH, HEIGHT);
        primaryStage.setTitle("Bruttopool bolag bokföring för månad till .si fil");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
