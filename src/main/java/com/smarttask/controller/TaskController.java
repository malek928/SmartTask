package com.smarttask.controller;

import com.smarttask.dao.TaskDAOImpl;
import com.smarttask.model.Priority;
import com.smarttask.model.Task;
import com.smarttask.model.TaskStatus;
import com.smarttask.utils.CSVExporter;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

public class TaskController {

    @FXML private TableView<Task> tableTaches;
    @FXML private TableColumn<Task, String> colTitre;
    @FXML private TableColumn<Task, String> colDescription;
    @FXML private TableColumn<Task, String> colPriorite;
    @FXML private TableColumn<Task, String> colStatut;
    @FXML private TableColumn<Task, String> colDeadline;
    @FXML private Label labelStatut;

    private TaskDAOImpl taskDAO = new TaskDAOImpl();
    private int userId = 1;

    @FXML
    public void initialize() {
        colTitre.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getTitre()));
        colDescription.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getDescription()));
        colPriorite.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getPriorite().name()));
        colStatut.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getStatut().name()));
        colDeadline.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getDeadline()));
        chargerTaches();
    }

    private void chargerTaches() {
        List<Task> liste = taskDAO.getToutesLesTasks(userId);
        ObservableList<Task> observableListe = FXCollections.observableArrayList(liste);
        tableTaches.setItems(observableListe);
        labelStatut.setText(liste.size() + " tâche(s) chargée(s)");
    }

    @FXML
    public void ouvrirFormulaireAjout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/smarttask/view/task_form.fxml"));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Nouvelle tâche");
            stage.setScene(new Scene(loader.load()));
            TaskFormController formController = loader.getController();
            formController.setModeAjout(userId);
            stage.showAndWait();
            chargerTaches();
        } catch (Exception e) {
            afficherErreur("Impossible d'ouvrir le formulaire : " + e.getMessage());
        }
    }

    @FXML
    public void ouvrirFormulaireModifier() {
        Task tacheSelectionnee = tableTaches.getSelectionModel().getSelectedItem();
        if (tacheSelectionnee == null) {
            afficherNotification("Sélectionne d'abord une tâche dans le tableau !");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/smarttask/view/task_form.fxml"));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Modifier la tâche");
            stage.setScene(new Scene(loader.load()));
            TaskFormController formController = loader.getController();
            formController.setModeModification(tacheSelectionnee);
            stage.showAndWait();
            chargerTaches();
        } catch (Exception e) {
            afficherErreur("Impossible d'ouvrir le formulaire : " + e.getMessage());
        }
    }

    @FXML
    public void supprimerTache() {
        Task tacheSelectionnee = tableTaches.getSelectionModel().getSelectedItem();
        if (tacheSelectionnee == null) {
            afficherNotification("Sélectionne d'abord une tâche !");
            return;
        }
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setHeaderText("Supprimer la tâche ?");
        confirmation.setContentText("Tu veux vraiment supprimer : " + tacheSelectionnee.getTitre() + " ?");
        confirmation.showAndWait().ifPresent(reponse -> {
            if (reponse == ButtonType.OK) {
                taskDAO.supprimerTask(tacheSelectionnee.getId());
                chargerTaches();
                afficherSucces("Tâche supprimée avec succès !");
            }
        });
    }

    @FXML
    public void marquerTerminee() {
        Task tacheSelectionnee = tableTaches.getSelectionModel().getSelectedItem();
        if (tacheSelectionnee == null) {
            afficherNotification("Sélectionne d'abord une tâche !");
            return;
        }
        taskDAO.changerStatut(tacheSelectionnee.getId(), TaskStatus.TERMINEE.name());
        chargerTaches();
        afficherSucces("Tâche marquée comme terminée ✅");
    }

    @FXML
    public void archiverTache() {
        Task tacheSelectionnee = tableTaches.getSelectionModel().getSelectedItem();
        if (tacheSelectionnee == null) {
            afficherNotification("Sélectionne d'abord une tâche !");
            return;
        }
        taskDAO.archiverTask(tacheSelectionnee.getId());
        chargerTaches();
        afficherSucces("Tâche archivée 📦");
    }

    @FXML
    public void exporterCSV() {
        List<Task> liste = taskDAO.getToutesLesTasks(userId);
        CSVExporter.exporter(liste, "taches_export.csv");
        afficherSucces("Fichier exporté : taches_export.csv 📄");
    }

    @FXML
    public void afficherStats() {
        List<Task> liste = taskDAO.getToutesLesTasks(userId);
        long terminees = liste.stream().filter(t -> t.getStatut() == TaskStatus.TERMINEE).count();
        long enCours = liste.stream().filter(t -> t.getStatut() == TaskStatus.EN_COURS).count();
        long enAttente = liste.stream().filter(t -> t.getStatut() == TaskStatus.EN_ATTENTE).count();
        long urgentes = liste.stream().filter(t -> t.getPriorite() == Priority.HAUTE).count();
        Alert stats = new Alert(Alert.AlertType.INFORMATION);
        stats.setTitle("Statistiques");
        stats.setHeaderText("📊 Résumé de tes tâches");
        stats.setContentText(
                "Total : " + liste.size() + " tâche(s)\n" +
                        "✅ Terminées : " + terminees + "\n" +
                        "🔄 En cours : " + enCours + "\n" +
                        "⏳ En attente : " + enAttente + "\n" +
                        "🔴 Urgentes (priorité haute) : " + urgentes
        );
        stats.showAndWait();
    }

    private void afficherNotification(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void afficherSucces(String message) {
        labelStatut.setText(message);
        labelStatut.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
    }

    private void afficherErreur(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Une erreur s'est produite");
        alert.setContentText(message);
        alert.showAndWait();
    }
}