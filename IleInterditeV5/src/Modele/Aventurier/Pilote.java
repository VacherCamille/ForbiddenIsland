/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele.Aventurier;

import Modele.Plateau.Grille;
import Modele.Plateau.Tuile;
import Util.Utils;
import java.util.ArrayList;

/**
 *
 * @author dieuaida
 */
public class Pilote extends CarteAventurier {

    public Pilote() {
        super("Pilote", Utils.Pion.BLEU);
    }

    private boolean actionSpeciale = true;

    public void prendreHelico(int l, int c) {
        if (actionSpeciale && getTuilesHelico().contains(getJoueur().getEnvironnement().getTuile(l, c))) {
            getJoueur().getPosition().setColonne(c);
            getJoueur().getPosition().setLigne(l);
        }
    }

    public ArrayList<Tuile> getTuilesHelico() {
        ArrayList<Tuile> tuiles = new ArrayList<>();
        Grille g = getJoueur().getEnvironnement();
        for (int i = 0; i <= 5; i++) {
            for (int j = 0; j <= 5; j++) {
                if (g.getTuile(i, j).getEtat() != Utils.EtatTuile.COULEE) {
                    tuiles.add(g.getTuile(i, j));
                }
            }
        }
        return tuiles;
    }

    public void setActionSpeciale(boolean actionSpeciale) {
        this.actionSpeciale = actionSpeciale;
    }

}
