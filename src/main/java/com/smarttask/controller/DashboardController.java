package com.smarttask.controller;

import com.smarttask.dao.TaskDAOImpl;
import com.smarttask.model.Task;
import com.smarttask.model.TaskStatus;
import com.smarttask.model.Priority;
import com.smarttask.utils.DateUtils;
import com.smarttask.utils.SessionManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import java.util.List;

public class DashboardController {

    @FXML private Label labelTotal;
    @FXML private Label labelTerminees;
    @FXML private Label labelEnRetard;
    @FXML private Label labelEnCours;
    @FXML private PieChart pieStatuts;
    @FXML private BarChart<String, Number> barPriorites;

    private TaskDAOImpl taskDAO = new TaskDAOImpl();

    @FXML
    public void initialize() {
        chargerStatistiques();
    }

    private void chargerStatistiques() {
        int userId = SessionManager.getCurrentUser() != null ?
                SessionManager.getCurrentUser().getId() : 1;

        List<Task> taches = taskDAO.getToutesLesTasks(userId);

        // Compteurs
        long total = taches.size();
        long terminees = taches.stream()
                .filter(t -> t.getStatut() == TaskStatus.TERMINEE).count();
        long enCours = taches.stream()
                .filter(t -> t.getStatut() == TaskStatus.EN_COURS).count();
        long enRetard = taches.stream()
                .filter(t -> t.getDeadline() != null &&
                        !t.getDeadline().isEmpty() &&
                        DateUtils.estEnRetard(t.getDeadline())).count();

        // Afficher les chiffres
        labelTotal.setText(String.valueOf(total));
        labelTerminees.setText(String.valueOf(terminees));
        labelEnCours.setText(String.valueOf(enCours));
        labelEnRetard.setText(String.valueOf(enRetard));

        // Graphique PieChart — répartition par statut
        pieStatuts.setData(FXCollections.observableArrayList(
                new PieChart.Data("En Attente", taches.stream()
                        .filter(t -> t.getStatut() == TaskStatus.EN_ATTENTE).count()),
                new PieChart.Data("En Cours", enCours),
                new PieChart.Data("Terminées", terminees)
        ));

        // Graphique BarChart — répartition par priorité
        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName("Tâches");
        serie.getData().add(new XYChart.Data<>("HAUTE", taches.stream()
                .filter(t -> t.getPriorite() == Priority.HAUTE).count()));
        serie.getData().add(new XYChart.Data<>("MOYENNE", taches.stream()
                .filter(t -> t.getPriorite() == Priority.MOYENNE).count()));
        serie.getData().add(new XYChart.Data<>("BASSE", taches.stream()
                .filter(t -> t.getPriorite() == Priority.BASSE).count()));

        barPriorites.getData().clear();
        barPriorites.getData().add(serie);
    }
}