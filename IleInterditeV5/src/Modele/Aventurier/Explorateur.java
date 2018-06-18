/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele.Aventurier;

import Modele.Plateau.Grille;
import Modele.Plateau.Position;
import Modele.Plateau.Tuile;
import Util.Utils.EtatTuile;
import Util.Utils.Pion;
import java.util.ArrayList;

/**
 *
 * @author dieuaida
 */
public class Explorateur extends CarteAventurier {

    public Explorateur() {
        super("Explorateur", Pion.VERT);
    }

    // === SE DEPLACER =========================================================
    // rajout des cases diagonales
    @Override
    public ArrayList<Tuile> getTuilesDeplacement() {
        int posL = getJoueur().getPosition().getLigne();
        int posC = getJoueur().getPosition().getColonne();

        ArrayList<Tuile> tuilesdeplacement = super.getTuilesDeplacement(); // tuiles JOUEUR, EST, SUD, OUEST, NORD
        Grille grille = getJoueur().getGrille();

        Tuile tuile = grille.getTuile(posL - 1, posC - 1);
        if (tuile != null && (tuile.getEtat() == EtatTuile.ASSECHEE || tuile.getEtat() == EtatTuile.INONDEE)) { // tuile NORD-OUEST
            tuilesdeplacement.add(tuile);
        }
        tuile = grille.getTuile(posL - 1, posC + 1);
        if (tuile != null && (tuile.getEtat() == EtatTuile.ASSECHEE || tuile.getEtat() == EtatTuile.INONDEE)) { // tuile NORD-EST
            tuilesdeplacement.add(tuile);
        }
        tuile = grille.getTuile(posL + 1, posC + 1);
        if (tuile != null && (tuile.getEtat() == EtatTuile.ASSECHEE || tuile.getEtat() == EtatTuile.INONDEE)) { // tuile SUD-EST
            tuilesdeplacement.add(tuile);
        }
        tuile = grille.getTuile(posL + 1, posC - 1);
        if (tuile != null && (tuile.getEtat() == EtatTuile.ASSECHEE || tuile.getEtat() == EtatTuile.INONDEE)) { // tuile SUD-OUEST
            tuilesdeplacement.add(tuile);
        }
        return tuilesdeplacement;
    }

    // === ASSECHEMENT =========================================================
    // rajout des diagonales
    @Override
    public ArrayList<Tuile> getInondeesAdjacentes() {
        int posL = getJoueur().getPosition().getLigne();
        int posC = getJoueur().getPosition().getColonne();

        ArrayList<Tuile> tuilesInondees = super.getInondeesAdjacentes(); // tuiles JOUEUR, EST, SUD, OUEST, NORD
        Grille grille = getJoueur().getGrille();

        Tuile tuile = grille.getTuile(posL - 1, posC - 1);
        if (tuile != null && tuile.getEtat() == EtatTuile.INONDEE) { // tuile NORD-OUEST
            tuilesInondees.add(tuile);
        }
        tuile = grille.getTuile(posL - 1, posC + 1);
        if (tuile != null && tuile.getEtat() == EtatTuile.INONDEE) { // tuile NORD-EST
            tuilesInondees.add(tuile);
        }
        tuile = grille.getTuile(posL + 1, posC + 1);
        if (tuile != null && tuile.getEtat() == EtatTuile.INONDEE) { // tuile SUD-EST
            tuilesInondees.add(tuile);
        }
        tuile = grille.getTuile(posL + 1, posC - 1);
        if (tuile != null && tuile.getEtat() == EtatTuile.INONDEE) { // tuile SUD-OUEST
            tuilesInondees.add(tuile);
        }

        return tuilesInondees;
    }
}
