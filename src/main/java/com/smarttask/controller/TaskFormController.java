package com.smarttask.controller;

import com.smarttask.dao.TaskDAOImpl;
import com.smarttask.model.Priority;
import com.smarttask.model.Task;
import com.smarttask.model.TaskStatus;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;

public class TaskFormController {

    @FXML private TextField champTitre;
    @FXML private TextArea champDescription;
    @FXML private ComboBox<String> comboPriorite;
    @FXML private ComboBox<String> comboStatut;
    @FXML private DatePicker dateDeadline;

    private TaskDAOImpl taskDAO = new TaskDAOImpl();
    private Task tacheAModifier = null; // null = mode ajout, sinon = mode modification
    private int userId;

    // Appelée automatiquement quand le formulaire s'ouvre
    @FXML
    public void initialize() {
        // Remplir les listes déroulantes
        comboPriorite.setItems(FXCollections.observableArrayList("HAUTE", "MOYENNE", "BASSE"));
        comboStatut.setItems(FXCollections.observableArrayList("EN_ATTENTE", "EN_COURS", "TERMINEE"));

        // Valeurs par défaut
        comboPriorite.setValue("MOYENNE");
        comboStatut.setValue("EN_ATTENTE");
    }

    // Appelée depuis TaskController quand on clique "Ajouter"
    public void setModeAjout(int userId) {
        this.userId = userId;
        this.tacheAModifier = null;
    }

    // Appelée depuis TaskController quand on clique "Modifier"
    // Elle remplit le formulaire avec les données de la tâche existante
    public void setModeModification(Task tache) {
        this.tacheAModifier = tache;
        this.userId = tache.getUserId();

        // Remplir les champs avec les données existantes
        champTitre.setText(tache.getTitre());
        champDescription.setText(tache.getDescription());
        comboPriorite.setValue(tache.getPriorite().name());
        comboStatut.setValue(tache.getStatut().name());

        if (tache.getDeadline() != null && !tache.getDeadline().isEmpty()) {
            dateDeadline.setValue(LocalDate.parse(tache.getDeadline()));
        }
    }

    // Bouton "Sauvegarder"
    @FXML
    public void sauvegarder() {
        // Vérification : le titre est obligatoire
        if (champTitre.getText().trim().isEmpty()) {
            afficherErreur("Le titre est obligatoire !");
            return;
        }
        if (comboPriorite.getValue() == null) {
            afficherErreur("Choisis une priorité !");
            return;
        }

        // Récupérer la deadline (peut être vide)
        String deadline = "";
        if (dateDeadline.getValue() != null) {
            deadline = dateDeadline.getValue().toString();
        }

        if (tacheAModifier == null) {
            // MODE AJOUT — créer une nouvelle tâche
            Task nouvelleTache = new Task(
                    0, // l'id sera généré automatiquement par MySQL
                    userId,
                    champTitre.getText().trim(),
                    champDescription.getText().trim(),
                    Priority.valueOf(comboPriorite.getValue()),
                    TaskStatus.valueOf(comboStatut.getValue()),
                    deadline
            );
            taskDAO.insererTask(nouvelleTache);
            afficherSucces("Tâche ajoutée avec succès !");

        } else {
            // MODE MODIFICATION — mettre à jour la tâche existante
            tacheAModifier.setTitre(champTitre.getText().trim());
            tacheAModifier.setDescription(champDescription.getText().trim());
            tacheAModifier.setPriorite(Priority.valueOf(comboPriorite.getValue()));
            tacheAModifier.setStatut(TaskStatus.valueOf(comboStatut.getValue()));
            tacheAModifier.setDeadline(deadline);
            taskDAO.modifierTask(tacheAModifier);
            afficherSucces("Tâche modifiée avec succès !");
        }

        fermerFenetre();
    }

    // Bouton "Annuler" → ferme le formulaire sans rien faire
    @FXML
    public void annuler() {
        fermerFenetre();
    }

    // Ferme la fenêtre du formulaire
    private void fermerFenetre() {
        Stage stage = (Stage) champTitre.getScene().getWindow();
        stage.close();
    }

    // Pop-up d'erreur
    private void afficherErreur(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Pop-up de succès
    private void afficherSucces(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}