package se.addq.exceltosie.excel;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import se.addq.exceltosie.error.BadDataException;

import java.io.File;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ExcelDataReaderTest {


    private ExcelFileReader excelFileReader;

    @BeforeEach
    void setup() {
        excelFileReader = new ExcelFileReader(new XSSFWorkbook());
    }


    @Test
    void readAllDataExistingFileAndExistingSheetByName() throws BadDataException {
        ExcelSheetData excelSheetData = excelFileReader.readAllData(getFileAsResource("testfil.xlsx"), "SI-fildata");
        assertThat(excelSheetData).isNotNull();
        assertThat(excelSheetData.getExcelSheetName()).isEqualTo("SI-fildata");
    }

    @Test
    void readAllDataExistingFileAndExistingSheetById() throws BadDataException {
        ExcelSheetData excelSheetData = excelFileReader.readAllData(getFileAsResource("testfil.xlsx"), 1);
        assertThat(excelSheetData).isNotNull();
        assertThat(excelSheetData.getExcelSheetName()).isEqualTo("2018");
    }


    @Test
    void readAllDataExistingFileEmpty() {
        File file = getFileAsResource("empty_file.xlsx");
        Throwable throwable = Assertions.assertThrows(BadDataException.class, () -> excelFileReader.readAllData(file, 1));
        assertThat(throwable.getMessage()).isEqualTo("Kan inte läsa Excel fil " + file.getAbsolutePath());
    }

    @Test
    void readAllDataExistingFileBrokenExcel() {
        File file = getFileAsResource("broken_excel.xlsx");
        Throwable throwable = Assertions.assertThrows(BadDataException.class, () -> excelFileReader.readAllData(file, 1));
        assertThat(throwable.getMessage()).isEqualTo("Kan inte läsa Excel fil " + file.getAbsolutePath());
    }

    @Test
    void getTrueIfExistingSheetInFile() throws BadDataException {
        boolean isExisting = excelFileReader.isExistingTab("2018", getFileAsResource("testfil.xlsx"));
        assertThat(isExisting).isTrue();
    }

    @Test
    void getFalseIfNonExistingSheetInFile() throws BadDataException {
        boolean isExisting = excelFileReader.isExistingTab("2017", getFileAsResource("testfil.xlsx"));
        assertThat(isExisting).isFalse();
    }

    private File getFileAsResource(String fileName) {
        return new File(Objects.requireNonNull(getClass().getClassLoader().getResource(fileName)).getFile());
    }
}