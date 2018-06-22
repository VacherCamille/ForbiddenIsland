/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MVC;

import java.util.ArrayList;

/**
 *
 * @author Aymerick
 */
public class Message {
    public TypesMessages type;
    public String destinateur;
    
    // DEMARRER_PARTIE
    public ArrayList<String> listeJoueurs;
    public String difficulte;
    
    // DONNER_CARTE
    public String nomCarteT;
    public String destinataire;
    
    // SE_DEPLACER / ASSECHER
    public int posL;
    public int posC;
    
    // HELICOPTERE / SACS DE SABLE
    public boolean cardMode = false;
}
