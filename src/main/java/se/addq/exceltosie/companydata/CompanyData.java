package se.addq.exceltosie.companydata;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompanyData {

    public static final int GROSS_POOL_DEBT_EMPLOYEES_2801_ID = 2801;
    public static final int GROSS_POOL_DEBT_MANAGEMENT_2802_ID = 2802;
    public static final int GROSS_POOL_SALARY_MANAGEMENT_7020_ID = 7020;
    public static final int GROSS_POOL_REVENUE_AND_SALARY_EMPLOYEES_7010_ID = 7010;
    public static final int GROSS_POOL_CHANGE_EMPLOYEES_7029_ID = 7029;
    public static final int GROSS_POOL_CHANGE_MANAGEMENT_7018_ID = 7018;

    public static final String CHANGE_GROSS_POOL_ACCOUNT_900_84_ID = "84";
    public static final String ACCOUNT_ZERO_30_ID = "30";

    public static final int COST_CENTER_ID = 1;
    public static final int PROJECT_ID = 6;

    private String accountingMonth = "";

    private String companyName;

    private String organizationNumber;

    private BigDecimal grossPoolDebtEmployees2801;

    private BigDecimal grossPoolDebtManagement2802;

    private BigDecimal grossPoolChangeEmployees7018;

    private BigDecimal grossPoolChangeManagement7029;

    private ManagementData managementData = new ManagementData();

    private List<TeamData> teamDataList = new ArrayList<>();

    public String getAccountingMonth() {
        return accountingMonth;
    }

    public void setAccountingMonth(String accountingMonth) {
        this.accountingMonth = accountingMonth;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getOrganizationNumber() {
        return organizationNumber;
    }

    public void setOrganizationNumber(String organizationNumber) {
        this.organizationNumber = organizationNumber;
    }

    public List<TeamData> getTeamDataList() {
        return teamDataList;
    }

    public void setTeamDataList(List<TeamData> teamDataList) {
        this.teamDataList = teamDataList;
    }

    public BigDecimal getGrossPoolDebtEmployees2801() {
        return grossPoolDebtEmployees2801;
    }

    public void setGrossPoolDebtEmployees2801(BigDecimal grossPoolDebtEmployees2801) {
        this.grossPoolDebtEmployees2801 = grossPoolDebtEmployees2801;
    }

    public BigDecimal getGrossPoolDebtManagement2802() {
        return grossPoolDebtManagement2802;
    }

    public void setGrossPoolDebtManagement2802(BigDecimal grossPoolDebtManagement2802) {
        this.grossPoolDebtManagement2802 = grossPoolDebtManagement2802;
    }

    public BigDecimal getGrossPoolChangeEmployees7018() {
        return grossPoolChangeEmployees7018;
    }

    public void setGrossPoolChangeEmployees7018(BigDecimal grossPoolChangeEmployees7018) {
        this.grossPoolChangeEmployees7018 = grossPoolChangeEmployees7018;
    }

    public BigDecimal getGrossPoolChangeManagement7029() {
        return grossPoolChangeManagement7029;
    }

    public void setGrossPoolChangeManagement7029(BigDecimal grossPoolChangeManagement7029) {
        this.grossPoolChangeManagement7029 = grossPoolChangeManagement7029;
    }

    public ManagementData getManagementData() {
        return managementData;
    }

    public void setManagementData(ManagementData managementData) {
        this.managementData = managementData;
    }

    public Map<Integer, String> getAccountIdToTextMapping() {
        Map<Integer, String> map = new HashMap<>();
        map.put(GROSS_POOL_DEBT_EMPLOYEES_2801_ID, "Bruttopoolsskuld till personal");
        map.put(GROSS_POOL_DEBT_MANAGEMENT_2802_ID, "Bruttopoolsskuld till f-ledning");
        map.put(GROSS_POOL_SALARY_MANAGEMENT_7020_ID, "Lön tjänstemän");
        map.put(GROSS_POOL_REVENUE_AND_SALARY_EMPLOYEES_7010_ID, "Bruttopoolsintäkt + grundlön inkl.soc.avgLöner företagsledare");
        map.put(GROSS_POOL_CHANGE_MANAGEMENT_7018_ID, "Förändring bruttopool f-ledare");
        map.put(GROSS_POOL_CHANGE_EMPLOYEES_7029_ID, "Förändring bruttopool personal");
        return map;
    }

    public Map<String, String> getObjectAccountIdToTextMapping() {
        Map<String, String> map = new HashMap<>();
        map.put(CHANGE_GROSS_POOL_ACCOUNT_900_84_ID, "900 Förändring bruttopool");
        map.put(ACCOUNT_ZERO_30_ID, "Nollan");
        return map;
    }


}
