package se.addq.exceltosie.utils;

import se.addq.exceltosie.error.BadDataException;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class DateUtil {

    static String getLocalDateFormattedAsSieFileDate(LocalDate localDate) {
        DateTimeFormatter formatter = DateTimeFormatter.BASIC_ISO_DATE;
        return localDate.format(formatter);
    }

    public static String getYearFromLocalDate(LocalDate localDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
        return localDate.format(formatter);
    }

    public static String getDateAsStringForAccountingMonth(String swedishMonth) throws BadDataException {
        if(swedishMonth == null) {
           throw new BadDataException("Ingen bokföringsmånad hittad!");
        }
        LocalDate dateToConvert = LocalDate.now(ZoneId.systemDefault()).withMonth(getMonthNumberFromSwedishMonthName(swedishMonth));
        LocalDate convertedDate = dateToConvert.withDayOfMonth(
                dateToConvert.getMonth().length(dateToConvert.isLeapYear()));
        return getLocalDateFormattedAsSieFileDate(convertedDate);
    }


    private static int getMonthNumberFromSwedishMonthName(String name) {
        return getMonthMap().get(name.toLowerCase().trim());
    }

    private static Map<String, Integer> getMonthMap() {
        Map<String, Integer> map = new HashMap<>();
        map.put("januari", 1);
        map.put("februari", 2);
        map.put("mars", 3);
        map.put("april", 4);
        map.put("maj", 5);
        map.put("juni", 6);
        map.put("juli", 7);
        map.put("augusti", 8);
        map.put("september", 9);
        map.put("oktober", 10);
        map.put("november", 11);
        map.put("december", 12);
        return map;
    }
}
