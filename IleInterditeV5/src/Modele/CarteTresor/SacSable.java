/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele.CarteTresor;

import Modele.Aventurier.Aventurier;
import Modele.Plateau.Tuile;
import Util.Utils;
import Util.Utils.EtatTuile;
import java.util.ArrayList;

/**
 *
 * @author dieuaida
 */
public class SacSable extends CarteTresor {

    public SacSable() {
        super("Sacs de Sable");
    }

    public void  assecheSacSable (Aventurier a, int ligne, int colonne) {

        ArrayList<Tuile> toutesTuilesInondees = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                Tuile tuile = a.getGrille().getTuile(j, i);
                if (tuile.getEtat() == EtatTuile.INONDEE) {
                    toutesTuilesInondees.add(tuile);

                }
                

            }
        }
        
        if ( (a != null && a.getPointAction() > 0)     && (toutesTuilesInondees.contains(a.getGrille().getTuile(ligne, colonne)))){
             Tuile tuile = a.getGrille().getTuile(ligne, colonne);
              tuile.assecherTuile();
              System.out.println("\033[32m [ ASSECHEMENT EFFECTUE ! ]");
                a.utiliserPA();
                toutesTuilesInondees.remove(tuile);
            } else {
                System.out.println("\033[31m [ ERREUR  ASSECHEMENT : TUILE DEJA ASSECHEE OU HORS DE PORTEE ! ]");
            }
        
            
            
        
    
}
    }
    


    
   