package se.addq.exceltosie.utils;


import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.assertj.core.api.Assertions.assertThat;

class StringHelperTest {

    @Test
    void shouldGetFormattedAsStringWhenWhitespace() {
        String expectedResult = "\"hej kalle\"";
        String row = StringHelper.formatAsStringIfContainsSpace("hej kalle");
        assertThat(row).isEqualTo(expectedResult);
    }

    @Test
    void shouldNotGetFormattedAsStringWhenNoWhitespace() {
        String expectedResult = "hejkalle";
        String row = StringHelper.formatAsStringIfContainsSpace("hejkalle");
        assertThat(row).isEqualTo(expectedResult);
    }

    @Test
    void test() {
        String value = "-1234";
        BigDecimal amount = new BigDecimal(value).setScale(2, RoundingMode.HALF_EVEN);
        System.out.println(amount.toString());
    }

    @Test
    void shouldGetFormattedAsString() {
        String expectedResult = "\"hejkalle\"";
        String row = StringHelper.formatAsString("hejkalle");
        assertThat(row).isEqualTo(expectedResult);
    }

}