package com.smarttask.controller;

import com.smarttask.dao.UserDAOImpl;
import com.smarttask.model.User;
import com.smarttask.utils.PasswordUtils;
import com.smarttask.utils.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField champUsername;
    @FXML private PasswordField champPassword;
    @FXML private Label labelErreur;

    private UserDAOImpl userDAO = new UserDAOImpl();

    @FXML
    public void seConnecter() {
        String username = champUsername.getText().trim();
        String password = champPassword.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            labelErreur.setText("⚠️ Remplissez tous les champs !");
            return;
        }

        User user = userDAO.getUserByUsername(username);

        if (user == null) {
            labelErreur.setText("❌ Utilisateur introuvable !");
            return;
        }

        if (PasswordUtils.checkPassword(password, user.getPassword())) {
            SessionManager.setCurrentUser(user);
            ouvrirFenetrePrincipale();
        } else {
            labelErreur.setText("❌ Mot de passe incorrect !");
        }
    }

    @FXML
    public void sInscrire() {
        String username = champUsername.getText().trim();
        String password = champPassword.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            labelErreur.setText("⚠️ Remplissez tous les champs !");
            return;
        }

        if (userDAO.getUserByUsername(username) != null) {
            labelErreur.setText("❌ Nom d'utilisateur déjà pris !");
            return;
        }

        String hashedPassword = PasswordUtils.hashPassword(password);
        User newUser = new User(username, hashedPassword);
        boolean succes = userDAO.registerUser(newUser);

        if (succes) {
            labelErreur.setStyle("-fx-text-fill: #2ecc71;");
            labelErreur.setText("✅ Inscription réussie ! Connectez-vous.");
        } else {
            labelErreur.setText("❌ Erreur lors de l'inscription !");
        }
    }

    private void ouvrirFenetrePrincipale() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/smarttask/view/main.fxml"));
            Stage stage = new Stage();
            stage.setTitle("SmartTask — Mes Tâches");
            Scene scene = new Scene(loader.load(), 900, 600);
            scene.getStylesheets().add(
                    getClass().getResource("/com/smarttask/view/styles.css").toExternalForm());
            stage.setScene(scene);
            stage.show();

            // Fermer la fenêtre de login
            Stage loginStage = (Stage) champUsername.getScene().getWindow();
            loginStage.close();

        } catch (Exception e) {
            e.printStackTrace();
            labelErreur.setText("❌ Erreur ouverture application !");
        }
    }
}