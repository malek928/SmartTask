package com.smarttask.controller;

import com.smarttask.dao.TaskDAOImpl;
import com.smarttask.model.Task;
import com.smarttask.utils.SessionManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.List;

public class HistoryController {

    @FXML private TableView<Task> tableArchives;
    @FXML private TableColumn<Task, String> colTitre;
    @FXML private TableColumn<Task, String> colDescription;
    @FXML private TableColumn<Task, String> colPriorite;
    @FXML private TableColumn<Task, String> colStatut;
    @FXML private TableColumn<Task, String> colDeadline;
    @FXML private Label labelStatut;

    private TaskDAOImpl taskDAO = new TaskDAOImpl();
    private ObservableList<Task> listeArchives = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        configurerColonnes();
        chargerArchives();
    }

    private void configurerColonnes() {
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
        tableArchives.setItems(listeArchives);
    }

    private void chargerArchives() {
        listeArchives.clear();
        int userId = SessionManager.getCurrentUser() != null ?
                SessionManager.getCurrentUser().getId() : 1;
        List<Task> archives = taskDAO.getTasksArchivees(userId);
        listeArchives.addAll(archives);
        labelStatut.setText(archives.size() + " tâche(s) archivée(s)");
    }

    @FXML
    public void restaurerTache() {
        Task selected = tableArchives.getSelectionModel().getSelectedItem();
        if (selected == null) {
            labelStatut.setText("⚠️ Sélectionnez une tâche à restaurer !");
            return;
        }
        // Restaurer = désarchiver
        String sql_restore = "UPDATE tasks SET archive = false WHERE id = ?";
        try (java.sql.Connection conn = com.smarttask.utils.DatabaseConnection.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql_restore)) {
            ps.setInt(1, selected.getId());
            ps.executeUpdate();
            chargerArchives();
            labelStatut.setText("✅ Tâche restaurée avec succès !");
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            labelStatut.setText("❌ Erreur lors de la restauration !");
        }
    }

    @FXML
    public void supprimerDefinitivement() {
        Task selected = tableArchives.getSelectionModel().getSelectedItem();
        if (selected == null) {
            labelStatut.setText("⚠️ Sélectionnez une tâche à supprimer !");
            return;
        }
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Suppression définitive");
        confirmation.setHeaderText("⚠️ Cette action est irréversible !");
        confirmation.setContentText("Supprimer définitivement : " + selected.getTitre() + " ?");
        confirmation.showAndWait().ifPresent(reponse -> {
            if (reponse == ButtonType.OK) {
                taskDAO.supprimerTask(selected.getId());
                chargerArchives();
                labelStatut.setText("🗑️ Tâche supprimée définitivement !");
            }
        });
    }
}