package com.smarttask.utils;

// This import line tells Java where to find the User file you just made!
import com.smarttask.model.User;

public class SessionManager {

    private static User currentUser = null;

    // ROLE 1: Log the user in (Notice the capital 'U' in User)
    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    // ROLE 2: Check who is logged in
    public static User getCurrentUser() {
        return currentUser;
    }

    // ROLE 3: Log the user out
    public static void clearSession() {
        currentUser = null;
    }
}