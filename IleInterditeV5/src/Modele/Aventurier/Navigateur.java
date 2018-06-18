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
    
  
    public void deplacerJoueur(Aventurier joueur,int ligne, int colonne) {
        
        if(getTuilesDeplacement().contains(joueur.getEnvironnement().getTuile(ligne, colonne))){         
                System.out.println("\033[32m [ DEPLACEMENT EFFECTUE ! ]");
                joueur.getPosition().setColonne(colonne);
                joueur.getPosition().setLigne(ligne);
                joueur.utiliserPA();
        }
    }
}
