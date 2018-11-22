package se.addq.exceltosie.file;

import java.util.ArrayList;
import java.util.List;

public class VerificationPost {

    private static final String SERIE = "";

    private static final String VERIFICATION_NUMBER = "";

    private String verificationDate = "";

    private String verificationText = "";

    private String registrationDate = "";

    private List<Transaction> transactionList = new ArrayList<>();

    public static String getSERIE() {
        return SERIE;
    }

    public static String getVerificationNumber() {
        return VERIFICATION_NUMBER;
    }

    public String getVerificationDate() {
        return verificationDate;
    }

    public void setVerificationDate(String verificationDate) {
        this.verificationDate = verificationDate;
    }

    public String getVerificationText() {
        return verificationText;
    }

    public void setVerificationText(String verificationText) {
        this.verificationText = verificationText;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    List<Transaction> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }


}
