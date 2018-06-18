/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele.Aventurier;

import Modele.CarteTresor.CarteTresor;
import Modele.Plateau.Position;
import Modele.Plateau.Tuile;
import Util.Utils.EtatTuile;
import Util.Utils.Pion;
import java.util.ArrayList;

/**
 *
 * @author dieuaida
 */
public abstract class CarteAventurier {
    private final String nomRole;
    private final Pion pion;
    private Aventurier joueur;
    
    public CarteAventurier(String nomRole, Pion pion) {
        this.nomRole = nomRole;
        this.pion = pion;
    }
    
    // === GETTERS & SETTERS ===================================================

    public String getNomRole() {
        return nomRole;
    }

    public Pion getPion() {
        return pion;
    }

    public Aventurier getJoueur() {
        return joueur;
    }

    public void setJoueur(Aventurier joueur) {
        this.joueur = joueur;
    }
    
    // === DONNER CARTE ========================================================
    
    public void donnerCarte(Aventurier destinataire, String nomCarte) {
        if (destinataire.hasFullDeck()) {
            System.out.println("\033[31m [ ERREUR DON DE CARTE : DECK DESTINATAIRE PLEIN");
            return;
        }
        if (joueur != null && joueur.getPointAction() > 0) {
            Tuile tuileDestinateur = joueur.getTuile();
            //Tuile tuileDestinataire = destinataire.getPosition().getTuile();
            Tuile tuileDestinataire = destinataire.getTuile();
            if (tuileDestinateur.equals(tuileDestinataire)) {
                CarteTresor carteDonnee = joueur.getCarteTresorFromName(nomCarte);
                joueur.removeCarteTresor(carteDonnee);
                destinataire.addCarteTresor(carteDonnee);
                System.out.println("\033[32m [ CARTE TRANSFEREE ! ]");
                joueur.utiliserPA();
            } else {
                System.out.println("\033[31m [ ERREUR DON DE CARTE : TUILE DIFFERENTE ! ]");
            }
        } else {
            System.out.println("\033[31m [ ERREUR DON DE CARTE : PAS ASSEZ DE PA ! ]");
        }
    }
    
    // === DEPLACEMENT =========================================================
    
    public void seDeplacer(int ligne, int colonne) {
        if(getTuilesDeplacement().contains(getJoueur().getEnvironnement().getTuile(ligne, colonne))){         
                System.out.println("\033[32m [ DEPLACEMENT EFFECTUE ! ]");
                joueur.getPosition().setColonne(colonne);
                joueur.getPosition().setLigne(ligne);
                joueur.utiliserPA();
        }else{
            System.out.println("\033[32m [ ERREUR ! ]");
        }
    }
    
    // === ASSECHEMENT =========================================================
    
    public void assecherTuile(int ligne, int colonne) {
        if (joueur != null && joueur.getPointAction() > 0) {
            ArrayList<Tuile> tuilesInondees = this.getInondeesAdjacentes();
            
            if (tuilesInondees.contains(joueur.getGrille().getTuile(ligne, colonne))) {
                Tuile tuile = joueur.getGrille().getTuile(ligne, colonne);
                
                tuile.assecherTuile();
                System.out.println("\033[32m [ ASSECHEMENT EFFECTUE ! ]");
                joueur.utiliserPA();
            } else {
                System.out.println("\033[31m [ ERREUR  ASSECHEMENT : TUILE DEJA ASSECHEE OU HORS DE PORTEE ! ]");
            }
        } else {
            System.out.println("\033[31m [ ERREUR ASSECHEMENT : PAS ASSEZ DE PA ! ]");
        }
    }
    
    // THE méthode coeur de l'assèchement
    public ArrayList<Tuile> getInondeesAdjacentes() {
        int posL = joueur.getPosition().getLigne();
        int posC = joueur.getPosition().getColonne();
        
        
        // A OPTIMISER...
        ArrayList<Tuile> tuilesInondees = new ArrayList<>();
        if (joueur.getTuile().getEtat() == EtatTuile.INONDEE) { // tuile JOUEUR
            tuilesInondees.add(joueur.getTuile()); 
        }
        Tuile tuile = joueur.getGrille().getTuile(posL, posC+1);
        if (tuile != null && tuile.getEtat() == EtatTuile.INONDEE) { // tuile EST
            tuilesInondees.add(tuile);
        }
        tuile = joueur.getGrille().getTuile(posL+1, posC);
        if (tuile != null && tuile.getEtat() == EtatTuile.INONDEE) { // tuile SUD
            tuilesInondees.add(tuile);
        }
        tuile = joueur.getGrille().getTuile(posL, posC-1);
        if (tuile != null && tuile.getEtat() == EtatTuile.INONDEE) { // tuile OUEST
            tuilesInondees.add(tuile);
        }
        tuile = joueur.getGrille().getTuile(posL-1, posC);
        if (tuile != null && tuile.getEtat() == EtatTuile.INONDEE) { // tuile NORD
            tuilesInondees.add(tuile);
        }
        return tuilesInondees;
    }
    
     public ArrayList<Tuile> getTuilesDeplacement() {
        int posL = joueur.getPosition().getLigne();
        int posC = joueur.getPosition().getColonne();
        
        
        // A OPTIMISER...
        ArrayList<Tuile> tuilesPossibles = new ArrayList<>();
        if (joueur.getTuile().getEtat() == EtatTuile.INONDEE || joueur.getTuile().getEtat() == EtatTuile.ASSECHEE) { // tuile JOUEUR
            tuilesPossibles.add(joueur.getTuile()); 
        }
        Tuile tuile = joueur.getGrille().getTuile(posL, posC+1);
        if (tuile != null && (tuile.getEtat() == EtatTuile.INONDEE ||tuile.getEtat() == EtatTuile.ASSECHEE)) { // tuile EST
            tuilesPossibles.add(tuile);
        }
        tuile = joueur.getGrille().getTuile(posL+1, posC);
        if (tuile != null && (tuile.getEtat() == EtatTuile.INONDEE ||tuile.getEtat() == EtatTuile.ASSECHEE)) { // tuile SUD
            tuilesPossibles.add(tuile);
        }
        tuile = joueur.getGrille().getTuile(posL, posC-1);
        if (tuile != null && (tuile.getEtat() == EtatTuile.INONDEE ||tuile.getEtat() == EtatTuile.ASSECHEE)) { // tuile OUEST
            tuilesPossibles.add(tuile);
        }
        tuile = joueur.getGrille().getTuile(posL-1, posC);
        if (tuile != null && (tuile.getEtat() == EtatTuile.INONDEE ||tuile.getEtat() == EtatTuile.ASSECHEE)) { // tuile NORD
            tuilesPossibles.add(tuile);
        }
        return tuilesPossibles;
    }
    
    // === GAGNER TRESOR =======================================================
    
    // en cours d'implantation
}
