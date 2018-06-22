/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele.Aventurier;

import Modele.Plateau.Tuile;
import Util.Utils;
import java.util.ArrayList;

/**
 *
 * @author dieuaida
 */
public class Navigateur extends CarteAventurier {

    public Navigateur() {
        super("Navigateur", Utils.Pion.JAUNE);
    }

    public void deplacerJoueur(Aventurier joueur, int ligne, int colonne) {

        if (getTuilesDeplacementNavigateur().contains(joueur.getEnvironnement().getTuile(ligne, colonne))) {
            System.out.println("\033[32m [ DEPLACEMENT EFFECTUE ! ]");
            joueur.getPosition().setColonne(colonne);
            joueur.getPosition().setLigne(ligne);
            joueur.utiliserPA();
        }
    }

    public ArrayList<Tuile> getTuilesDeplacementNavigateur() {
        Aventurier joueur = super.getJoueur();
        int posL = super.getJoueur().getPosition().getLigne();
        int posC = joueur.getPosition().getColonne();

        ArrayList<Tuile> tuiles = getTuilesAdjacentes(posL, posC);
        int[] pos;
        for (Tuile t : tuiles) {
            pos = getJoueur().getGrille().getPosFromTuile(t);
            for (Tuile t1 : getTuilesAdjacentes(pos[0], pos[1])) {
                if (!tuiles.contains(t1)) {
                    tuiles.add(t1);
                }
            }
        }
        return tuiles;
    }

    public ArrayList<Tuile> getTuilesAdjacentes(int posL, int posC) {
        Aventurier joueur = super.getJoueur();
        ArrayList<Tuile> tuiles1 = new ArrayList<>();

        // A OPTIMISER...
        ArrayList<Tuile> tuilesPossibles = new ArrayList<>();
        Tuile tuile = joueur.getGrille().getTuile(posL, posC + 1);
        if (tuile != null && (tuile.getEtat() == Utils.EtatTuile.INONDEE || tuile.getEtat() == Utils.EtatTuile.ASSECHEE)) { // tuile EST
            tuilesPossibles.add(tuile);
        }
        tuile = joueur.getGrille().getTuile(posL + 1, posC);
        if (tuile != null && (tuile.getEtat() == Utils.EtatTuile.INONDEE || tuile.getEtat() == Utils.EtatTuile.ASSECHEE)) { // tuile SUD
            tuilesPossibles.add(tuile);
        }
        tuile = joueur.getGrille().getTuile(posL, posC - 1);
        if (tuile != null && (tuile.getEtat() == Utils.EtatTuile.INONDEE || tuile.getEtat() == Utils.EtatTuile.ASSECHEE)) { // tuile OUEST
            tuilesPossibles.add(tuile);
        }
        tuile = joueur.getGrille().getTuile(posL - 1, posC);
        if (tuile != null && (tuile.getEtat() == Utils.EtatTuile.INONDEE || tuile.getEtat() == Utils.EtatTuile.ASSECHEE)) { // tuile NORD
            tuilesPossibles.add(tuile);
        }
        return tuilesPossibles;
    }
}
