package com.smarttask.dao;

import com.smarttask.model.Task;
import java.util.List;

public interface ITaskDAO {
    void insererTask(Task task);
    void supprimerTask(int id);
    void modifierTask(Task task);
    void archiverTask(int id);
    void changerStatut(int id, String statut);
    List<Task> getToutesLesTasks(int userId);
    List<Task> getTasksArchivees(int userId);
}