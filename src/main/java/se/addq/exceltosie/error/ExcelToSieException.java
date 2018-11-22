package se.addq.exceltosie.error;

abstract class ExcelToSieException extends Exception {

    ExcelToSieException(String errorMessage) {
        super(errorMessage);
    }

    ExcelToSieException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }


}
