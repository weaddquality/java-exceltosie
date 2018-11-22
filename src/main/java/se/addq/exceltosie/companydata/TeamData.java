package se.addq.exceltosie.companydata;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TeamData {

    private String teamName;
    private String teamId;
    private BigDecimal costTotalAmount;
    private List<EmployeeData> employeeDataList = new ArrayList<>();
    private String teamAmountRefCell;
    private boolean isOnlyManagementTeam;

    public TeamData(String teamName, String teamId) {
        this.teamName = teamName;
        this.teamId = teamId;
    }

    public TeamData(String teamName, String teamId, BigDecimal costTotalAmount, List<EmployeeData> employeeDataList) {
        this.teamName = teamName;
        this.teamId = teamId;
        this.costTotalAmount = costTotalAmount;
        this.employeeDataList = employeeDataList;
    }


    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public BigDecimal getCostTotalAmount() {
        return costTotalAmount;
    }

    void setCostTotalAmount(BigDecimal costTotalAmount) {
        this.costTotalAmount = costTotalAmount;
    }


    public List<EmployeeData> getEmployeeDataList() {
        return employeeDataList;
    }

    void setEmployeeDataList(List<EmployeeData> employeeDataList) {
        this.employeeDataList = employeeDataList;
    }

    public String getTeamAmountRefCell() {
        return teamAmountRefCell;
    }

    void setTeamAmountRefCell(String teamAmountRefCell) {
        this.teamAmountRefCell = teamAmountRefCell;
    }

    public boolean isOnlyManagementTeam() {
        return isOnlyManagementTeam;
    }

    public void setOnlyManagementTeam(boolean onlyManagementTeam) {
        isOnlyManagementTeam = onlyManagementTeam;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeamData teamData = (TeamData) o;
        return isOnlyManagementTeam == teamData.isOnlyManagementTeam &&
                Objects.equals(teamName, teamData.teamName) &&
                Objects.equals(teamId, teamData.teamId) &&
                Objects.equals(costTotalAmount, teamData.costTotalAmount) &&
                Objects.equals(employeeDataList, teamData.employeeDataList) &&
                Objects.equals(teamAmountRefCell, teamData.teamAmountRefCell);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamName, teamId, costTotalAmount, employeeDataList, teamAmountRefCell, isOnlyManagementTeam);
    }

    @Override
    public String toString() {
        return "TeamData{" +
                "teamName='" + teamName + '\'' +
                ", teamId='" + teamId + '\'' +
                ", costTotalAmount=" + costTotalAmount +
                ", employeeDataList=" + employeeDataList +
                ", teamAmountRefCell='" + teamAmountRefCell + '\'' +
                ", isOnlyManagementTeam=" + isOnlyManagementTeam +
                '}';
    }
}
