package se.addq.exceltosie.sie;

import se.addq.exceltosie.file.SieObject;

import static se.addq.exceltosie.utils.StringHelper.CRLF;
import static se.addq.exceltosie.utils.StringHelper.formatAsString;

public class SieObjectPostData {

    //#OBJEKT 1 "1" "TeamData 1"
    private static final String FORMATTED_ROW = "%s %s %s %s%s";

    private static final String LABEL = "#OBJEKT";

    public static String getFormattedRow(SieObject sieObject) {
        return String.format(FORMATTED_ROW,
                LABEL,
                sieObject.getDimensionNumber(),
                formatAsString(sieObject.getObjectNumber()),
                formatAsString(sieObject.getObjectName()),
                CRLF);
    }
}
