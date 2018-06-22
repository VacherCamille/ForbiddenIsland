/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele.Aventurier;

import Modele.CarteTresor.CarteTresor;
import Modele.Plateau.Grille;
import Modele.Plateau.Position;
import Modele.Plateau.Tuile;
import Util.Utils.Pion;
import java.util.ArrayList;

/**
 *
 * @author dieuaida
 */
public class Aventurier {
    private final String nomAventurier;
    private final CarteAventurier role;
    private Position position;
    private ArrayList<CarteTresor> deckTresor;
    private int pointAction;
    
    public Aventurier(String nomAventurier, CarteAventurier role) {
        this.nomAventurier = nomAventurier;
        this.role = role;
        this.pointAction = 3;
        
        deckTresor = new ArrayList<>();
    }
    
    // === GETTERS & SETTERS ===================================================

    public String getNomAventurier() {
        return nomAventurier;
    }

    public CarteAventurier getRole() {
        return role;
    }

    public Position getPosition() {
        return position;
    }

    public ArrayList<CarteTresor> getDeckTresor() {
        return deckTresor;
    }

    public int getPointAction() {
        return pointAction;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
    
    // === UTILITAIRE ==========================================================
    
    public Grille getEnvironnement() {
        return position.getGrille();
    }
    
    public Tuile getTuile() {
        return position.getTuile();
    }
    
    public Pion getPion() {
        return this.getRole().getPion();
    }
    
    public int getNbCartes() {
        return this.getDeckTresor().size();
    }
    
    public boolean hasFullDeck() {
        return this.getNbCartes() > 5;
    }
    
    public void addCarteTresor(CarteTresor carteTresor) {
        deckTresor.add(carteTresor);
    }
    
    public CarteTresor getCarteTresorFromName(String nomCarteT) {
        for (CarteTresor carte : this.getDeckTresor()) {
            if (carte.getNomCarteT().equals(nomCarteT)) return carte;
        }
        return null;
    }
    
    public void removeCarteTresor(CarteTresor carteTresor) {
        if (deckTresor.contains(carteTresor)) deckTresor.remove(carteTresor);
    }
    
    public void utiliserPA() {
        this.pointAction -= 1;
    }
    
    public void reinitialiserPA() {
        this.pointAction = 3;
    }
    
    public CarteTresor removeOccurenceCarte(String nomCarte) {
        for (CarteTresor carte : deckTresor) {
            if (carte.getNomCarteT().equals(nomCarte)) {
                deckTresor.remove(carte);
                return carte;
            }
        }
        return null;
    }
    
    // === RACCOURCI ACTION ====================================================
    
    public boolean donnerCarte(Aventurier destinataire, String nomCarteT) {
        return this.getRole().donnerCarte(destinataire, nomCarteT);
    }
    
    public void seDeplacer(int ligne, int colonne) {
        this.getRole().seDeplacer(ligne, colonne);
    }
    
    public ArrayList<Tuile> getTuilesDeplacement() {
        return role.getTuilesDeplacement();
    }
    
    public void assecherTuile(int ligne, int colonne) {
        this.getRole().assecherTuile(ligne, colonne);
    }
    
    public ArrayList<Tuile> getTuilesAssechement() {
        return role.getInondeesAdjacentes();
    }
    
    public Grille getGrille(){
        return this.getPosition().getGrille();
    }
}
