/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele.Aventurier;

import Modele.CarteTresor.CarteTresor;
import Modele.Plateau.Tuile;
import Util.Utils;

/**
 *
 * @author dieuaida
 */
public class Messager extends CarteAventurier {
    
    public Messager() {
        super("Messager", Utils.Pion.ORANGE);
    }
    
    @Override
    public void donnerCarte(Aventurier destinataire, String nomCarte) {
        if (destinataire.hasFullDeck()) {
            System.out.println("\033[31m [ ERREUR DON DE CARTE : DECK DESTINATAIRE PLEIN");
            return;
        }
        if (getJoueur() != null && getJoueur().getPointAction() > 0) {
                CarteTresor carteDonnee = getJoueur().getCarteTresorFromName(nomCarte);
                getJoueur().removeCarteTresor(carteDonnee);
                destinataire.addCarteTresor(carteDonnee);
                System.out.println("\033[32m [ CARTE TRANSFEREE ! ]");
                getJoueur().utiliserPA();
        } else {
            System.out.println("\033[31m [ ERREUR DON DE CARTE : PAS ASSEZ DE PA ! ]");
        }
    }
}
