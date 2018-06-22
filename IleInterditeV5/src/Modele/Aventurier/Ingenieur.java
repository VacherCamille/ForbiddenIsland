/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele.Aventurier;

import Modele.Plateau.Tuile;
import Util.Utils;

/**
 *
 * @author dieuaida
 */
public class Ingenieur extends CarteAventurier {

    public Ingenieur() {
        super("Ingénieur", Utils.Pion.ROUGE);
    }

    @Override
    public void assecherTuile(int ligne, int colonne) {
        if (getJoueur() != null) {
            Tuile tuile = getJoueur().getEnvironnement().getTuile(ligne, colonne);
            tuile.assecherTuile();
        }
    }
}
