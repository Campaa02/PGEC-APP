package com.PCA.Others;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateValidator {

    public static boolean isDateValid(String date) {
        try {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            LocalDate inputDate = LocalDate.parse(date, formatter);

            LocalDate currentDate = LocalDate.now();

            return !inputDate.isBefore(currentDate);
        } catch (DateTimeParseException e) {

            return false;
        }
    }
}

