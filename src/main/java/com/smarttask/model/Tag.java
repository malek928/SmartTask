package com.smarttask.model;

public class Tag {
    private int id;
    private int userId;
    private String nom;

    public Tag(int id, int userId, String nom) {
        this.id = id;
        this.userId = userId;
        this.nom = nom;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
}