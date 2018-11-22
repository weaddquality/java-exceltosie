package se.addq.exceltosie.companydata;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import se.addq.exceltosie.error.BadDataException;
import se.addq.exceltosie.excel.CellType;
import se.addq.exceltosie.excel.ExcelCell;
import se.addq.exceltosie.excel.ExcelSheetData;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ExcelToCompanyDataMapperTest {

    private AccountingDataToCompanyDataMapper accountingDataToCompanyDataMapper;

    @Mock
    private SieFileDataToCompanyDataMapper sieFileDataToCompanyDataMapper;

    @BeforeEach
    void setup() {
        accountingDataToCompanyDataMapper = new AccountingDataToCompanyDataMapper(sieFileDataToCompanyDataMapper);
    }

    @Test
    void getMappedDataFromExcelWithPoolChangeEmployee7018() throws BadDataException {
        Mockito.when(sieFileDataToCompanyDataMapper.getCompanyDataForSieFile(Mockito.any())).thenReturn(new CompanyData());
        CompanyData companyData = accountingDataToCompanyDataMapper.getMappedDataFromExcel(getExcelSheetAccountingData(), getExcelSheetCompanyData());
        assertThat(companyData.getGrossPoolChangeEmployees7018()).isEqualTo(new BigDecimal(200).setScale(2, RoundingMode.HALF_EVEN));
    }

    @Test
    void getMappedDataFromExcelWithPoolChangeManagement7029() throws BadDataException {
        Mockito.when(sieFileDataToCompanyDataMapper.getCompanyDataForSieFile(Mockito.any())).thenReturn(new CompanyData());
        CompanyData companyData = accountingDataToCompanyDataMapper.getMappedDataFromExcel(getExcelSheetAccountingData(), getExcelSheetCompanyData());
        assertThat(companyData.getGrossPoolChangeManagement7029()).isEqualTo(new BigDecimal(200).setScale(2, RoundingMode.HALF_EVEN));
    }


    @Test
    void getMappedDataFromExcelWithPoolDebtEmployee() throws BadDataException {
        Mockito.when(sieFileDataToCompanyDataMapper.getCompanyDataForSieFile(Mockito.any())).thenReturn(new CompanyData());
        CompanyData companyData = accountingDataToCompanyDataMapper.getMappedDataFromExcel(getExcelSheetAccountingData(), getExcelSheetCompanyData());
        assertThat(companyData.getGrossPoolDebtEmployees2801()).isEqualTo(new BigDecimal(4012).setScale(2, RoundingMode.HALF_EVEN).negate());
    }

    @Test
    void getMappedDataFromExcelWithPoolDebtManagement() throws BadDataException {
        Mockito.when(sieFileDataToCompanyDataMapper.getCompanyDataForSieFile(Mockito.any())).thenReturn(new CompanyData());
        CompanyData companyData = accountingDataToCompanyDataMapper.getMappedDataFromExcel(getExcelSheetAccountingData(), getExcelSheetCompanyData());
        assertThat(companyData.getGrossPoolDebtManagement2802()).isEqualTo(new BigDecimal(20012).setScale(2, RoundingMode.HALF_EVEN).negate());
    }

    @Test
    void getMappedDataFromExcelWithTeamCostForStaff() throws BadDataException {
        Mockito.when(sieFileDataToCompanyDataMapper.getCompanyDataForSieFile(Mockito.any())).thenReturn(getCompanyData());
        CompanyData actualCompanyData = accountingDataToCompanyDataMapper.getMappedDataFromExcel(getExcelSheetAccountingData(), getExcelSheetCompanyData());
        for (TeamData teamData : getCompanyData().getTeamDataList()) {
            for (EmployeeData employeeData : teamData.getEmployeeDataList()) {
                if (employeeData.getEmployeeType().equals(EmployeeType.STAFF)) {
                    teamData.setCostTotalAmount(new BigDecimal("2341.00").negate());
                    teamData.getEmployeeDataList().get(0).setEmployeeGrossPoolRevenueAmount(new BigDecimal("3232.00").negate());
                    teamData.setTeamAmountRefCell("H8");
                    assertThat(actualCompanyData.getTeamDataList()).contains(teamData);
                }
            }
        }
    }

    @Test
    void getMappedDataFromExcelWithTeamCostForManagement() throws BadDataException {
        Mockito.when(sieFileDataToCompanyDataMapper.getCompanyDataForSieFile(Mockito.any())).thenReturn(getCompanyData());
        CompanyData actualCompanyData = accountingDataToCompanyDataMapper.getMappedDataFromExcel(getExcelSheetAccountingData(), getExcelSheetCompanyData());
        for (TeamData teamData : getCompanyData().getTeamDataList()) {
            for (EmployeeData employeeData : teamData.getEmployeeDataList()) {
                if (employeeData.getEmployeeType().equals(EmployeeType.MANAGEMENT)) {
                    teamData.setCostTotalAmount(new BigDecimal("4000.00").negate());
                    teamData.getEmployeeDataList().get(0).setEmployeeGrossPoolRevenueAmount(new BigDecimal("200.00").negate());
                    teamData.setTeamAmountRefCell("H9");
                    assertThat(actualCompanyData.getTeamDataList()).contains(teamData);
                }
            }

        }
    }

    private CompanyData getCompanyData() {
        CompanyData companyData = new CompanyData();
        List<TeamData> teamDataList = new ArrayList<>();
        List<EmployeeData> employeeDataListStaff = new ArrayList<>();
        employeeDataListStaff.add(new EmployeeData(EmployeeType.STAFF, "4 Nils Åkesson", "4"));
        teamDataList.add(new TeamData("Team 1", "1", null, employeeDataListStaff));
        List<EmployeeData> employeeDataListManagement = new ArrayList<>();
        employeeDataListManagement.add(new EmployeeData(EmployeeType.MANAGEMENT, "3 Åsa Olofsson", "3"));
        teamDataList.add(new TeamData("Team 2", "2", null, employeeDataListManagement));
        companyData.setTeamDataList(teamDataList);
        return companyData;
    }


    private ExcelSheetData getExcelSheetCompanyData() {
        ExcelSheetData excelSheetData = new ExcelSheetData();
        excelSheetData.setExcelSheetName("SI-fildata");
        List<ExcelCell> excelCellList = new ArrayList<>(getExcelTeamListCells());
        excelSheetData.setExcelCellList(excelCellList);
        return excelSheetData;
    }

    private List<ExcelCell> getExcelTeamListCells() {
        List<ExcelCell> excelCellList = new ArrayList<>();
        excelCellList.add(getTeamCell("Team namn", "A", 4));
        excelCellList.add(getTeamCell("Team 1", "A", 5));
        excelCellList.add(getTeamCell("1", "B", 5));
        return excelCellList;
    }

    private ExcelCell getTeamCell(String value, String column, int row) {
        ExcelCell excelCell = new ExcelCell();
        excelCell.setCellType(CellType.STRING);
        excelCell.setCellReference(column + row);
        excelCell.setValue(value);
        excelCell.setRow(row);
        excelCell.setColumn(column);
        return excelCell;
    }

    private ExcelSheetData getExcelSheetAccountingData() {
        ExcelSheetData excelSheetData = new ExcelSheetData();
        excelSheetData.setExcelSheetName("TestFile");
        List<ExcelCell> excelCellList = new ArrayList<>();
        excelCellList.add(getExcelCellMonthlyChange("Månadens föränd. ledning"));
        excelCellList.add(getExcelCellMonthlyChange("Månadens föränd. personal"));
        excelCellList.add(getExcelCellMonthlyChange("Månadens kostn ledning"));
        excelCellList.add(getExcelCellAmount("200", "H", 4));
        excelCellList.add(getExcelCellAmount("20012", "E", 5));
        excelCellList.add(getExcelCellAmount("4012", "E", 7));
        excelCellList.add(getExcelCellAmount("Team 1", "K", 8));
        excelCellList.add(getExcelCellAmount("Team 2", "K", 9));
        excelCellList.add(getExcelCellAmount("2341", "H", 8));
        excelCellList.add(getExcelCellAmount("4000", "H", 9));
        excelCellList.add(getExcelCellAmount("4 Nils Åkesson", "C", 35));
        excelCellList.add(getExcelCellAmount("3 Åsa Olofsson", "C", 14));
        excelCellList.add(getExcelCellAmount("3232", "C", 48));
        excelCellList.add(getExcelCellAmount("200", "C", 27));
        excelSheetData.setExcelCellList(excelCellList);
        return excelSheetData;
    }

    private ExcelCell getExcelCellAmount(String value, String column, int row) {
        ExcelCell excelCell = new ExcelCell();
        excelCell.setCellType(CellType.STRING);
        excelCell.setCellReference(column + row);
        excelCell.setValue(value);
        excelCell.setRow(row);
        excelCell.setColumn(column);
        excelCell.setRawValue("=!A7");
        return excelCell;
    }


    private ExcelCell getExcelCellMonthlyChange(String s) {
        ExcelCell excelCell = new ExcelCell();
        excelCell.setCellType(CellType.STRING);
        excelCell.setCellReference("C4");
        excelCell.setValue(s);
        excelCell.setRow(4);
        excelCell.setColumn("C");
        excelCell.setRawValue("=!A7");
        return excelCell;
    }


}