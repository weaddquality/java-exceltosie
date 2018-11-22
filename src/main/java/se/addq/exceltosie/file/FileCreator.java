package se.addq.exceltosie.file;

import se.addq.exceltosie.companydata.AccountingDataToCompanyDataMapper;
import se.addq.exceltosie.companydata.CompanyData;
import se.addq.exceltosie.error.BadDataException;
import se.addq.exceltosie.excel.ExcelFileReader;
import se.addq.exceltosie.excel.ExcelSheetData;
import se.addq.exceltosie.utils.DateUtil;

import java.io.File;
import java.time.LocalDate;

/**
 * Main class for reading and creating a SIE-file from excel,
 */
public class FileCreator {

    private static final String TAB_NAME_FOR_SI_FILE_SPECIFIC_DATA = "SI-fildata";

    private ExcelFileReader excelFileReader;

    private CompanyDataToFileDataMapper companyDataToFileDataMapper;

    private AccountingDataToCompanyDataMapper accountingDataToCompanyDataMapper;

    public FileCreator(ExcelFileReader excelFileReader, CompanyDataToFileDataMapper companyDataToFileDataMapper, AccountingDataToCompanyDataMapper accountingDataToCompanyDataMapper) {
        this.excelFileReader = excelFileReader;
        this.companyDataToFileDataMapper = companyDataToFileDataMapper;
        this.accountingDataToCompanyDataMapper = accountingDataToCompanyDataMapper;
    }

    public FileData getAccountingDataFromExcel(File file) throws BadDataException {
        ExcelSheetData companyDataSheet = excelFileReader.readAllData(file, TAB_NAME_FOR_SI_FILE_SPECIFIC_DATA);

        if (companyDataSheet == null || companyDataSheet.getExcelSheetName() == null) {
            return new FileData();
        }
        ExcelSheetData accountingDataSheet = excelFileReader.readAllData(file, getTabNameForAccountingData(file));
        if (accountingDataSheet == null) {
            return new FileData();
        }
        CompanyData companyData = accountingDataToCompanyDataMapper.getMappedDataFromExcel(accountingDataSheet, companyDataSheet);
        return companyDataToFileDataMapper.getMappedDataFromCompanyData(companyData);
    }

    private String getTabNameForAccountingData(File file) throws BadDataException {
        String thisYear = DateUtil.getYearFromLocalDate(LocalDate.now());
        if (excelFileReader.isExistingTab(thisYear, file)) {
            return thisYear;
        }
        String previousYear = String.valueOf(Integer.valueOf(thisYear) - 1);
        if (excelFileReader.isExistingTab(previousYear, file)) {
            return previousYear;
        }
        throw new BadDataException("Kan inte hitta tab med år " + previousYear + " eller " + thisYear);
    }


}






