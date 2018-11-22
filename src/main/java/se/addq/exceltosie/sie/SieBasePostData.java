package se.addq.exceltosie.sie;

import se.addq.exceltosie.utils.StringHelper;

import static se.addq.exceltosie.utils.StringHelper.CRLF;

public class SieBasePostData {

    private static final String FORMATTED_ROW = "%s %s%s";

    public static String getFormattedRow(String label, String data) {
        if (data == null) {
            return "";
        }
        return String.format(FORMATTED_ROW, label, StringHelper.formatAsStringIfContainsSpace(data), CRLF);
    }

    public static String getFormattedRow(String label, int data) {
        return String.format(FORMATTED_ROW, label, data, CRLF);
    }
}
