package com.smarttask.utils;

import com.smarttask.model.Task;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVExporter {

    public static void exporter(List<Task> tasks, String nomFichier) {
        try (FileWriter writer = new FileWriter(nomFichier)) {

            // La première ligne = les titres des colonnes
            writer.write("ID,Titre,Description,Statut,Priorite,Deadline\n");

            // Ensuite une ligne par tâche
            for (Task t : tasks) {
                writer.write(
                        t.getId() + "," +
                                t.getTitre() + "," +
                                t.getDescription() + "," +
                                t.getStatut() + "," +
                                t.getPriorite() + "," +
                                t.getDeadline() + "\n"
                );
            }

            System.out.println("Export CSV réussi : " + nomFichier);

        } catch (IOException e) {
            System.out.println("Erreur export CSV : " + e.getMessage());
        }
    }
}