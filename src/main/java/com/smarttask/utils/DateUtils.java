package com.smarttask.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    public static String formater(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date.format(formatter);
    }

    public static boolean estEnRetard(String deadline) {
        LocalDate dateLimit = LocalDate.parse(deadline);
        return LocalDate.now().isAfter(dateLimit);
    }

    public static boolean estBientotDue(String deadline) {
        LocalDate dateLimit = LocalDate.parse(deadline);
        return LocalDate.now().plusDays(1).isAfter(dateLimit)
                && !estEnRetard(deadline);
    }
}