package com.smarttask.model;

public class ArchivedTask extends Task {
    private String dateArchivage;

    public ArchivedTask(int id, int userId, String titre, String description,
                        Priority priorite, TaskStatus statut,
                        String deadline, String dateArchivage) {
        super(id, userId, titre, description, priorite, statut, deadline);
        this.dateArchivage = dateArchivage;
        this.setArchive(true);
    }

    public String getDateArchivage() { return dateArchivage; }
    public void setDateArchivage(String d) { this.dateArchivage = d; }
}