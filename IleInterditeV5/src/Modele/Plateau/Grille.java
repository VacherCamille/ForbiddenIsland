/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele.Plateau;

import Util.Parameters;
import Util.Utils;
import Util.Utils.EtatTuile;
import Util.Utils.Pion;
import Util.Utils.Tresor;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author dieuaida
 */
public class Grille {
    private final Tuile tuiles[][] = new Tuile[6][6];
    private HashMap<String, Position> posJoueurs;
    
    public Grille() {
        posJoueurs = new HashMap<>();
        // Initialisation et mélange des tuiles de jeu...
        ArrayList<Tuile> listeTuiles = new ArrayList<>();
        this.initTuiles(listeTuiles);
        
        // Disposition des tuiles sur le plateau...
        this.initGrille(listeTuiles);
    }
    
    // === GETTERS & SETTERS ===================================================

    public Tuile[][] getTuiles() {
        return tuiles;
    }

    public HashMap<String, Position> getPosJoueurs() {
        return posJoueurs;
    }

    public void setPosJoueurs(HashMap<String, Position> posJoueurs) {
        this.posJoueurs = posJoueurs;
    }
    
    // === INITIALISATION ======================================================
    
    private void initTuiles(ArrayList<Tuile> arrayList) {
        for (String args : Parameters.tuilesJeu) { // cf. Parameters
            String parts[] = args.split(","); // les caractéristiques sont séparés par des virgules...
            String nomTuile = parts[0];
            Pion spawnPion = Pion.getFromName(parts[1]);
            Tresor spawnTresor = Tresor.getFromName(parts[2]);
            arrayList.add(new Tuile(nomTuile, spawnPion, spawnTresor));
        }
        Utils.melangerTuiles(arrayList);
    }
    
    private void initGrille(ArrayList<Tuile> arrayList) {
        int k = 0;
        // PATTERN DE L'ÎLE :
        for (int i = 0; i < 6; i++) {
            switch (i) {
                case 0: case 5:
                    tuiles[i][0] = null;    tuiles[i][1] = null;
                    tuiles[i][4] = null;    tuiles[i][5] = null;
                    tuiles[i][2] = arrayList.get(k); k += 1;
                    tuiles[i][3] = arrayList.get(k); k += 1;
                    break;
                case 1: case 4:
                    tuiles[i][0] = null;    tuiles[i][5] = null;
                    for (int j = 1; j < 5; j++) {
                        tuiles[i][j] = arrayList.get(k); k += 1;
                    }
                    break;
                case 2: case 3:
                    for (int j = 0; j <= 5; j++) {
                        tuiles[i][j] = arrayList.get(k); k += 1;
                    }
                    break;
            }
        }
    }
    
    // === UTILITAIRE ==========================================================
    
    public void setPosJoueur(String nomAventurier, Position position) {
        this.posJoueurs.put(nomAventurier, position);
    }
    
    public Tuile getTuile(int ligne, int colonne) {
        if (ligne < 0 || ligne > 5 || colonne < 0 || colonne > 5) {
            return null;
        } else {
            return tuiles[ligne][colonne];
        }
    }
    
    public Tuile getTuileFromName(String nomTuile) {
        Tuile tuile = null;
        boolean trouve = false;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                tuile = this.getTuile(i, j);
                if (tuile != null && tuile.getNomTuile().equals(nomTuile)) {
                    trouve = true;
                    break;
                }
            }
            if (trouve) break;
        }
        return tuile;
    }
    
    public Tuile getTuileFromSpawnPion(Pion pion) {
        Tuile tuile = null;
        boolean trouve = false;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                tuile = this.getTuile(i, j);
                if (tuile != null && tuile.getSpawnPion() == pion) {
                    trouve = true;
                    break;
                }
            }
            if (trouve) break;
        }
        return tuile;
    }
    
    public int[] getPosFromTuile(Tuile tuile) {
        int coordonnees[] = new int[2];
        boolean trouve = false;
        for (int i = 0; i < 6; i++) { // ligne
            for (int j = 0; j < 6; j++) { // colonne
                if (tuiles[i][j] != null && tuiles[i][j].equals(tuile)) {
                    trouve = true;
                    coordonnees[0] = i; // ligne
                    coordonnees[1] = j; // colonne
                    break;
                }
            }
            if (trouve) break;
        }
        return coordonnees;
    }
    
    public ArrayList<Tuile> getTuilesInondees() {
        ArrayList<Tuile> tuiles = new ArrayList<>();
        for (int ligne = 0; ligne < 6; ligne++) {
            for (int colonne = 0; colonne < 6; colonne++) {
                Tuile tuile = getTuile(ligne, colonne);
                if (tuile != null && tuile.getEtat() == EtatTuile.INONDEE) tuiles.add(tuile);
            }
        }
        return tuiles;
    }
    
    public ArrayList<Tuile> getTuilesInondeesAssechees() {
        ArrayList<Tuile> tuiles = new ArrayList<>();
        for (int ligne = 0; ligne < 6; ligne++) {
            for (int colonne = 0; colonne < 6; colonne++) {
                Tuile tuile = getTuile(ligne, colonne);
                if (tuile != null && (tuile.getEtat() == EtatTuile.INONDEE || tuile.getEtat() == EtatTuile.ASSECHEE)) {
                    tuiles.add(tuile);
                }
            }
        }
        return tuiles;
    }
}