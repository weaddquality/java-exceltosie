package se.addq.exceltosie.file;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for data model (dao) of data needed to create an .si file. *
 */
public class FileData {

    private static final int FLAG = 0;

    private static final String FORMAT = "PC8";

    private static final String SIE_FILE_TYPE = "4";

    private static final String ACCOUNT_FILE_TYPE = "EUBAS97";

    private String generationDate;

    private String organisationNumber;

    private String companyName;

    private List<Account> accountList = new ArrayList<>();

    private List<Dimension> dimensionList = new ArrayList<>();

    private List<SieObject> sieObjectList = new ArrayList<>();

    private VerificationPost verificationPost;

    static int getFLAG() {
        return FLAG;
    }

    static String getFORMAT() {
        return FORMAT;
    }

    static String getSieFileType() {
        return SIE_FILE_TYPE;
    }

    static String getAccountFileType() {
        return ACCOUNT_FILE_TYPE;
    }

    String getGenerationDate() {
        return generationDate;
    }

    void setGenerationDate(String generationDate) {
        this.generationDate = generationDate;
    }

    String getOrganisationNumber() {
        return organisationNumber;
    }

    void setOrganisationNumber(String organisationNumber) {
        this.organisationNumber = organisationNumber;
    }

    public String getCompanyName() {
        return companyName;
    }

    void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    List<Account> getAccountList() {
        return accountList;
    }

    void setAccountList(List<Account> accountList) {
        this.accountList = accountList;
    }

    List<Dimension> getDimensionList() {
        return dimensionList;
    }

    void setDimensionList(List<Dimension> dimensionList) {
        this.dimensionList = dimensionList;
    }

    List<SieObject> getSieObjectList() {
        return sieObjectList;
    }

    void setSieObjectList(List<SieObject> sieObjectList) {
        this.sieObjectList = sieObjectList;
    }

    VerificationPost getVerificationPost() {
        return verificationPost;
    }

    void setVerificationPost(VerificationPost verificationPost) {
        this.verificationPost = verificationPost;
    }
}
