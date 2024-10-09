package com.attendance.management.util;

import java.util.Random;

public class UniqueIdGenerator {

    private static final Random random = new Random();

    // Generates a random 8-digit number as a string
    public static String generateUniqueId() {
        int number = 10000000 + random.nextInt(90000000);  // Generates a random number between 10000000 and 99999999
        return String.valueOf(number);
    }
}
