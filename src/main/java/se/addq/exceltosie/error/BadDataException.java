package se.addq.exceltosie.error;

public class BadDataException extends ExcelToSieException {

    public BadDataException(String errorMessage) {
        super(errorMessage);
    }

    public BadDataException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
