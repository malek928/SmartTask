package com.smarttask;

import com.smarttask.controller.LoginController;
import com.smarttask.utils.SessionManager;

public class Main {
    public static void main(String[] args) {

        // 1. Bring in our Boss Controller
        LoginController auth = new LoginController();

        System.out.println("=== TEST 1: REGISTER A NEW USER ===");
        // Let's pretend a user typed this into a registration screen
        auth.register("amine_dz", "superSecret123");

        System.out.println("\n=== TEST 2: LOGIN WITH WRONG PASSWORD ===");
        // Let's pretend they made a typo
        auth.login("amine_dz", "wrongPassword");

        System.out.println("\n=== TEST 3: LOGIN WITH CORRECT PASSWORD ===");
        // Let's pretend they typed it perfectly
        auth.login("amine_dz", "superSecret123");

        System.out.println("\n=== TEST 4: CHECK THE SESSION MANAGER ===");
        // Did the app remember who logged in?
        if (SessionManager.getCurrentUser() != null) {
            System.out.println("Success! The app remembers that user ID "
                    + SessionManager.getCurrentUser().getId()
                    + " is logged in.");
        } else {
            System.out.println("Uh oh, the session manager is empty.");
        }
    }
}
