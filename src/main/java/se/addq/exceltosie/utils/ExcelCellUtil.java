package se.addq.exceltosie.utils;

import se.addq.exceltosie.error.BadDataException;
import se.addq.exceltosie.excel.CellType;
import se.addq.exceltosie.excel.ExcelCell;
import se.addq.exceltosie.excel.ExcelSheetData;

public class ExcelCellUtil {

    public static String getExcelValueByCellReference(String cellId, ExcelSheetData sheetData) throws BadDataException {
        ExcelCell excelCell = sheetData.getExcelCellList().stream().filter(cell -> cell.getCellReference().equals(cellId)).findFirst().orElse(new ExcelCell());
        String value;
        if (excelCell.getCellType() == null) {
            return "";
        }
        if (excelCell.getCellType().equals(CellType.FORMULA)) {
            value = excelCell.getRawValue();
        } else {
            value = excelCell.getValue();
        }
        if(value == null) {
            throw new BadDataException("Could not find a value for cellid " + cellId + " in " + sheetData.getExcelSheetName());
        }
        return value;
    }

    public static ExcelCell getExcelCellByValue(String cellValue, ExcelSheetData sheetData) {
        ExcelCell excelCell = getExcelCellFromReference(cellValue, sheetData);
        if (excelCell.getCellType() == null) {
            return new ExcelCell();
        }
        return excelCell;
    }

    private static ExcelCell getExcelCellFromReference(String cellValue, ExcelSheetData sheetData) {
        return sheetData.getExcelCellList().stream().filter(cell -> ExcelCellUtil.isCellMatching(cellValue, cell)).findFirst().orElse(new ExcelCell());
    }


    static boolean isCellMatching(String cellValue, ExcelCell cell) {
        if (cell.getValue() == null) {
            return false;
        }
        String trimmedCellValue = cell.getValue().trim().toLowerCase();
        if (cell.getRawValue() == null) {
            return false;
        }
        String trimmedRawValue = cell.getRawValue().trim().toLowerCase();
        String trimmedExpectedValue = cellValue.trim().toLowerCase();
        if (trimmedCellValue.equals(trimmedExpectedValue)) {
            return true;
        }
        return trimmedRawValue.equals(trimmedExpectedValue);
    }

    public static String getExcelCellReferenceByValue(String cellValue, ExcelSheetData sheetData) {
        ExcelCell excelCell = sheetData.getExcelCellList().stream().filter(cell -> cell.getValue().equals(cellValue)).findFirst().orElse(new ExcelCell());
        if (excelCell.getCellType() == null) {
            return "";
        }
        return excelCell.getCellReference();
    }


}
