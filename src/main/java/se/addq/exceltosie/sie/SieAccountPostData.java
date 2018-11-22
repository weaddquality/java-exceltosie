package se.addq.exceltosie.sie;

import se.addq.exceltosie.file.Account;
import se.addq.exceltosie.utils.StringHelper;

public class SieAccountPostData {

    //#KONTO 2801 "Bruttopoolsskuld till personal"
    private static final String FORMATTED_ROW = "%s %s %s%s";

    private static final String LABEL = "#KONTO";

    public static String getFormattedRow(Account account) {
        return String.format(FORMATTED_ROW, LABEL, account.getAccountNumber(), StringHelper.formatAsString(account.getAccountName()), StringHelper.CRLF);
    }
}
