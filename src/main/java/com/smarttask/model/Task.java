package com.smarttask.model;

public class Task {
    private int id;
    private int userId;
    private String titre;
    private String description;
    private Priority priorite;
    private TaskStatus statut;
    private String deadline;
    private boolean archive;
    private int categoryId;

    public Task(int id, int userId, String titre, String description,
                Priority priorite, TaskStatus statut, String deadline) {
        this.id = id;
        this.userId = userId;
        this.titre = titre;
        this.description = description;
        this.priorite = priorite;
        this.statut = statut;
        this.deadline = deadline;
        this.archive = false;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Priority getPriorite() { return priorite; }
    public void setPriorite(Priority priorite) { this.priorite = priorite; }
    public TaskStatus getStatut() { return statut; }
    public void setStatut(TaskStatus statut) { this.statut = statut; }
    public String getDeadline() { return deadline; }
    public void setDeadline(String deadline) { this.deadline = deadline; }
    public boolean isArchive() { return archive; }
    public void setArchive(boolean archive) { this.archive = archive; }
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
}