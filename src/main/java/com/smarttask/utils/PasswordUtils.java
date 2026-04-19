package com.smarttask.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {

    // 1. This method takes a normal password (like "12345") and scrambles it into a secure hash.
    // We will use this when a new user REGISTERS.
    public static String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    // 2. This method checks if the password the user typed matches the scrambled one in the database.
    // We will use this when a user tries to LOGIN.
    public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }
}