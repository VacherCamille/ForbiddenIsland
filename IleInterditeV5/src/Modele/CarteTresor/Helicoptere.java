/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele.CarteTresor;

import Modele.Aventurier.Aventurier;
import Modele.Plateau.Grille;
import Modele.Plateau.Tuile;
import Util.Utils;
import java.util.ArrayList;

/**
 *
 * @author dieuaida
 */
public class Helicoptere extends CarteTresor {
    
    public Helicoptere() {
        super("Helicoptere");
    }
    
    public void utiliserHelico(Aventurier joueur,int l, int c) {
        if (getTuilesHelico(joueur).contains(joueur.getEnvironnement().getTuile(l, c))) {
            joueur.getPosition().setColonne(c);
            joueur.getPosition().setLigne(l);
        }
    }
    
    public ArrayList<Tuile> getTuilesHelico(Aventurier joueur) {
        ArrayList<Tuile> tuiles = new ArrayList<>();
        Grille g = joueur.getEnvironnement();
        for (int i = 0; i <= 5; i++) {
            for (int j = 0; j <= 5; j++) {
                if (g.getTuile(i, j).getEtat() != Utils.EtatTuile.COULEE) {
                    tuiles.add(g.getTuile(i, j));
                }
            }
        }
        return tuiles;
}
}