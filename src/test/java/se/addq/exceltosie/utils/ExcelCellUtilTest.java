package se.addq.exceltosie.utils;

import org.junit.jupiter.api.Test;
import se.addq.exceltosie.error.BadDataException;
import se.addq.exceltosie.excel.CellType;
import se.addq.exceltosie.excel.ExcelCell;
import se.addq.exceltosie.excel.ExcelSheetData;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ExcelCellUtilTest {

    @Test
    void getExistingExcelCellValueInExcelSheetDataShouldReturnValue() throws BadDataException {
        String expectedValue = "text";
        String cellRef = "C7";
        ExcelSheetData excelSheetData = getExcelSheetData(expectedValue, cellRef);
        String actualValue = ExcelCellUtil.getExcelValueByCellReference(cellRef, excelSheetData);
        assertThat(actualValue).isEqualTo(expectedValue);
    }


    @Test
    void getNonExistingExcelCellValueInExcelSheetDataShouldRetunrEmptyString() throws BadDataException {
        String expectedValue = "";
        String cellRef = "C7";
        ExcelSheetData excelSheetData = new ExcelSheetData();
        String actualValue = ExcelCellUtil.getExcelValueByCellReference(cellRef, excelSheetData);
        assertThat(actualValue).isEqualTo(expectedValue);
    }


    @Test
    void shouldGetExcelCellWhenExistingByValue() {
        String expectedValue = "text";
        String cellRef = "C7";
        ExcelCell expectedExcelCell = new ExcelCell();
        expectedExcelCell.setCellType(CellType.STRING);
        expectedExcelCell.setValue(expectedValue);
        expectedExcelCell.setCellReference(cellRef);
        ExcelSheetData excelSheetData = getExcelSheetData(expectedValue, cellRef);

        ExcelCell actualExcelCell = ExcelCellUtil.getExcelCellByValue(expectedValue, excelSheetData);

        assertThat(actualExcelCell).isEqualTo(expectedExcelCell);
    }

    @Test
    void shouldGetEmptyExcelCellWhenExistingByValueNotMatching() {
        String expectedValue = "text";
        String cellRef = "C7";
        ExcelCell expectedExcelCell = new ExcelCell();
        expectedExcelCell.setCellType(CellType.STRING);
        expectedExcelCell.setValue(expectedValue);
        expectedExcelCell.setCellReference(cellRef);
        ExcelSheetData excelSheetData = getExcelSheetData(expectedValue, cellRef);

        ExcelCell actualExcelCell = ExcelCellUtil.getExcelCellByValue("notMatching", excelSheetData);

        assertThat(actualExcelCell).isEqualTo(new ExcelCell());
    }

    @Test
    void shouldGetCellReferenceByValue() {
        String expectedValue = "text";
        String cellRef = "C7";
        ExcelCell expectedExcelCell = new ExcelCell();
        expectedExcelCell.setCellType(CellType.STRING);
        expectedExcelCell.setValue(expectedValue);
        expectedExcelCell.setCellReference(cellRef);
        ExcelSheetData excelSheetData = getExcelSheetData(expectedValue, cellRef);

        String actualCellRef = ExcelCellUtil.getExcelCellReferenceByValue(expectedValue, excelSheetData);
        assertThat(actualCellRef).isEqualTo(cellRef);
    }

    @Test
    void shouldGetEmptyStringWhenCellReferenceByValueIsNotMatching() {
        String expectedValue = "text";
        String cellRef = "C7";
        ExcelCell expectedExcelCell = new ExcelCell();
        expectedExcelCell.setCellType(CellType.STRING);
        expectedExcelCell.setValue(expectedValue);
        expectedExcelCell.setCellReference(cellRef);
        ExcelSheetData excelSheetData = getExcelSheetData(expectedValue, cellRef);

        String actualCellRef = ExcelCellUtil.getExcelCellReferenceByValue("notExistong", excelSheetData);
        assertThat(actualCellRef).isEqualTo("");
    }

    @Test
    void shouldBeMatchingCellWhenWhiteSpaceInValue() {
        String valueToMatch = "test värde";
        ExcelCell excelCell = new ExcelCell();
        excelCell.setValue("test värde   ");
        boolean isMatching = ExcelCellUtil.isCellMatching(valueToMatch, excelCell);
        assertThat(isMatching).isTrue();
    }

    @Test
    void shouldBeMatchingCellWhenUpperCaseInValue() {
        String valueToMatch = "test värde";
        ExcelCell excelCell = new ExcelCell();
        excelCell.setValue("Test Värde");
        boolean isMatching = ExcelCellUtil.isCellMatching(valueToMatch, excelCell);
        assertThat(isMatching).isTrue();
    }

    @Test
    void shouldBeMatchingCellWhenWhiteSpaceInRawValue() {
        String valueToMatch = "test värde";
        ExcelCell excelCell = new ExcelCell();
        excelCell.setRawValue("test värde   ");
        excelCell.setValue("=-(G48)");
        boolean isMatching = ExcelCellUtil.isCellMatching(valueToMatch, excelCell);
        assertThat(isMatching).isTrue();
    }

    @Test
    void shouldBeMatchingCellWhenUpperCaseInRawValue() {
        String valueToMatch = "test värde";
        ExcelCell excelCell = new ExcelCell();
        excelCell.setValue("=-(G48)");
        excelCell.setRawValue("Test Värde");
        boolean isMatching = ExcelCellUtil.isCellMatching(valueToMatch, excelCell);
        assertThat(isMatching).isTrue();
    }

    private ExcelSheetData getExcelSheetData(String expectedValue, String cellRef) {
        ExcelSheetData excelSheetData = new ExcelSheetData();
        List<ExcelCell> excelCellList = new ArrayList<>();
        ExcelCell excelCell = new ExcelCell();
        excelCell.setCellReference(cellRef);
        excelCell.setValue(expectedValue);
        excelCell.setCellType(CellType.STRING);
        excelCellList.add(excelCell);
        excelSheetData.setExcelCellList(excelCellList);
        return excelSheetData;
    }

}