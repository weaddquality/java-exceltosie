package se.addq.exceltosie.file;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import se.addq.exceltosie.companydata.AccountingDataToCompanyDataMapper;
import se.addq.exceltosie.error.BadDataException;
import se.addq.exceltosie.excel.CellType;
import se.addq.exceltosie.excel.ExcelCell;
import se.addq.exceltosie.excel.ExcelFileReader;
import se.addq.exceltosie.excel.ExcelSheetData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class FileCreatorTest {

    @Mock
    private ExcelFileReader excelFileReaderMock;

    @Mock
    private CompanyDataToFileDataMapper companyDataToFileDataMapperMock;

    @Mock
    private AccountingDataToCompanyDataMapper accountingDataToCompanyDataMapperMock;

    private FileCreator fileCreator;

    private File existingFile = new File("c:/existingFile");

    private File nonExistingFile = new File("c:/nonExistingFile");

    @BeforeEach
    void setup() {
        fileCreator = new FileCreator(excelFileReaderMock, companyDataToFileDataMapperMock, accountingDataToCompanyDataMapperMock);
    }


    @Test
    void getEmptyFileDataWhenNoExcelSheetDataIsReturned() throws BadDataException {
        Mockito.lenient().when(excelFileReaderMock.readAllData(Mockito.eq(nonExistingFile), Mockito.anyString())).thenReturn(new ExcelSheetData());
        Mockito.lenient().when(excelFileReaderMock.readAllData(Mockito.eq(nonExistingFile), Mockito.anyInt())).thenReturn(new ExcelSheetData());
        FileData fileData = fileCreator.getAccountingDataFromExcel(nonExistingFile);
        assertThat(fileData.getCompanyName()).isNull();
    }

    @Test
    void getFileDataWhenExcelSheetDataIsReturned() throws BadDataException {
        Mockito.lenient().when(excelFileReaderMock.readAllData(Mockito.eq(existingFile), Mockito.anyString())).thenReturn(getExcelSheetData());
        Mockito.lenient().when(excelFileReaderMock.readAllData(Mockito.eq(existingFile), Mockito.anyInt())).thenReturn(getExcelSheetData());
        Mockito.when(excelFileReaderMock.isExistingTab(Mockito.anyString(), Mockito.any())).thenReturn(true);
        fileCreator.getAccountingDataFromExcel(existingFile);
        Mockito.verify(accountingDataToCompanyDataMapperMock, Mockito.times(1)).getMappedDataFromExcel(Mockito.any(), Mockito.any());
        Mockito.verify(companyDataToFileDataMapperMock, Mockito.times(1)).getMappedDataFromCompanyData(Mockito.any());
    }


    private ExcelSheetData getExcelSheetData() {
        ExcelSheetData excelSheetData = new ExcelSheetData();
        excelSheetData.setExcelSheetName("TestFile");
        List<ExcelCell> excelCellList = new ArrayList<>();
        ExcelCell excelCell = new ExcelCell();
        excelCell.setCellType(CellType.STRING);
        excelCell.setCellReference("C4");
        excelCell.setRow(4);
        excelCell.setColumn("C");
        excelCell.setRawValue("=!A7");
        excelCellList.add(excelCell);
        excelSheetData.setExcelCellList(excelCellList);
        return excelSheetData;
    }
}