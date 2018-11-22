package se.addq.exceltosie.excel;

import java.util.ArrayList;
import java.util.List;

/**
 * Data class (dao) for internal to easier find cells and specific values. Excel cell is an object
 * with rows and columns. Data about the cell type is also stored.
 * See CellType for values.
 */
public class ExcelSheetData {

    private String excelSheetName;

    private List<ExcelCell> excelCellList = new ArrayList<>();

    public String getExcelSheetName() {
        return excelSheetName;
    }

    public void setExcelSheetName(String excelSheetName) {
        this.excelSheetName = excelSheetName;
    }

    public List<ExcelCell> getExcelCellList() {
        return excelCellList;
    }

    public void setExcelCellList(List<ExcelCell> excelCellList) {
        this.excelCellList = excelCellList;
    }
}
