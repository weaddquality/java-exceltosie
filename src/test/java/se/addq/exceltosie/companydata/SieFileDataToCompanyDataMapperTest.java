package se.addq.exceltosie.companydata;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import se.addq.exceltosie.error.BadDataException;
import se.addq.exceltosie.excel.CellType;
import se.addq.exceltosie.excel.ExcelCell;
import se.addq.exceltosie.excel.ExcelSheetData;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class SieFileDataToCompanyDataMapperTest {

    private SieFileDataToCompanyDataMapper sieFileDataToCompanyDataMapper;

    @BeforeEach
    void setup() {
        sieFileDataToCompanyDataMapper = new SieFileDataToCompanyDataMapper();
    }

    @Test
    void shouldGetCompanyNameWhenSetInExcelSheet() throws BadDataException {
        CompanyData companyData = sieFileDataToCompanyDataMapper.getCompanyDataForSieFile(getExcelSheetCompanyData());
        assertThat(companyData.getCompanyName()).isEqualTo("Company X AB");
    }

    @Test
    void shouldGetCompanyOrganisationNumberWhenSetInExcelSheet() throws BadDataException {
        CompanyData companyData = sieFileDataToCompanyDataMapper.getCompanyDataForSieFile(getExcelSheetCompanyData());
        assertThat(companyData.getOrganizationNumber()).isEqualTo("526362-4201");
    }

    @Test
    void shouldGetTeamDataWhenSetInExcelSheet() throws BadDataException {
        String teamName = "Team 1";
        TeamData teamData = new TeamData(teamName, "1");
        List<EmployeeData> employeeDataList = new ArrayList<>();
        employeeDataList.add(new EmployeeData(EmployeeType.STAFF, "3 Pelle Pärson", "1"));
        employeeDataList.add(new EmployeeData(EmployeeType.MANAGEMENT, "4 Kalle Karlsson", "2"));
        teamData.setEmployeeDataList(employeeDataList);
        CompanyData companyData = sieFileDataToCompanyDataMapper.getCompanyDataForSieFile(getExcelSheetCompanyData());
        assertThat(companyData.getTeamDataList()).contains(teamData);
    }

    @Test
    void shouldGetManagementDataWhenSetInExcelSheet() throws BadDataException {
        String teamName = "Team 1";
        CompanyData companyData = sieFileDataToCompanyDataMapper.getCompanyDataForSieFile(getExcelSheetCompanyData());
        assertThat(companyData.getManagementData().getTeamName()).isEqualTo(teamName);
    }

    @Test
    void shouldNotBeAllowedToAddSameTeamTwiceInExcelSheet() {
        String teamName = "Team 1";
        ExcelSheetData excelSheetData = getExcelSheetCompanyData();
        excelSheetData.getExcelCellList().add(getCell(teamName, "A", 6));
        Throwable throwable = Assertions.assertThrows(BadDataException.class, () -> sieFileDataToCompanyDataMapper.getCompanyDataForSieFile(excelSheetData));
        assertThat(throwable.getMessage()).isEqualTo("Det finns flera team med samma namn " + teamName + " i flik " + excelSheetData.getExcelSheetName() );
    }



    @Test
    void shouldNotBeAllowedToAddSameEmployeeTwiceInExcelSheet() {
        String employeeName = "3 Pelle Pärson";
        ExcelSheetData excelSheetData = getExcelSheetCompanyData();
        excelSheetData.getExcelCellList().add(getCell(employeeName, "A", 12));
        excelSheetData.getExcelCellList().add(getCell("3", "B", 12));
        excelSheetData.getExcelCellList().add(getCell("Team 3", "C", 12));
        Throwable throwable = Assertions.assertThrows(BadDataException.class, () -> sieFileDataToCompanyDataMapper.getCompanyDataForSieFile(excelSheetData));
        assertThat(throwable.getMessage()).isEqualTo("Det finns flera anställda med samma namn " + employeeName +  " i flik " + excelSheetData.getExcelSheetName());
    }


    private ExcelSheetData getExcelSheetCompanyData() {
        ExcelSheetData excelSheetData = new ExcelSheetData();
        excelSheetData.setExcelSheetName("SI-fildata");
        List<ExcelCell> excelCellList = new ArrayList<>();
        excelCellList.addAll(getExcelBaseCells());
        excelCellList.addAll(getExcelTeamListCells());
        excelCellList.addAll(getExcelEmployeeListCells());
        excelSheetData.setExcelCellList(excelCellList);
        return excelSheetData;
    }

    private List<ExcelCell> getExcelBaseCells() {
        List<ExcelCell> excelCellList = new ArrayList<>();
        excelCellList.add(getCell("Company X AB", "A", 2));
        excelCellList.add(getCell("526362-4201", "B", 2));
        return excelCellList;
    }

    private List<ExcelCell> getExcelEmployeeListCells() {
        List<ExcelCell> excelCellList = new ArrayList<>();
        excelCellList.add(getCell("Anställd namn", "A", 9));
        excelCellList.add(getCell("3 Pelle Pärson", "A", 10));
        excelCellList.add(getCell("1", "B", 10));
        excelCellList.add(getCell("Team 1", "C", 10));
        excelCellList.add(getCell("", "D", 10));
        excelCellList.add(getCell("4 Kalle Karlsson", "A", 11));
        excelCellList.add(getCell("2", "B", 11));
        excelCellList.add(getCell("Team 1", "C", 11));
        excelCellList.add(getCell("MANAGEMENT", "D", 11));
        return excelCellList;
    }

    private List<ExcelCell> getExcelTeamListCells() {
        List<ExcelCell> excelCellList = new ArrayList<>();
        excelCellList.add(getCell("Team namn", "A", 4));
        excelCellList.add(getCell("Team 1", "A", 5));
        excelCellList.add(getCell("1", "B", 5));
        excelCellList.add(getCell("STAFF", "C", 5));
        return excelCellList;
    }

    private ExcelCell getCell(String value, String column, int row) {
        ExcelCell excelCell = new ExcelCell();
        excelCell.setCellType(CellType.STRING);
        excelCell.setCellReference(column + row);
        excelCell.setValue(value);
        excelCell.setRow(row);
        excelCell.setColumn(column);
        return excelCell;
    }
}