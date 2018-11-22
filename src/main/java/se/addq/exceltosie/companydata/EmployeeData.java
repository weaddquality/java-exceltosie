package se.addq.exceltosie.companydata;

import java.math.BigDecimal;
import java.util.Objects;

public class EmployeeData {

    private String name;

    private String id;

    private BigDecimal employeeGrossPoolRevenueAmount;

    private EmployeeType employeeType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public EmployeeType getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(EmployeeType employeeType) {
        this.employeeType = employeeType;
    }

    public EmployeeData(EmployeeType employeeType, String name, String id) {
        this.employeeType = employeeType;
        this.name = name;
        this.id = id;
    }

    public EmployeeData(EmployeeType employeeType, String name, String id, BigDecimal employeeGrossPoolRevenueAmount) {
        this.name = name;
        this.id = id;
        this.employeeGrossPoolRevenueAmount = employeeGrossPoolRevenueAmount;
        this.employeeType = employeeType;
    }

    public BigDecimal getEmployeeGrossPoolRevenueAmount() {
        return employeeGrossPoolRevenueAmount;
    }

    public void setEmployeeGrossPoolRevenueAmount(BigDecimal employeeGrossPoolRevenueAmount) {
        this.employeeGrossPoolRevenueAmount = employeeGrossPoolRevenueAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeData that = (EmployeeData) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(id, that.id) &&
                Objects.equals(employeeGrossPoolRevenueAmount, that.employeeGrossPoolRevenueAmount) &&
                employeeType == that.employeeType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id, employeeGrossPoolRevenueAmount, employeeType);
    }

    @Override
    public String toString() {
        return "EmployeeData{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", employeeGrossPoolRevenueAmount=" + employeeGrossPoolRevenueAmount +
                ", employeeType=" + employeeType +
                '}';
    }


}
