package com.smarttask.dao;

import com.smarttask.model.Task;
import com.smarttask.model.Priority;
import com.smarttask.model.TaskStatus;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDAOImpl implements ITaskDAO {

    @Override
    public void insererTask(Task task) {
        String sql = "INSERT INTO tasks (user_id, titre, description, priorite, statut, deadline) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, task.getUserId());
            ps.setString(2, task.getTitre());
            ps.setString(3, task.getDescription());
            ps.setString(4, task.getPriorite().name());
            ps.setString(5, task.getStatut().name());
            ps.setString(6, task.getDeadline());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimerTask(int id) {
        String sql = "DELETE FROM tasks WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifierTask(Task task) {
        String sql = "UPDATE tasks SET titre=?, description=?, priorite=?, deadline=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, task.getTitre());
            ps.setString(2, task.getDescription());
            ps.setString(3, task.getPriorite().name());
            ps.setString(4, task.getDeadline());
            ps.setInt(5, task.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void archiverTask(int id) {
        String sql = "UPDATE tasks SET archive = true WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void changerStatut(int id, String statut) {
        String sql = "UPDATE tasks SET statut = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, statut);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Task> getToutesLesTasks(int userId) {
        List<Task> liste = new ArrayList<>();
        String sql = "SELECT * FROM tasks WHERE user_id = ? AND archive = false";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Task t = new Task(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("titre"),
                        rs.getString("description"),
                        Priority.valueOf(rs.getString("priorite")),
                        TaskStatus.valueOf(rs.getString("statut")),
                        rs.getString("deadline")
                );
                liste.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return liste;
    }

    @Override
    public List<Task> getTasksArchivees(int userId) {
        List<Task> liste = new ArrayList<>();
        String sql = "SELECT * FROM tasks WHERE user_id = ? AND archive = true";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Task t = new Task(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("titre"),
                        rs.getString("description"),
                        Priority.valueOf(rs.getString("priorite")),
                        TaskStatus.valueOf(rs.getString("statut")),
                        rs.getString("deadline")
                );
                liste.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return liste;
    }
}