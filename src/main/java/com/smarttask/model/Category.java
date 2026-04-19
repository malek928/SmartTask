package com.smarttask.model;

public class Category {
    private int id;
    private int userId;
    private String nom;
    private String couleur;

    public Category(int id, int userId, String nom, String couleur) {
        this.id = id;
        this.userId = userId;
        this.nom = nom;
        this.couleur = couleur;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getCouleur() { return couleur; }
    public void setCouleur(String couleur) { this.couleur = couleur; }
}