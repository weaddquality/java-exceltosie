package se.addq.exceltosie.sie;

import se.addq.exceltosie.file.Dimension;

import static se.addq.exceltosie.utils.StringHelper.CRLF;
import static se.addq.exceltosie.utils.StringHelper.formatAsString;

public class SieDimensionPostData {

    private static final String FORMATTED_ROW = "%s %s %s%s";

    private static final String LABEL = "#DIM";

    public static String getFormattedRow(Dimension dimension) {
        return String.format(FORMATTED_ROW, LABEL, dimension.getDimensionNumber(), formatAsString(dimension.getDimensionName()), CRLF);
    }
}
