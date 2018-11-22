package se.addq.exceltosie.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.addq.exceltosie.sie.*;
import se.addq.exceltosie.utils.FileHandler;
import se.addq.exceltosie.utils.StringHelper;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Class for creating and formatting .si file correctly according to SIE standard.
 * Input is internal FileData which contains correct line data without formatting.
 */
public class FileGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String BASE_FILE_NAME_BP_SI = "BP_";
    private static final String IBM_437_PC_8_bit_extended_ASCII = "ibm437";

    private String fileName;
    private FileHandler fileHandler;

    public FileGenerator(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }

    public String createFile(String filePath, FileData fileData) {
        setFileName(filePath);
        CreateBaseData(fileData);
        CreateAccountData(fileData.getAccountList());
        CreateDimensionData(fileData.getDimensionList());
        CreateSieObjectData(fileData.getSieObjectList());
        CreateTransactionData(fileData.getVerificationPost());
        return this.fileName;
    }

    private void setFileName(String parent) {
        fileName = parent + File.separator + BASE_FILE_NAME_BP_SI + System.currentTimeMillis() + ".si";
        LOG.info("Set file full path to {}", fileName);
    }


    private void CreateBaseData(FileData fileData) {
        createRowInFile(SieBasePostData.getFormattedRow("#FLAGGA", FileData.getFLAG()));
        createRowInFile(SieBasePostData.getFormattedRow("#FORMAT", FileData.getFORMAT()));
        createRowInFile(SieBasePostData.getFormattedRow("#GEN", fileData.getGenerationDate()));
        createRowInFile(SieBasePostData.getFormattedRow("#SIETYP", FileData.getSieFileType()));
        createRowInFile(SieBasePostData.getFormattedRow("#ORGNR", fileData.getOrganisationNumber()));
        createRowInFile(SieBasePostData.getFormattedRow("#FNAMN", fileData.getCompanyName()));
        createRowInFile(SieBasePostData.getFormattedRow("#KPTYP", FileData.getAccountFileType()));
    }

    private void CreateDimensionData(List<Dimension> dimensionList) {
        for (Dimension dimension : dimensionList) {
            createRowInFile(SieDimensionPostData.getFormattedRow(dimension));
        }
    }

    private void CreateAccountData(List<Account> accountList) {
        for (Account account : accountList) {
            createRowInFile(SieAccountPostData.getFormattedRow(account));
        }
    }

    private void CreateSieObjectData(List<SieObject> dimensionList) {
        for (SieObject sieObject : dimensionList) {
            createRowInFile(SieObjectPostData.getFormattedRow(sieObject));
        }
    }

    private void CreateTransactionData(VerificationPost verificationPost) {
        if (verificationPost == null) {
            return;
        }
        createMultipleRowsInFile(StringHelper.CRLF, 3);
        createRowInFile(SieVerificationPostData.getFormattedRow(verificationPost));
        createRowInFile("{" + StringHelper.CRLF);
        for (Transaction transaction : verificationPost.getTransactionList()) {
            createRowInFile(SieTransactionPostData.getFormattedRow(transaction));
        }
        createRowInFile("}" + StringHelper.CRLF);
    }

    private void createRowInFile(String lineToAdd) {
        addLineToFile(lineToAdd);
    }

    private void createMultipleRowsInFile(String lineToAdd, int numberOfRows) {
        for (int i = 0; i < numberOfRows; ++i) {
            addLineToFile(lineToAdd);
        }
    }

    private void addLineToFile(String lineToAdd) {
        Charset charset = Charset.forName(IBM_437_PC_8_bit_extended_ASCII);
        fileHandler.addLineToFile(fileName, lineToAdd, charset);
    }


}
