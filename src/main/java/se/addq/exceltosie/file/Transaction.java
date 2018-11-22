package se.addq.exceltosie.file;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

public class Transaction {
    private int accountNumber;
    private Map<Integer, String> objectList;
    private BigDecimal amount;
    private String transactionText = "";

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Map<Integer, String> getObjectList() {
        return objectList;
    }

    public void setObjectList(Map<Integer, String> objectList) {
        this.objectList = objectList;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getTransactionText() {
        return transactionText;
    }

    public void setTransactionText(String transactionText) {
        this.transactionText = transactionText;
    }

    public Transaction(int accountNumber, Map<Integer, String> objectList, BigDecimal amount, String transactionText) {
        this.accountNumber = accountNumber;
        this.objectList = objectList;
        this.amount = amount;
        this.transactionText = transactionText;
    }

    Transaction(int accountNumber, BigDecimal amount) {
        this.accountNumber = accountNumber;
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return accountNumber == that.accountNumber &&
                Objects.equals(objectList, that.objectList) &&
                Objects.equals(amount, that.amount) &&
                Objects.equals(transactionText, that.transactionText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber, objectList, amount, transactionText);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "accountNumber=" + accountNumber +
                ", objectList=" + objectList +
                ", amount=" + amount +
                ", transactionText='" + transactionText + '\'' +
                '}';
    }
}
