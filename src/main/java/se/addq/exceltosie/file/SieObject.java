package se.addq.exceltosie.file;

import java.util.Objects;

public class SieObject {
    private int dimensionNumber;
    private String objectNumber;
    private String objectName;

    public int getDimensionNumber() {
        return dimensionNumber;
    }

    public void setDimensionNumber(int dimensionNumber) {
        this.dimensionNumber = dimensionNumber;
    }

    public String getObjectNumber() {
        return objectNumber;
    }

    public void setObjectNumber(String objectNumber) {
        this.objectNumber = objectNumber;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    SieObject(int dimensionNumber, String objectNumber, String objectName) {
        this.dimensionNumber = dimensionNumber;
        this.objectNumber = objectNumber;
        this.objectName = objectName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SieObject sieObject = (SieObject) o;
        return dimensionNumber == sieObject.dimensionNumber &&
                Objects.equals(objectNumber, sieObject.objectNumber) &&
                Objects.equals(objectName, sieObject.objectName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dimensionNumber, objectNumber, objectName);
    }

    @Override
    public String toString() {
        return "SieObject{" +
                "dimensionNumber=" + dimensionNumber +
                ", objectNumber='" + objectNumber + '\'' +
                ", objectName='" + objectName + '\'' +
                '}';
    }
}
