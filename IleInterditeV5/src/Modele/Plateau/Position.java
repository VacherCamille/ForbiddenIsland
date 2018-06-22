/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele.Plateau;

import Modele.Aventurier.Aventurier;

/**
 *
 * @author dieuaida
 */
public class Position {
    private int ligne;
    private int colonne;
    private Grille grille;
    private Aventurier aventurier;
    
    public Position(Grille grille, Aventurier aventurier, int ligne, int colonne) {
        this.grille = grille;
        this.aventurier = aventurier;
        this.ligne = ligne;
        this.colonne = colonne;
    }
    
    // === GETTERS & SETTERS ===================================================

    public int getLigne() {
        return ligne;
    }

    public int getColonne() {
        return colonne;
    }

    public Grille getGrille() {
        return grille;
    }

    public Aventurier getAventurier() {
        return aventurier;
    }

    public void setLigne(int ligne) {
        this.ligne = ligne;
    }

    public void setColonne(int colonne) {
        this.colonne = colonne;
    }
    
    // === MODE POSITION NEUTRE (POUR PLONGEUR) ================================
    
    public Position(int ligne, int colonne) {
        this.ligne = ligne;
        this.colonne = colonne;
    }
    
    @Override
    public boolean equals(Object obj) {
        Position pos = (Position) obj;
        return (pos.getColonne() == this.getColonne() && pos.getLigne() == this.getLigne());
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 14 * hash + this.ligne;
        hash = 67 * hash + this.colonne;
        return hash;
    }
    
    // === UTILITAIRE ==========================================================
    
    public Tuile getTuile() {
        return grille.getTuile(ligne, colonne);
    }
}
