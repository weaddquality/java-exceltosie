package se.addq.exceltosie.companydata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.addq.exceltosie.error.BadDataException;
import se.addq.exceltosie.excel.ExcelSheetData;
import se.addq.exceltosie.utils.ExcelCellUtil;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

import static se.addq.exceltosie.utils.ExcelCellUtil.getExcelCellReferenceByValue;

/**
 * Class for mapping SI-file specific organisation data.
 * The tab contains data for company, team and employees not set in original file
 */
public class SieFileDataToCompanyDataMapper {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String COMPANY_NAME_CELL = "A2";
    private static final String ORGANIZATION_NUMBER_CELL_ID = "B2";
    private static final String HEADING_TEXT_SIETAB_TEAM_NAMN = "Team namn";
    private static final String HEADING_TEXT_SIETAB_EMPLOYEE_NAME = "Anställd namn";
    private static final String COLUMN_EMPLOYEE_NAME = "A";
    private static final String COLUMN_EMPLOYEE_TEAMNAME = "C";
    private static final String COLUMN_EMPLOYEE_TYPE = "D";
    private static final String COLUMN_EMPLOYEE_ID = "B";
    private static final String COLUMN_TEAM_NAME = "A";
    private static final String COLUMN_TEAM_ID = "B";

    private CompanyData companyData;

    private ExcelSheetData companyDataSheet;


    CompanyData getCompanyDataForSieFile(ExcelSheetData companyDataSheet) throws BadDataException {
        this.companyDataSheet = companyDataSheet;
        this.companyData = new CompanyData();
        setCompanyData();
        setTeamData();
        setEmployeeData();
        setManagementData();
        return companyData;
    }

    private void setManagementData() {
        for (TeamData teamData : companyData.getTeamDataList()) {
            for (EmployeeData employeeData : teamData.getEmployeeDataList()) {
                if (employeeData.getEmployeeType().equals(EmployeeType.MANAGEMENT)) {
                    ManagementData managementData = new ManagementData();
                    managementData.setTeamName(teamData.getTeamName());
                    LOG.info("Set management team to {}", managementData.getTeamName());
                    companyData.setManagementData(managementData);
                }
            }
        }
    }

    private void setCompanyData() throws BadDataException {
        String value = getExcelCellValue(COMPANY_NAME_CELL);
        LOG.info("Set company name to value {} from cell {}", value, COMPANY_NAME_CELL);
        companyData.setOrganizationNumber(getExcelCellValue(ORGANIZATION_NUMBER_CELL_ID));
        companyData.setCompanyName(value);
    }


    private void setTeamData() throws BadDataException {
        List<TeamData> teamDataList = new ArrayList<>();
        int headingRowId = getStartCellRowForValueExcel(HEADING_TEXT_SIETAB_TEAM_NAMN);
        int row = headingRowId + 1;
        boolean moreExists = true;
        do {
            String teamName = getExcelCellValue(COLUMN_TEAM_NAME + row).trim();
            if (teamName.length() > 0) {
                try {
                    TeamData teamData = new TeamData(teamName, getExcelCellValue(COLUMN_TEAM_ID + row).trim());
                    if (teamDataList.stream().noneMatch(td -> td.getTeamName().equals(teamData.getTeamName()))) {
                        teamDataList.add(teamData);
                    } else {
                        throw new BadDataException("Det finns flera team med samma namn " + teamName + " i flik " + companyDataSheet.getExcelSheetName());
                    }
                } catch (IllegalArgumentException e) {
                    throw new BadDataException("Kan inte hitta data för värde " + teamName + " i " + companyDataSheet.getExcelSheetName());
                }
                row++;
            } else {
                moreExists = false;
            }
        } while (moreExists);
        companyData.setTeamDataList(teamDataList);
    }

    private void setEmployeeData() throws BadDataException {
        int headingRowId = getStartCellRowForValueExcel(HEADING_TEXT_SIETAB_EMPLOYEE_NAME);
        int row = headingRowId + 1;
        boolean moreExists = true;
        do {
            String employeeName = getExcelCellValue(COLUMN_EMPLOYEE_NAME + row).trim();
            if (employeeName.length() > 0) {
                String teamName = getExcelCellValue(COLUMN_EMPLOYEE_TEAMNAME + row).trim();
                String type = getExcelCellValue(COLUMN_EMPLOYEE_TYPE + row).trim();
                EmployeeData employeeData = new EmployeeData(getEmployeeType(type), employeeName, getExcelCellValue(COLUMN_EMPLOYEE_ID + row));
                if (isExisting(companyData.getTeamDataList(), employeeData)) {
                    throw new BadDataException("Det finns flera anställda med samma namn " + employeeName + " i flik " + companyDataSheet.getExcelSheetName());
                } else {
                    addEmployeeToTeam(teamName, employeeData);
                }
                row++;
            } else {
                moreExists = false;
            }
        } while (moreExists);
    }

    private void addEmployeeToTeam(String teamName, EmployeeData employeeData) throws BadDataException {
        TeamData teamDataToSet = companyData.getTeamDataList().stream().filter(teamData -> teamData.getTeamName().equals(teamName)).findFirst().orElseThrow(() -> new BadDataException("Hittar inget team som matchar " + teamName));
        teamDataToSet.getEmployeeDataList().add(employeeData);
    }

    private EmployeeType getEmployeeType(String type) {
        EmployeeType employeeType = EmployeeType.STAFF;
        if (!type.equals("")) {
            employeeType = EmployeeType.valueOf(type);
        }
        return employeeType;
    }

    private boolean isExisting(List<TeamData> teamDataList, EmployeeData employeeData) {
        return teamDataList.stream().flatMap(teamData -> teamData.getEmployeeDataList().stream()).anyMatch(ed -> ed.getName().equals(employeeData.getName()));
    }

    private int getStartCellRowForValueExcel(String value) throws BadDataException {
        String cell = getExcelCellReferenceByValue(value, companyDataSheet);
        if ("".equals(cell)) {
            throw new BadDataException("Kan inte hitta cell i flik " + companyDataSheet.getExcelSheetName() + " för " + value);
        }
        LOG.info("Found cell {} for value {}", cell, value);
        return Integer.valueOf(cell.substring(1));
    }


    private String getExcelCellValue(String cellId) throws BadDataException {
        return ExcelCellUtil.getExcelValueByCellReference(cellId, companyDataSheet);
    }


}
