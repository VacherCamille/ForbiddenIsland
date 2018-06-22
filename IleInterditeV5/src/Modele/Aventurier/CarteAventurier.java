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
import Util.Utils.Tresor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

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
            } else {
                System.out.println("\033[31m [ ERREUR DON DE CARTE : TUILE DIFFERENTE ! ]");
            }
        } else {
            System.out.println("\033[31m [ ERREUR DON DE CARTE : PAS ASSEZ DE PA ! ]");
        }
    }
    
    public HashSet<String> getJoueursTuile() {
        HashSet<String> joueursTuile = new HashSet<>(); // pour ne pas compter le donneur 2 fois
        HashMap<String, Position> liste = joueur.getEnvironnement().getPosJoueurs();
        Position pos = liste.get(joueur.getNomAventurier());

        for (String nomJoueur : liste.keySet()) {
            Position p = liste.get(nomJoueur);
            if (p.getColonne() == pos.getColonne() && p.getLigne() == pos.getLigne()) {
                joueursTuile.add(nomJoueur);
            }
        }
        joueursTuile.remove(joueur.getNomAventurier());
        return joueursTuile;
    }
    
    // === GAGNER TRESOR =======================================================
    
    public Tresor gagnerTresor() {
        int nbCarte = 0;
        Tresor tresor = getJoueur().getPosition().getTuile().getSpawnTresor();
        for (int i = 0; i <= getJoueur().getNbCartes(); i++) {
            CarteTresor carte = getJoueur().getDeckTresor().get(i);
            if (carte.getNomCarteT().equals(tresor.toString())) {
                nbCarte += 1;
            }
        }
        if (nbCarte >= 4) {
            int i = 4;
            for (CarteTresor ct : getJoueur().getDeckTresor()) {
                if (ct.getNomCarteT().equals(tresor.toString())) {
                    getJoueur().removeCarteTresor(ct);
                    i--;
                }
                if (i == 0) break;
            }
            return tresor;
        } else {
            return null;
        }
    }
    
    // === DEPLACEMENT =========================================================
    
    public void seDeplacer(int ligne, int colonne) {
        if (joueur != null && joueur.getPointAction() > 0) {
            joueur.getPosition().setColonne(colonne);
            joueur.getPosition().setLigne(ligne);
        }
    }
    
    // méthode pour récupérer les tuiles accessibles autour de soi :
    public ArrayList<Tuile> getTuilesDeplacement() {
        int posL = joueur.getPosition().getLigne();
        int posC = joueur.getPosition().getColonne();
        
        ArrayList<Tuile> tuilesPossibles = new ArrayList<>();
        Tuile tuile = joueur.getEnvironnement().getTuile(posL, posC + 1);
        if (tuile != null && (tuile.getEtat() == EtatTuile.INONDEE || tuile.getEtat() == EtatTuile.ASSECHEE)) { // tuile EST
            tuilesPossibles.add(tuile);
        }
        tuile = joueur.getEnvironnement().getTuile(posL + 1, posC);
        if (tuile != null && (tuile.getEtat() == EtatTuile.INONDEE || tuile.getEtat() == EtatTuile.ASSECHEE)) { // tuile SUD
            tuilesPossibles.add(tuile);
        }
        tuile = joueur.getEnvironnement().getTuile(posL, posC - 1);
        if (tuile != null && (tuile.getEtat() == EtatTuile.INONDEE || tuile.getEtat() == EtatTuile.ASSECHEE)) { // tuile OUEST
            tuilesPossibles.add(tuile);
        }
        tuile = joueur.getEnvironnement().getTuile(posL - 1, posC);
        if (tuile != null && (tuile.getEtat() == EtatTuile.INONDEE || tuile.getEtat() == EtatTuile.ASSECHEE)) { // tuile NORD
            tuilesPossibles.add(tuile);
        }
        return tuilesPossibles;
    }
    
    // === ASSECHEMENT =========================================================
    
    public void assecherTuile(int ligne, int colonne) {
        if (joueur != null && joueur.getPointAction() > 0) {
            Tuile tuile = joueur.getEnvironnement().getTuile(ligne, colonne);
            tuile.assecherTuile();
        }
    }
    
    // méthode pour récupérer les tuiles inondées autour de soi :
    public ArrayList<Tuile> getInondeesAdjacentes() {
        int posL = joueur.getPosition().getLigne();
        int posC = joueur.getPosition().getColonne();
        
        ArrayList<Tuile> tuilesInondees = new ArrayList<>();
        if (joueur.getTuile().getEtat() == EtatTuile.INONDEE) { // tuile JOUEUR
            tuilesInondees.add(joueur.getTuile()); 
        }
        Tuile tuile = joueur.getEnvironnement().getTuile(posL, posC+1);
        if (tuile != null && tuile.getEtat() == EtatTuile.INONDEE) { // tuile EST
            tuilesInondees.add(tuile);
        }
        tuile = joueur.getEnvironnement().getTuile(posL+1, posC);
        if (tuile != null && tuile.getEtat() == EtatTuile.INONDEE) { // tuile SUD
            tuilesInondees.add(tuile);
        }
        tuile = joueur.getEnvironnement().getTuile(posL, posC-1);
        if (tuile != null && tuile.getEtat() == EtatTuile.INONDEE) { // tuile OUEST
            tuilesInondees.add(tuile);
        }
        tuile = joueur.getEnvironnement().getTuile(posL-1, posC);
        if (tuile != null && tuile.getEtat() == EtatTuile.INONDEE) { // tuile NORD
            tuilesInondees.add(tuile);
        }
        return tuilesInondees;
    }
    
    // === ACTION SPECIALE =====================================================
    
    
}
