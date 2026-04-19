package com.smarttask.controller;

import com.smarttask.dao.TaskDAOImpl;
import com.smarttask.model.Priority;
import com.smarttask.model.Task;
import com.smarttask.model.TaskStatus;
import java.util.List;

public class TaskController {

    private TaskDAOImpl taskDAO = new TaskDAOImpl();

    // Ajouter une tâche
    public void ajouterTache(int userId, String titre, String description,
                             Priority priorite, String deadline) {
        Task task = new Task(0, userId, titre, description,
                priorite, TaskStatus.EN_ATTENTE, deadline);
        taskDAO.insererTask(task);
    }

    // Supprimer une tâche
    public void supprimerTache(int id) {
        taskDAO.supprimerTask(id);
    }

    // Modifier une tâche
    public void modifierTache(int id, String titre, String description,
                              Priority priorite, String deadline) {
        Task task = new Task(id, 0, titre, description,
                priorite, TaskStatus.EN_ATTENTE, deadline);
        taskDAO.modifierTask(task);
    }

    // Marquer comme terminée
    public void marquerTerminee(int id) {
        taskDAO.changerStatut(id, TaskStatus.TERMINEE.name());
    }

    // Marquer en cours
    public void marquerEnCours(int id) {
        taskDAO.changerStatut(id, TaskStatus.EN_COURS.name());
    }

    // Archiver une tâche
    public void archiverTache(int id) {
        taskDAO.archiverTask(id);
    }

    // Récupérer toutes les tâches
    public List<Task> getToutesLesTaches(int userId) {
        return taskDAO.getToutesLesTasks(userId);
    }

    // Récupérer les tâches archivées
    public List<Task> getTachesArchivees(int userId) {
        return taskDAO.getTasksArchivees(userId);
    }
}