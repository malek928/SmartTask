package com.smarttask.controller;

import com.smarttask.dao.UserDAOImpl;
import com.smarttask.model.User;
import com.smarttask.utils.PasswordUtils;
import com.smarttask.utils.SessionManager;

public class LoginController {

    // We bring in the DAO so the Controller can talk to the database
    private UserDAOImpl userDAO = new UserDAOImpl();

    // ROLE 1: Handle User Registration
    public boolean register(String username, String password) {

        // 1. Check if the username is already taken
        if (userDAO.getUserByUsername(username) != null) {
            System.out.println("Registration failed: Username already exists!");
            return false;
        }

        // 2. Scramble (hash) the password using our utility
        String hashedPassword = PasswordUtils.hashPassword(password);

        // 3. Create a new User object with the hashed password
        User newUser = new User(username, hashedPassword);

        // 4. Tell the DAO to save the user to the database
        boolean isRegistered = userDAO.registerUser(newUser);
        if (isRegistered) {
            System.out.println("Registration successful for user: " + username);
        }
        return isRegistered;
    }

    // ROLE 2: Handle User Login
    public boolean login(String username, String password) {

        // 1. Ask DAO to find the user in the database
        User user = userDAO.getUserByUsername(username);

        // 2. If the user doesn't exist at all
        if (user == null) {
            System.out.println("Login failed: User not found!");
            return false;
        }

        // 3. Check if the typed password matches the hashed password in the DB
        if (PasswordUtils.checkPassword(password, user.getPassword())) {

            // 4. Success! Save them in the SessionManager so the app remembers them
            SessionManager.setCurrentUser(user);
            System.out.println("Login successful! Welcome, " + username);
            return true;

        } else {
            System.out.println("Login failed: Incorrect password!");
            return false;
        }
    }
}