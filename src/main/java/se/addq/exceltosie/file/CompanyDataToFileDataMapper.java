package se.addq.exceltosie.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.addq.exceltosie.companydata.CompanyData;
import se.addq.exceltosie.companydata.EmployeeData;
import se.addq.exceltosie.companydata.EmployeeType;
import se.addq.exceltosie.companydata.TeamData;
import se.addq.exceltosie.error.BadDataException;
import se.addq.exceltosie.utils.DateUtil;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static se.addq.exceltosie.companydata.CompanyData.*;

/**
 * Class for mapping internal data model of accounting and company data to sie-file specific objects.
 */
public class CompanyDataToFileDataMapper {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private FileData fileData = new FileData();

    private CompanyData companyData;

    FileData getMappedDataFromCompanyData(CompanyData companyData) throws BadDataException {
        fileData.setVerificationPost(new VerificationPost());
        this.companyData = companyData;
        setBaseData();
        setTransactionData();
        return fileData;
    }


    private void setBaseData() throws BadDataException {
        setGenerationDate();
        setCompanyName();
        setOrganisationNumber();
        setAccounts();
        setDimensions();
        setSieObjects();
    }

    private void setGenerationDate() throws BadDataException {
        String value = DateUtil.getDateAsStringForAccountingMonth(companyData.getAccountingMonth());
        LOG.info("Set generation date to value {} from accounting month", value);
        fileData.setGenerationDate(value);
    }

    private void setCompanyName() {
        fileData.setCompanyName(companyData.getCompanyName());
    }

    private void setOrganisationNumber() {
        fileData.setOrganisationNumber(companyData.getOrganizationNumber());
    }

    private void setAccounts() {
        List<Account> accountList = new ArrayList<>();
        companyData.getAccountIdToTextMapping().forEach((k, v) -> accountList.add(new Account(k, v)));
        fileData.setAccountList(accountList);
    }

    private void setDimensions() {
        List<Dimension> dimensionList = new ArrayList<>();
        dimensionList.add(new Dimension(COST_CENTER_ID, "Kostnadsställe"));
        dimensionList.add(new Dimension(PROJECT_ID, "Projekt"));
        fileData.setDimensionList(dimensionList);
    }

    private void setSieObjects() {
        List<SieObject> sieObjectList = new ArrayList<>();
        sieObjectList.addAll(getTeamObjectList());
        sieObjectList.addAll(getEmployeeObjectList());
        sieObjectList.addAll(getBaseObjectList());
        fileData.setSieObjectList(sieObjectList);
    }

    private List<SieObject> getBaseObjectList() {
        List<SieObject> sieObjectList = new ArrayList<>();
        sieObjectList.add(new SieObject(PROJECT_ID, CHANGE_GROSS_POOL_ACCOUNT_900_84_ID, companyData.getObjectAccountIdToTextMapping().get(CHANGE_GROSS_POOL_ACCOUNT_900_84_ID)));
        sieObjectList.add(new SieObject(PROJECT_ID, ACCOUNT_ZERO_30_ID, companyData.getObjectAccountIdToTextMapping().get(ACCOUNT_ZERO_30_ID)));
        return sieObjectList;
    }

    private List<SieObject> getEmployeeObjectList() {
        List<SieObject> sieObjectList = new ArrayList<>();
        for (EmployeeData employeeData : getFlatMapEmployeeDataList()) {
            sieObjectList.add(new SieObject(PROJECT_ID, employeeData.getId(), employeeData.getName()));
        }
        return sieObjectList;
    }

    private List<EmployeeData> getFlatMapEmployeeDataList() {
        return companyData.getTeamDataList().stream().flatMap(teamData -> teamData.getEmployeeDataList().stream()).collect(Collectors.toList());
    }

    private List<SieObject> getTeamObjectList() {
        List<SieObject> sieObjectList = new ArrayList<>();
        if (companyData.getTeamDataList() == null) {
            return sieObjectList;
        }
        for (TeamData teamData : companyData.getTeamDataList()) {
            sieObjectList.add(new SieObject(COST_CENTER_ID, teamData.getTeamId(), teamData.getTeamName()));
        }
        return sieObjectList;
    }


    private void setTransactionData() throws BadDataException {
        setTransactionsHeaderData();
        setTransactionGrossPoolDebtManagement();
        setTransactionGrossPoolDebtEmployees();
        setTransactionMonthlyCostGrossPoolTeamStaff();
        setTransactionMonthlyCostGrossPoolTeamManagement();
        setTransactionMonthlyRevenueGrossPoolEmployee();
        setTransactionGrossPoolChangeManagement();
        setTransactionGrossPoolChangeEmployees();
    }

    private void setTransactionMonthlyRevenueGrossPoolEmployee() throws BadDataException {
        List<Transaction> transactionsToAdd = new ArrayList<>();
        for (TeamData teamData : companyData.getTeamDataList()) {
            LOG.info("Will add transactions for team {}", teamData.getTeamName());
            for (EmployeeData employeeData : teamData.getEmployeeDataList()) {
                Map<Integer, String> map = new HashMap<>();
                map.put(PROJECT_ID, employeeData.getId());
                if (employeeData.getEmployeeType() == null) {
                    throw new BadDataException("No type set on employee " + employeeData.getName());
                }
                if(employeeData.getEmployeeGrossPoolRevenueAmount().compareTo(new BigDecimal("0")) == 0) {
                    LOG.info("Employee {} grosspool revenue amount zero: {} no transaction set", employeeData.getId(), employeeData.getEmployeeGrossPoolRevenueAmount());
                    continue;
                }
                Transaction transaction;
                if (employeeData.getEmployeeType().equals(EmployeeType.STAFF)) {
                    transaction = new Transaction(GROSS_POOL_REVENUE_AND_SALARY_EMPLOYEES_7010_ID, map, employeeData.getEmployeeGrossPoolRevenueAmount(), "");
                } else {
                    transaction = new Transaction(GROSS_POOL_SALARY_MANAGEMENT_7020_ID, map, employeeData.getEmployeeGrossPoolRevenueAmount(), "");
                }
                LOG.info("Will add transaction {} for employee {}", transaction, employeeData.getId());
                transactionsToAdd.add(transaction);
            }
        }
        LOG.info("Will add {} number of employee transactions", transactionsToAdd.size());
        fileData.getVerificationPost().getTransactionList().addAll(transactionsToAdd);
    }


    private void setTransactionsHeaderData() throws BadDataException {
        fileData.getVerificationPost().setVerificationDate(DateUtil.getDateAsStringForAccountingMonth(companyData.getAccountingMonth()));
        fileData.getVerificationPost().setVerificationText("Bruttopool " + companyData.getAccountingMonth().toLowerCase());
        fileData.getVerificationPost().setRegistrationDate(DateUtil.getDateAsStringForAccountingMonth(companyData.getAccountingMonth()));
    }

    private void setTransactionGrossPoolChangeManagement() {
        Map<Integer, String> map = new HashMap<>();
        map.put(PROJECT_ID, CHANGE_GROSS_POOL_ACCOUNT_900_84_ID);
        Transaction transaction = new Transaction(CompanyData.GROSS_POOL_CHANGE_MANAGEMENT_7018_ID, map, companyData.getGrossPoolChangeEmployees7018(), "");
        fileData.getVerificationPost().getTransactionList().add(transaction);
    }

    private void setTransactionGrossPoolChangeEmployees() {
        Map<Integer, String> map = new HashMap<>();
        map.put(PROJECT_ID, CHANGE_GROSS_POOL_ACCOUNT_900_84_ID);
        Transaction transaction = new Transaction(CompanyData.GROSS_POOL_CHANGE_EMPLOYEES_7029_ID, map, companyData.getGrossPoolChangeManagement7029(), "");
        fileData.getVerificationPost().getTransactionList().add(transaction);
    }


    private void setTransactionGrossPoolDebtManagement() {
        Transaction transaction = new Transaction(CompanyData.GROSS_POOL_DEBT_MANAGEMENT_2802_ID, companyData.getGrossPoolDebtManagement2802());
        fileData.getVerificationPost().getTransactionList().add(transaction);
    }

    private void setTransactionGrossPoolDebtEmployees() {
        Transaction transaction = new Transaction(GROSS_POOL_DEBT_EMPLOYEES_2801_ID, companyData.getGrossPoolDebtEmployees2801());
        fileData.getVerificationPost().getTransactionList().add(transaction);
    }

    private void setTransactionMonthlyCostGrossPoolTeamStaff() {
        for (TeamData teamData : companyData.getTeamDataList()) {
            if (teamData.isOnlyManagementTeam()) {
                return;
            }
            Map<Integer, String> map = new HashMap<>();
            map.put(COST_CENTER_ID, teamData.getTeamId());
            map.put(PROJECT_ID, ACCOUNT_ZERO_30_ID);
            Transaction transaction = new Transaction(GROSS_POOL_REVENUE_AND_SALARY_EMPLOYEES_7010_ID, map, teamData.getCostTotalAmount(), "");
            fileData.getVerificationPost().getTransactionList().add(transaction);
        }
    }

    private void setTransactionMonthlyCostGrossPoolTeamManagement() {
        for (TeamData teamData : companyData.getTeamDataList()) {
            if (teamData.getTeamName().equals(companyData.getManagementData().getTeamName())) {
                Map<Integer, String> map = new HashMap<>();
                map.put(COST_CENTER_ID, teamData.getTeamId());
                map.put(PROJECT_ID, ACCOUNT_ZERO_30_ID);
                Transaction transaction = new Transaction(GROSS_POOL_SALARY_MANAGEMENT_7020_ID, map, companyData.getManagementData().getMonthlyTeamCostManagement(), "");
                fileData.getVerificationPost().getTransactionList().add(transaction);
            }
        }
    }

}
