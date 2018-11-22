package se.addq.exceltosie.companydata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.addq.exceltosie.error.BadDataException;
import se.addq.exceltosie.excel.ExcelCell;
import se.addq.exceltosie.excel.ExcelSheetData;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static se.addq.exceltosie.utils.ExcelCellUtil.getExcelCellByValue;
import static se.addq.exceltosie.utils.ExcelCellUtil.getExcelValueByCellReference;

/**
 * Class for mapping monthly gross pool accounting specific excel data for company, team and employees
 */
public class AccountingDataToCompanyDataMapper {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String ACCOUNTING_MONTH_CELL = "B1";
    private static final String MANAGEMENT_MONTHLY_DEBT_2802_CELL = "E5";
    private static final String EMPLOYEES_MONTHLY_DEBT_2801_CELL = "E7";
    private static final String AMOUNT_COLUMN = "H";
    private static final String MONTHLY_COST_MANAGEMENT_TEXT = "Månadens kostn ledning";
    private static final String MONTHLY_CHANGE_MANAGEMENT_TEXT = "Månadens föränd. ledning";
    private static final String MONTHLY_CHANGE_EMPLOYEES_TEXT = "Månadens föränd. personal";

    private ExcelSheetData accountingDataSheet;

    private SieFileDataToCompanyDataMapper sieFileDataToCompanyDataMapper;

    public AccountingDataToCompanyDataMapper(SieFileDataToCompanyDataMapper sieFileDataToCompanyDataMapper) {
        this.sieFileDataToCompanyDataMapper = sieFileDataToCompanyDataMapper;
    }


    private CompanyData companyData;

    public CompanyData getMappedDataFromExcel(ExcelSheetData accountingDataSheet, ExcelSheetData companyDataSheet) throws BadDataException {
        this.accountingDataSheet = accountingDataSheet;
        companyData = sieFileDataToCompanyDataMapper.getCompanyDataForSieFile(companyDataSheet);
        getAccountingMonthFromFile();
        setCompanyAccountAmounts();
        setStaffTeamAccountAmounts();
        setManagementTeamAccountAmounts();
        setEmployeeAccountAmounts();
        return companyData;
    }


    private void getAccountingMonthFromFile() throws BadDataException {
        companyData.setAccountingMonth(getValueFromExcelCell(ACCOUNTING_MONTH_CELL));
    }


    private void setCompanyAccountAmounts() throws BadDataException {
        setTransactionMonthlyChangeManagement();
        setTransactionMonthlyChangeEmployees();
        setTransactionMonthlyDebtManagement();
        setTransactionMonthlyDebtEmployees();
    }

    private void setTransactionMonthlyDebtManagement() throws BadDataException {
        BigDecimal value = getAmountAsBigDecimal(getValueFromExcelCell(MANAGEMENT_MONTHLY_DEBT_2802_CELL)).negate();
        LOG.info("Set monthly total change management to value {} from cell {}", value, MANAGEMENT_MONTHLY_DEBT_2802_CELL);
        companyData.setGrossPoolDebtManagement2802(value);
    }

    private void setTransactionMonthlyDebtEmployees() throws BadDataException {
        BigDecimal value = getAmountAsBigDecimal(getValueFromExcelCell(EMPLOYEES_MONTHLY_DEBT_2801_CELL)).negate();
        LOG.info("Set monthly total change employees to value {} from cell {}", value, EMPLOYEES_MONTHLY_DEBT_2801_CELL);
        companyData.setGrossPoolDebtEmployees2801(value);
    }


    private void setTransactionMonthlyChangeManagement() throws BadDataException {
        String cell = getMonthlyChangeAmountManagementReferenceCell();
        BigDecimal value = getAmountAsBigDecimal(getValueFromExcelCell(cell));
        LOG.info("Set monthly total change management to value {} from cell {}", value, cell);
        companyData.setGrossPoolChangeManagement7029(value);
    }

    private void setTransactionMonthlyChangeEmployees() throws BadDataException {
        String cell = getMonthlyChangeAmountEmployeesReferenceCell();
        BigDecimal value = getAmountAsBigDecimal(getValueFromExcelCell(cell));
        LOG.info("Set monthly total change employees to value {} from cell {}", value, cell);
        companyData.setGrossPoolChangeEmployees7018(value);
    }


    private void setStaffTeamAccountAmounts() throws BadDataException {
        for (TeamData teamData : companyData.getTeamDataList()) {
            setTransactionMonthlyGrossPoolTeamStaff(teamData);
        }
    }

    private void setTransactionMonthlyGrossPoolTeamManagement() throws BadDataException {
        String cellCost = getTeamManagementCostAmountReferenceCell();
        companyData.getManagementData().setMonthlyTeamCostManagement(getAmountAsBigDecimal(getValueFromExcelCell(cellCost)).negate());
        LOG.info("Set team management data {}", companyData.getManagementData());
    }


    private void setTransactionMonthlyGrossPoolTeamStaff(TeamData teamData) throws BadDataException {
        String cell = getTeamStaffCostAmountReferenceCell(teamData);
        if (teamData.isOnlyManagementTeam()) {
            return;
        }
        teamData.setTeamAmountRefCell(cell);
        BigDecimal amount = getAmountAsBigDecimal(getValueFromExcelCell(cell)).negate();
        teamData.setCostTotalAmount(amount);
        LOG.info("Set team data amount for {} : {}", teamData.getTeamName(), teamData.getCostTotalAmount());
    }


    private void setEmployeeAccountAmounts() throws BadDataException {
        for (TeamData teamData : companyData.getTeamDataList()) {
            for (EmployeeData employeeData : teamData.getEmployeeDataList()) {
                String refCell = getEmployeeRevenueAmountReferenceCell(employeeData.getName());
                BigDecimal amount = getAmountAsBigDecimal(getValueFromExcelCell(refCell)).negate();
                employeeData.setEmployeeGrossPoolRevenueAmount(amount);
                LOG.info("Set employee account data for {} with id {}", employeeData.getName(), employeeData.getId());
            }
        }
    }

    private void setManagementTeamAccountAmounts() throws BadDataException {
        setTransactionMonthlyGrossPoolTeamManagement();
    }

    private String getEmployeeRevenueAmountReferenceCell(String employeeName) throws BadDataException {
        ExcelCell refCell = getExcelCellByValue(employeeName, accountingDataSheet);
        if (refCell.getCellReference() == null) {
            throw new BadDataException(getBadDataCellNotFoundErrorMessage("anställd " + employeeName));
        }
        return getAmountRefCell(refCell.getColumn(), refCell.getRow() + 13);
    }

    private String getTeamStaffCostAmountReferenceCell(TeamData teamData) throws BadDataException {
        ExcelCell refCell = getExcelCellByValue(teamData.getTeamName(), accountingDataSheet);
        if (refCell.getCellReference() == null) {
            if (isTeamOnlyManagement(teamData)) {
                LOG.info("No staff in team will not set amount for team {}", teamData.getTeamName());
                teamData.setOnlyManagementTeam(true);
                return "";
            }
            throw new BadDataException(getBadDataCellNotFoundErrorMessage("team " + teamData.getTeamName()));
        }
        return getAmountRefCell(AMOUNT_COLUMN, refCell.getRow());
    }

    private boolean isTeamOnlyManagement(TeamData teamData) {
        return teamData.getEmployeeDataList().stream().noneMatch(employeeData -> employeeData.getEmployeeType().equals(EmployeeType.STAFF));
    }

    private String getAmountRefCell(String amountColumn, int row) {
        return amountColumn + row;
    }

    private String getTeamManagementCostAmountReferenceCell() throws BadDataException {
        ExcelCell refCell = getExcelCellByValue(MONTHLY_COST_MANAGEMENT_TEXT, accountingDataSheet);
        if (refCell.getCellReference() == null) {
            throw new BadDataException(getBadDataCellNotFoundErrorMessage(MONTHLY_COST_MANAGEMENT_TEXT));
        }
        return getAmountRefCell(AMOUNT_COLUMN, refCell.getRow());
    }


    private String getMonthlyChangeAmountManagementReferenceCell() throws BadDataException {
        ExcelCell refCell = getExcelCellByValue(MONTHLY_CHANGE_MANAGEMENT_TEXT, accountingDataSheet);
        if (refCell.getCellReference() == null) {
            throw new BadDataException(getBadDataCellNotFoundErrorMessage(MONTHLY_CHANGE_MANAGEMENT_TEXT));
        }
        return getAmountRefCell(AMOUNT_COLUMN, refCell.getRow());
    }

    private String getMonthlyChangeAmountEmployeesReferenceCell() throws BadDataException {
        ExcelCell refCell = getExcelCellByValue(MONTHLY_CHANGE_EMPLOYEES_TEXT, accountingDataSheet);
        if (refCell.getCellReference() == null) {
            throw new BadDataException(getBadDataCellNotFoundErrorMessage(MONTHLY_CHANGE_EMPLOYEES_TEXT));
        }
        return getAmountRefCell(AMOUNT_COLUMN, refCell.getRow());
    }

    private String getBadDataCellNotFoundErrorMessage(String cellText) {
        return "Kan inte hitta referens cell för '" + cellText + "' i flik " + accountingDataSheet.getExcelSheetName();
    }

    private BigDecimal getAmountAsBigDecimal(String rawExcelAmount) throws BadDataException {
        if (rawExcelAmount == null) {
            throw new BadDataException("Belopp är null!");
        }
        BigDecimal noDecimals;
        try {
            noDecimals = new BigDecimal(getValueWithoutSpaces(rawExcelAmount)).setScale(0, RoundingMode.HALF_EVEN);
        } catch (NumberFormatException e) {
            LOG.error("Could not parse amount {}", rawExcelAmount, e);
            throw new BadDataException("Kunde inte läsa ut ett belopp från värde" + rawExcelAmount, e);
        }
        return noDecimals.setScale(2, RoundingMode.HALF_EVEN);
    }

    private String getValueWithoutSpaces(String rawExcelAmount) {
        return rawExcelAmount.replaceAll("\\s", "").replaceAll("\u00A0", "");
    }


    private String getValueFromExcelCell(String cellId) throws BadDataException {
        return getExcelValueByCellReference(cellId, accountingDataSheet);
    }


}
