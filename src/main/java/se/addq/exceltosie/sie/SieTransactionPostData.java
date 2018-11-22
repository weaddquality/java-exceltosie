package se.addq.exceltosie.sie;

import se.addq.exceltosie.file.Transaction;
import se.addq.exceltosie.utils.StringHelper;

import static se.addq.exceltosie.utils.StringHelper.CRLF;

public class SieTransactionPostData {

    //#TRANS kontonr {objektlista} belopp transdat transtext kvantitet sign
    //#TRANS 7020 { 6 "4"} -91439.00
    private static final String FORMATTED_ROW = "%s%s %s %s %s%s";

    private static final String LABEL = "#TRANS";
    private static final String FOUR_WHITESPACE_CHARS = "    ";

    public static String getFormattedRow(Transaction transaction) {
        if (transaction == null) {
            return "";
        }
        return String.format(FORMATTED_ROW,
                FOUR_WHITESPACE_CHARS,
                LABEL,
                transaction.getAccountNumber(),
                getObjectListAsString(transaction),
                transaction.getAmount(),
                CRLF);
    }

    private static String getObjectListAsString(Transaction transaction) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        if (transaction.getObjectList() != null) {
            for (Integer key : transaction.getObjectList().keySet()) {
                String value = transaction.getObjectList().get(key);
                sb.append(" ").append(key).append(" ").append(StringHelper.formatAsString(value));
            }
        }
        sb.append("}");
        return sb.toString();
    }
}
