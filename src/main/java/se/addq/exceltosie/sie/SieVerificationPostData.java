package se.addq.exceltosie.sie;

import se.addq.exceltosie.file.VerificationPost;
import se.addq.exceltosie.utils.StringHelper;

import static se.addq.exceltosie.utils.StringHelper.CRLF;

public class SieVerificationPostData {

    //#VER serie vernr verdatum vertext regdatum (sign)
    //#VER "" "" 20180925 "Bruttopool augusti" 20180920
    private static final String FORMATTED_ROW = "%s %s %s %s %s %s%s";

    private static final String LABEL = "#VER";

    public static String getFormattedRow(VerificationPost verificationPost) {
        if (verificationPost == null) {
            return "";
        }
        return String.format(FORMATTED_ROW,
                LABEL,
                StringHelper.formatAsString(VerificationPost.getSERIE()),
                StringHelper.formatAsString(VerificationPost.getVerificationNumber()),
                verificationPost.getVerificationDate(),
                StringHelper.formatAsString(verificationPost.getVerificationText()),
                verificationPost.getRegistrationDate(),
                CRLF);
    }
}
