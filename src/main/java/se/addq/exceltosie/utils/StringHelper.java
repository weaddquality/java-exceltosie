package se.addq.exceltosie.utils;

public class StringHelper {

    private final static char CR = (char) 0x0D;
    private final static char LF = (char) 0x0A;

    public final static String CRLF = "" + CR + LF;

    public static String formatAsString(String value) {
        return getAsString(value);
    }


    public static String formatAsStringIfContainsSpace(String value) {
        if (value.contains(" ")) {
            return getAsString(value);
        }
        return value;
    }

    private static String getAsString(String value) {
        return "\"" + value + "\"";
    }


}
