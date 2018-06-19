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
    public Grille getEnvironnement(){
        return position.getGrille();
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
    
    public Grille getGrille() { // raccourci
        return this.getPosition().getGrille();
    }
    
    public Tuile getTuile() {
        /*int ligne = this.getPosition().getLigne();
        int colonne = this.getPosition().getColonne();
        return this.getGrille().getTuile(ligne, colonne);*/
        return this.getPosition().getTuile();
    }
    
    public Pion getPion() { // raccourci
        return this.getRole().getPion();
    }
    
    public int getNbCartes() {
        return this.getDeckTresor().size();
    }
    
    public boolean hasFullDeck() {
        return this.getNbCartes() == 5;
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
    
    // === RACCOURCI ACTION ====================================================
    
    public boolean donnerCarte(Aventurier destinataire, String nomCarteT) {
        return this.getRole().donnerCarte(destinataire, nomCarteT);
    }
    
    public boolean seDeplacer(int ligne, int colonne) {
        return this.getRole().seDeplacer(ligne, colonne);
    }
    
    public boolean assecherTuile(int ligne, int colonne) {
        return this.getRole().assecherTuile(ligne, colonne);
    }
    public ArrayList<Tuile> getTuilesDeplacement(){
        return this.getRole().getTuilesDeplacement();
    }
    public ArrayList<Tuile> getTuilesAssechement(){
        return this.getRole().getInondeesAdjacentes();
    }
}
