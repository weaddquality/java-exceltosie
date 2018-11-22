package se.addq.exceltosie.file;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import se.addq.exceltosie.companydata.*;
import se.addq.exceltosie.error.BadDataException;
import se.addq.exceltosie.utils.DateUtil;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class CompanyDataToFileDataMapperTest {

    @InjectMocks
    private CompanyDataToFileDataMapper companyDataToFileDataMapper;


    @Test
    void getMappedDefaultFileDataForAccountCompanyData() throws BadDataException {
        FileData fileData = companyDataToFileDataMapper.getMappedDataFromCompanyData(getCompanyData());
        assertThat(fileData.getAccountList()).contains(new Account(2801, "Bruttopoolsskuld till personal"));
        assertThat(fileData.getAccountList()).contains(new Account(2802, "Bruttopoolsskuld till f-ledning"));
        assertThat(fileData.getAccountList()).contains(new Account(7010, "Bruttopoolsintäkt + grundlön inkl.soc.avgLöner företagsledare"));
        assertThat(fileData.getAccountList()).contains(new Account(7029, "Förändring bruttopool personal"));
        assertThat(fileData.getAccountList()).contains(new Account(7018, "Förändring bruttopool f-ledare"));
        assertThat(fileData.getAccountList()).contains(new Account(7020, "Lön tjänstemän"));
    }

    @Test
    void getMappedDefaultFileDataForDimensionCompanyData() throws BadDataException {
        FileData fileData = companyDataToFileDataMapper.getMappedDataFromCompanyData(getCompanyData());
        assertThat(fileData.getDimensionList()).contains(new Dimension(1, "Kostnadsställe"));
        assertThat(fileData.getDimensionList()).contains(new Dimension(6, "Projekt"));
    }


    @Test
    void getMappedDefaultFileDataForSieObjectsCompanyData() throws BadDataException {
        FileData fileData = companyDataToFileDataMapper.getMappedDataFromCompanyData(getCompanyData());
        assertThat(fileData.getSieObjectList()).contains(new SieObject(6, "84", "900 Förändring bruttopool"));
        assertThat(fileData.getSieObjectList()).contains(new SieObject(6, "30", "Nollan"));
    }


    @Test
    void getMappedDefaultFileDataForBaseCompanyData() throws BadDataException {
        String accountingMonth = "Augusti";
        String expectedDate = DateUtil.getDateAsStringForAccountingMonth(accountingMonth);
        CompanyData expectedCompanyData = new CompanyData();
        expectedCompanyData.setAccountingMonth(accountingMonth);
        expectedCompanyData.setCompanyName("Test company");
        expectedCompanyData.setOrganizationNumber("5346623-1");
        FileData fileData = companyDataToFileDataMapper.getMappedDataFromCompanyData(expectedCompanyData);
        assertThat(fileData.getCompanyName()).isEqualTo(expectedCompanyData.getCompanyName());
        assertThat(fileData.getOrganisationNumber()).isEqualTo(expectedCompanyData.getOrganizationNumber());
        assertThat(fileData.getGenerationDate()).isEqualTo(expectedDate);
    }


    @Test
    void getMappedFileDataVerificationPostDataFromCompanyData() throws BadDataException {
        String accountingMonth = "Augusti";
        String expectedDate = DateUtil.getDateAsStringForAccountingMonth(accountingMonth);
        CompanyData companyData = new CompanyData();
        companyData.setAccountingMonth(accountingMonth);
        FileData fileData = companyDataToFileDataMapper.getMappedDataFromCompanyData(companyData);
        assertThat(fileData.getVerificationPost().getVerificationDate()).isEqualTo(expectedDate);
        assertThat(fileData.getVerificationPost().getRegistrationDate()).isEqualTo(expectedDate);
        assertThat(fileData.getVerificationPost().getVerificationText()).isEqualTo("Bruttopool augusti");
    }

    @Test
    void getMappedFileDataVerificationTransactionDataFromCompanyData() throws BadDataException {
        CompanyData companyData = getCompanyData();
        FileData fileData = companyDataToFileDataMapper.getMappedDataFromCompanyData(companyData);
        assertThat(fileData.getVerificationPost().getTransactionList()).contains(new Transaction(2802, null, companyData.getGrossPoolDebtManagement2802(), ""));
        assertThat(fileData.getVerificationPost().getTransactionList()).contains(new Transaction(2801, null, companyData.getGrossPoolDebtEmployees2801(), ""));
        assertThat(fileData.getVerificationPost().getTransactionList()).contains(new Transaction(7018, getIntegerStringMap(6, "84"), companyData.getGrossPoolChangeEmployees7018(), ""));
        assertThat(fileData.getVerificationPost().getTransactionList()).contains(new Transaction(7029, getIntegerStringMap(6, "84"), companyData.getGrossPoolChangeManagement7029(), ""));
        for (TeamData teamData : companyData.getTeamDataList()) {
            assertThat(fileData.getVerificationPost().getTransactionList()).contains(new Transaction(7010, getIntegerStringMap(1, teamData.getTeamId(), 6, "30"), teamData.getCostTotalAmount(), ""));
        }
        for (TeamData teamData : companyData.getTeamDataList()) {
            for (EmployeeData employeeData : teamData.getEmployeeDataList()) {
                if (employeeData.getEmployeeType().equals(EmployeeType.STAFF)) {
                    assertThat(fileData.getVerificationPost().getTransactionList()).contains(new Transaction(7010, getIntegerStringMap(6, employeeData.getId()), employeeData.getEmployeeGrossPoolRevenueAmount(), ""));
                } else {
                    assertThat(fileData.getVerificationPost().getTransactionList()).contains(new Transaction(7020, getIntegerStringMap(6, employeeData.getId()), employeeData.getEmployeeGrossPoolRevenueAmount(), ""));
                }
            }
        }
        assertThat(fileData.getVerificationPost().getTransactionList()).contains(new Transaction(7020, getIntegerStringMap(1, getTeamIdForManagementTeam(companyData), 6, "30"), companyData.getManagementData().getMonthlyTeamCostManagement(), ""));
    }


    @Test
    void getCorrectNumberOfTransactionsBasedOnCompanyDataWithOneTeamOnlyManagement() throws BadDataException {
        CompanyData companyDataTest = getCompanyData();
        int numberOfTeamTransactions = companyDataTest.getTeamDataList().size() - 1;
        companyDataTest.getTeamDataList().get(1).setOnlyManagementTeam(true);
        int numberOfEmployees = companyDataTest.getTeamDataList().stream().map(TeamData::getEmployeeDataList).collect(Collectors.toList()).size();
        int companyTransactions = 4;
        int managementTeamTransactions = 1;
        int expectedNumberOfTransactions = companyTransactions + numberOfTeamTransactions + numberOfEmployees + managementTeamTransactions;
        FileData fileData = companyDataToFileDataMapper.getMappedDataFromCompanyData(companyDataTest);
        assertThat(fileData.getVerificationPost().getTransactionList().size()).isEqualTo(expectedNumberOfTransactions);
    }

    @Test
    void getCorrectNumberOfTransactionsWhenEmployeeWithZeroAmount() throws BadDataException {
        CompanyData companyDataTest = getCompanyDataZeroAmount();
        int numberOfTeamTransactions = companyDataTest.getTeamDataList().size();
        int numberOfEmployees = companyDataTest.getTeamDataList().stream().map(TeamData::getEmployeeDataList).collect(Collectors.toList()).size();
        int companyTransactions = 4;
        int managementTeamTransactions = 1;
        int expectedNumberOfTransactions = companyTransactions + numberOfTeamTransactions + numberOfEmployees + managementTeamTransactions;
        FileData fileData = companyDataToFileDataMapper.getMappedDataFromCompanyData(companyDataTest);
        assertThat(fileData.getVerificationPost().getTransactionList().size()).isEqualTo(expectedNumberOfTransactions);
    }



    @Test
    void getCorrectNumberOfTransactionsBasedOnCompanyDataCombinedManagementAndStaffTeam() throws BadDataException {
        CompanyData companyData = getCompanyData();
        EmployeeData employeeData = new EmployeeData(EmployeeType.STAFF, "Gunnar", "3");
        employeeData.setEmployeeGrossPoolRevenueAmount(new BigDecimal("13421"));
        companyData.getTeamDataList().get(1).getEmployeeDataList().add(employeeData);
        int numberOfTeamTransactions = companyData.getTeamDataList().size();
        int numberOfEmployees = companyData.getTeamDataList().stream()
                .map(TeamData::getEmployeeDataList)
                .mapToInt(List::size)
                .sum();
        int companyTransactions = 4;
        int managementTeamTransactions = 1;
        int expectedNumberOfTransactions = companyTransactions + numberOfTeamTransactions + numberOfEmployees + managementTeamTransactions;
        FileData fileData = companyDataToFileDataMapper.getMappedDataFromCompanyData(companyData);
        assertThat(fileData.getVerificationPost().getTransactionList().size()).isEqualTo(expectedNumberOfTransactions);
    }

    private CompanyData getCompanyDataZeroAmount() {
        CompanyData companyData = getCompanyOnlyData();
        companyData.setTeamDataList(getTeamDataWithZeroAmount());
        return companyData;
    }



    private CompanyData getCompanyData() {
        CompanyData companyData = getCompanyOnlyData();
        ManagementData managementData = new ManagementData();
        managementData.setTeamName("Team2");
        managementData.setMonthlyTeamCostManagement(new BigDecimal("3473.00"));
        companyData.setManagementData(managementData);
        return companyData;
    }

    private CompanyData getCompanyOnlyData() {
        CompanyData companyData = new CompanyData();
        companyData.setAccountingMonth("Augusti");
        companyData.setTeamDataList(getTeamData());
        companyData.setGrossPoolChangeEmployees7018(new BigDecimal("1212"));
        companyData.setGrossPoolDebtEmployees2801(new BigDecimal("-8900.00"));
        companyData.setGrossPoolChangeManagement7029(new BigDecimal("-12400.00"));
        companyData.setGrossPoolDebtManagement2802(new BigDecimal("8700.00"));
        return companyData;
    }

    private String getTeamIdForManagementTeam(CompanyData companyData) {
        Optional<TeamData> optional = companyData.getTeamDataList().stream().filter(teamdata -> teamdata.getTeamName().equals(companyData.getManagementData().getTeamName())).findFirst();
        if (optional.isPresent()) {
            return optional.get().getTeamId();
        }
        return "";
    }

    private Map<Integer, String> getIntegerStringMap(int key, String value) {
        Map<Integer, String> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    private Map<Integer, String> getIntegerStringMap(int key1, String value1, int key2, String value2) {
        Map<Integer, String> map = new HashMap<>();
        map.put(key1, value1);
        map.put(key2, value2);
        return map;
    }

    private List<TeamData> getTeamDataWithZeroAmount() {
        List<TeamData> teamDataList = new ArrayList<>();
        List<EmployeeData> employeeDataListTeam1 = new ArrayList<>();
        EmployeeData employeeData = new EmployeeData(EmployeeType.STAFF, "Lisa", "0", new BigDecimal("2172.00"));
        EmployeeData employeeDataZeroAmount = new EmployeeData(EmployeeType.STAFF, "Nisse", "1", new BigDecimal("0.00"));
        EmployeeData employeeData1 = new EmployeeData(EmployeeType.STAFF, "Kalle", "2", new BigDecimal("23672.00"));
        employeeDataListTeam1.add(employeeData);
        employeeDataListTeam1.add(employeeDataZeroAmount);
        employeeDataListTeam1.add(employeeData1);
        teamDataList.add(new TeamData("Team1", "1", new BigDecimal("5004"), employeeDataListTeam1));
        return teamDataList;
    }

    private List<TeamData> getTeamData() {
        List<TeamData> teamDataList = new ArrayList<>();
        List<EmployeeData> employeeDataListTeam1 = new ArrayList<>();
        EmployeeData employeeData = new EmployeeData(EmployeeType.STAFF, "Nisse", "1");
        employeeData.setEmployeeGrossPoolRevenueAmount(new BigDecimal("23672"));
        employeeDataListTeam1.add(employeeData);
        teamDataList.add(new TeamData("Team1", "1", new BigDecimal("5004"), employeeDataListTeam1));
        List<EmployeeData> employeeDataListTeam2 = new ArrayList<>();
        EmployeeData employeeData1 = new EmployeeData(EmployeeType.MANAGEMENT, "Kalle", "2");
        employeeData1.setEmployeeGrossPoolRevenueAmount(new BigDecimal("32783"));
        employeeDataListTeam2.add(employeeData1);
        teamDataList.add(new TeamData("Team2", "2", new BigDecimal("2323"), employeeDataListTeam2));
        return teamDataList;
    }
}