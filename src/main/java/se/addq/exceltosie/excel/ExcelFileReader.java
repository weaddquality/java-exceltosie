package se.addq.exceltosie.excel;

import org.apache.poi.EmptyFileException;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.addq.exceltosie.error.BadDataException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Class for reading an excel file / sheet and return it in application internal format.
 */
public class ExcelFileReader {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private XSSFWorkbook workbook;

    public ExcelFileReader(XSSFWorkbook workbook) {
        this.workbook = workbook;
    }


    public ExcelSheetData readAllData(File file, String tabName) throws BadDataException {
        XSSFWorkbook xssfWorkbook = getExcelWorkBookFromFile(file);
        if (xssfWorkbook == null) {
            return new ExcelSheetData();
        }
        XSSFSheet xssfSheet = xssfWorkbook.getSheet(tabName);
        LOG.info("Fetch data from Sheet with name {}", tabName);
        if (xssfSheet == null) {
            return new ExcelSheetData();
        }
        return getExcelSheetData(xssfSheet);
    }

    public boolean isExistingTab(String tabName, File file) throws BadDataException {
        XSSFWorkbook xssfWorkbook = getExcelWorkBookFromFile(file);
        return xssfWorkbook.getSheetIndex(tabName) >= 0;
    }

    public ExcelSheetData readAllData(File file, int tabId) throws BadDataException {
        XSSFWorkbook xssfWorkbook = getExcelWorkBookFromFile(file);
        XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(tabId);
        LOG.info("Fetch data from tab id {}", tabId);
        return getExcelSheetData(xssfSheet);
    }

    private XSSFWorkbook getExcelWorkBookFromFile(File file) throws BadDataException {
        if (file == null) {
            return null;
        }
        try {
            FileInputStream excelFile = new FileInputStream(file);
            workbook = new XSSFWorkbook(excelFile);
            LOG.info("Fetched workbook from file {}", file.getName());
        } catch (EmptyFileException | IOException e) {
            workbook = null;
            throw new BadDataException("Kan inte läsa Excel fil " + file, e);
        }
        return workbook;
    }

    private ExcelSheetData getExcelSheetData(XSSFSheet xssfSheet) {
        LOG.info("Convert XSSF sheet to internal excel sheet data");
        DataFormatter formatter = new DataFormatter();
        ExcelSheetData excelSheetData = new ExcelSheetData();
        excelSheetData.setExcelSheetName(xssfSheet.getSheetName());
        List<ExcelCell> excelCellList = new ArrayList<>();
        XSSFRow row;
        XSSFCell cell;

        Iterator rows = xssfSheet.rowIterator();

        while (rows.hasNext()) {
            row = (XSSFRow) rows.next();
            Iterator cells = row.cellIterator();
            while (cells.hasNext()) {
                cell = (XSSFCell) cells.next();
                ExcelCell excelCell = new ExcelCell();
                CellReference cellReference = new CellReference(row.getRowNum(), cell.getColumnIndex());
                excelCell.setCellReference(cellReference.formatAsString());
                excelCell.setRow(cellReference.getRow() + 1);
                excelCell.setColumn(CellReference.convertNumToColString(cellReference.getCol()));
                excelCell.setValue(formatter.formatCellValue(cell));
                switch (cell.getCellType()) {
                    case STRING:
                        excelCell.setCellType(CellType.STRING);
                        break;
                    case NUMERIC:
                        if (DateUtil.isCellDateFormatted(cell)) {
                            excelCell.setCellType(CellType.DATE);
                        } else {
                            excelCell.setCellType(CellType.NUMERIC);
                        }
                        break;
                    case BOOLEAN:
                        excelCell.setCellType(CellType.BOOLEAN);
                        break;
                    case FORMULA:
                        excelCell.setCellType(CellType.FORMULA);
                        excelCell.setRawValue(cell.getRawValue());
                        break;
                    case BLANK:
                        excelCell.setCellType(CellType.BLANK);
                        break;
                    default:
                        excelCell.setCellType(CellType.UNKNOWN);
                }
                excelCellList.add(excelCell);
            }
        }
        excelSheetData.setExcelCellList(excelCellList);
        return excelSheetData;
    }

}






