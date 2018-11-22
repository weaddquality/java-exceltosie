package se.addq.exceltosie.utils;

import org.junit.jupiter.api.Test;
import se.addq.exceltosie.error.BadDataException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

class DateUtilTest {

    @Test
    void getLocalDateFormattedAsSieFileDate() {
        String date = DateUtil.getLocalDateFormattedAsSieFileDate(LocalDate.now());
        assertThat(date.length()).isEqualTo(8L);
    }

    @Test
    void getYearFromLocalDate() {
        String year = DateUtil.getYearFromLocalDate(LocalDate.now());
        assertThat(year.length()).isEqualTo(4L);
        assertThat(year.startsWith("20")).isTrue();
    }

    @Test
    void getLocalDateForLastDayOfLastMonth() throws BadDataException {
        String date = DateUtil.getDateAsStringForAccountingMonth("Augusti");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        assertThat(LocalDate.parse(date, dateTimeFormatter).getMonth().getValue()).isEqualTo(8);
    }
}