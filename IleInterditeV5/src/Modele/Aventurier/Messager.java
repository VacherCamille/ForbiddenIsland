/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele.Aventurier;

import Modele.CarteTresor.CarteTresor;
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
    public boolean donnerCarte(Aventurier destinataire, String nomCarte) {

        if (getJoueur() != null) {
            CarteTresor carteDonnee = getJoueur().getCarteTresorFromName(nomCarte);
            getJoueur().removeCarteTresor(carteDonnee);
            destinataire.addCarteTresor(carteDonnee);
            System.out.println("\033[32m [ CARTE TRANSFEREE ! ]");
            return true;
        }
        return false;
    }
}
