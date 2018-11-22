package se.addq.exceltosie.companydata;

import java.math.BigDecimal;

public class ManagementData {

    private String teamName = "";

    private BigDecimal monthlyTeamCostManagement;

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public BigDecimal getMonthlyTeamCostManagement() {
        return monthlyTeamCostManagement;
    }

    public void setMonthlyTeamCostManagement(BigDecimal monthlyTeamCostManagement) {
        this.monthlyTeamCostManagement = monthlyTeamCostManagement;
    }

    @Override
    public String toString() {
        return "ManagementData{" +
                "teamName='" + teamName + '\'' +
                ", monthlyTeamCostManagement=" + monthlyTeamCostManagement +
                '}';
    }
}
