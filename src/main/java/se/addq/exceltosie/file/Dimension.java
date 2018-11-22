package se.addq.exceltosie.file;

import java.util.Objects;

public class Dimension {

    private int dimensionNumber;

    private String dimensionName;

    public int getDimensionNumber() {
        return dimensionNumber;
    }

    public void setDimensionNumber(int dimensionNumber) {
        this.dimensionNumber = dimensionNumber;
    }

    public String getDimensionName() {
        return dimensionName;
    }

    public void setDimensionName(String dimensionName) {
        this.dimensionName = dimensionName;
    }

    Dimension(int dimensionNumber, String dimensionName) {
        this.dimensionNumber = dimensionNumber;
        this.dimensionName = dimensionName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dimension dimension = (Dimension) o;
        return dimensionNumber == dimension.dimensionNumber &&
                Objects.equals(dimensionName, dimension.dimensionName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dimensionNumber, dimensionName);
    }

    @Override
    public String toString() {
        return "Dimension{" +
                "dimensionNumber=" + dimensionNumber +
                ", dimensionName='" + dimensionName + '\'' +
                '}';
    }
}
