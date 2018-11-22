package se.addq.exceltosie.file;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.addq.exceltosie.utils.FileHandler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class FileGeneratorTest {

    @InjectMocks
    private FileGenerator fileGenerator;

    @Mock
    private FileHandler fileHandler;

    @Test
    void shouldReturnFullFileNameFromEmptyFileData() {
        String filePath = "C:/test/katalog";
        String filename = fileGenerator.createFile(filePath, new FileData());
        assertThat(filename).startsWith(filePath);
    }


    @Test
    void shouldCreateCorrectFileFromSetFileData() {
        String filePath = "testfil";
        String filename = fileGenerator.createFile(filePath, getFileData());
        assertThat(filename).startsWith(filePath);
    }

    private FileData getFileData() {
        FileData fileData = new FileData();
        fileData.setCompanyName("Company X AB");
        fileData.setGenerationDate("20180920");
        fileData.setOrganisationNumber("556762-4605");
        List<Account> accountList = new ArrayList<>();
        accountList.add(new Account(2801, "Bruttopoolsskuld till personal"));
        accountList.add(new Account(2802, "Bruttopoolsskuld till f-ledning"));
        accountList.add(new Account(7020, "Lön tjänstemän"));
        accountList.add(new Account(7010, "Bruttopoolsintäkt + grundlön inkl.soc.avgLöner företagsledare"));
        accountList.add(new Account(7029, "Förändring bruttopool f-ledare"));
        accountList.add(new Account(7018, "Förändring bruttopool personal"));
        fileData.setAccountList(accountList);
        List<Dimension> dimensionList = new ArrayList<>();
        dimensionList.add(new Dimension(1, "Kostnadsställe"));
        dimensionList.add(new Dimension(6, "Projekt"));
        fileData.setDimensionList(dimensionList);
        List<SieObject> sieObjectList = new ArrayList<>();
        sieObjectList.add(new SieObject(1, "1", "TeamData 1"));
        sieObjectList.add(new SieObject(1, "2", "TeamData 2"));
        sieObjectList.add(new SieObject(1, "3", "TeamData 3"));
        sieObjectList.add(new SieObject(6, "1", "1 Anna Andersson"));
        sieObjectList.add(new SieObject(6, "2", "2 Kalle Karlsson"));
        sieObjectList.add(new SieObject(6, "3", "3 Pelle Pärson"));
        sieObjectList.add(new SieObject(6, "4", "4 Lisa Larsson"));
        sieObjectList.add(new SieObject(6, "84", "900 Förändring bruttopool"));
        sieObjectList.add(new SieObject(6, "30", "Nollan"));
        fileData.setSieObjectList(sieObjectList);

        VerificationPost verificationPost = new VerificationPost();
        verificationPost.setVerificationDate("20180925");
        verificationPost.setVerificationText("Bruttopool augusti");
        verificationPost.setRegistrationDate("20180920");
        List<Transaction> transactionList = new ArrayList<>();
        Map<Integer, String> objectList = new HashMap<>();
        transactionList.add(new Transaction(2802, objectList, new BigDecimal("-3302.00"), ""));
        Map<Integer, String> objectList1 = new HashMap<>();
        transactionList.add(new Transaction(2801, objectList1, new BigDecimal("-22400.00"), ""));
        Map<Integer, String> objectList2 = new HashMap<>();
        objectList2.put(6, "4");
        transactionList.add(new Transaction(7020, objectList2, new BigDecimal("-91439.00"), ""));
        Map<Integer, String> objectList3 = new HashMap<>();
        objectList3.put(1, "3");
        objectList3.put(6, "30");
        transactionList.add(new Transaction(7020, objectList3, new BigDecimal("91439.00"), ""));
        verificationPost.setTransactionList(transactionList);
        fileData.setVerificationPost(verificationPost);
        return fileData;
    }


}