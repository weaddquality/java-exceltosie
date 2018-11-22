package se.addq.exceltosie.excel;

import java.util.Objects;

public class ExcelCell {

    private String cellReference;

    private String column;

    private int row;

    private CellType cellType;

    private String value;

    private String rawValue = "";

    public String getCellReference() {
        return cellReference;
    }

    public void setCellReference(String cellReference) {
        this.cellReference = cellReference;
    }

    public CellType getCellType() {
        return cellType;
    }

    public void setCellType(CellType cellType) {
        this.cellType = cellType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRawValue() {
        return rawValue;
    }

    public void setRawValue(String rawValue) {
        this.rawValue = rawValue;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExcelCell excelCell = (ExcelCell) o;
        return row == excelCell.row &&
                Objects.equals(cellReference, excelCell.cellReference) &&
                Objects.equals(column, excelCell.column) &&
                cellType == excelCell.cellType &&
                Objects.equals(value, excelCell.value) &&
                Objects.equals(rawValue, excelCell.rawValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cellReference, column, row, cellType, value, rawValue);
    }

    @Override
    public String toString() {
        return "ExcelCell{" +
                "cellReference='" + cellReference + '\'' +
                ", column='" + column + '\'' +
                ", row=" + row +
                ", cellType=" + cellType +
                ", value='" + value + '\'' +
                ", rawValue='" + rawValue + '\'' +
                '}';
    }
}
