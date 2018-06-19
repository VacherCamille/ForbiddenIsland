/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ADMIN;

import MVC.Message;
import MVC.Observateur;
import Modele.Aventurier.Aventurier;
import Modele.Aventurier.CarteAventurier;
import Modele.Aventurier.Explorateur;
import Modele.Aventurier.Ingenieur;
import Modele.Aventurier.Messager;
import Modele.Aventurier.Navigateur;
import Modele.Aventurier.Pilote;
import Modele.Aventurier.Plongeur;
import Modele.CarteTresor.CarteButin;
import Modele.CarteTresor.CarteTresor;
import Modele.CarteTresor.Helicoptere;
import Modele.CarteTresor.MonteeEau;
import Modele.CarteTresor.SacSable;
import Modele.Divers.CarteInondation;
import Modele.Divers.NivEau;
import Modele.Plateau.Grille;
import Modele.Plateau.Position;
import Modele.Plateau.Tuile;
import Util.Parameters;
import Util.Utils;
import Util.Utils.EtatTuile;
import Util.Utils.Pion;
import Util.Utils.Tresor;
import Vues.EcranPrincipal;
import Vues.PlateauJeu;
import Vues.VueAventurier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Aymerick
 */
public class Controleur implements Observateur {
    // Message
        // DEMARRER_PARTIE:
    private int nbJoueurs;
    private String[] listeJoueurs;
    private int indexOrdre = 0;
    private String difficulte;
    
    // Vues
    private final EcranPrincipal ecranPrincipal;
    private PlateauJeu plateauJeu;
    
    // Modele
    private Grille grille;
    
    private HashMap<Utils.Tresor, Boolean> collectionTresor; // les trésors récupérés ou non
    
    private ArrayList<CarteInondation> pileInondation;
    private ArrayList<CarteInondation> defausseInondation;
    private ArrayList<CarteTresor> pileTresor;
    private ArrayList<CarteTresor> defausseTresor;
    private ArrayList<CarteAventurier> pileAventurier;
    
    private HashMap<String, Aventurier> joueurs;
    
    private NivEau niveauDEau;
    
    public Controleur() {
        ecranPrincipal = new EcranPrincipal();
        ecranPrincipal.addObservateur(this);
        ecranPrincipal.afficher();
    }
    
    @Override
    public void traiterMessage(Message msg) {
        Aventurier destinateur;
        int posL, posC;
        
        switch (msg.type) {
            case DEMARRER_PARTIE:
                ecranPrincipal.fermer();
                
                // récupération des données Message
                nbJoueurs = msg.nbJoueurs;
                listeJoueurs = msg.listeJoueurs;
                difficulte = msg.difficulte;
                
                // Initialisation des Collections...
                collectionTresor = new HashMap<>();
                pileInondation = new ArrayList<>();
                defausseInondation = new ArrayList<>();
                pileTresor = new ArrayList<>();
                defausseTresor = new ArrayList<>();
                pileAventurier = new ArrayList<>();
                joueurs = new HashMap<>();
                
                // === INSTALLATION (cf. règles du jeu) ========================
                
                // 1. Créer l'Île Interdite :
                grille = new Grille();
                
                // 2. Placer les Trésors :
                for (Utils.Tresor tresor : Utils.Tresor.values()) {
                    collectionTresor.put(tresor, Boolean.FALSE);
                }
                
                // 3. Séparer les cartes :
                    // Pile "Inondation" :
                for (String args : Parameters.tuilesJeu) {
                    String parts[] = args.split(","); // cf. Parameters
                    String nomCarteI = parts[0];
                    pileInondation.add(new CarteInondation(nomCarteI));
                }
                Utils.melangerInondation(pileInondation);
                
                    // Pile "Tresor" (cf. Materiel) :
                for (Tresor tresor : Tresor.values()) {
                    for (int i = 0; i < 5; i++) {
                        pileTresor.add(new CarteButin(tresor)); // 5 cartes / trésor
                    }
                }
                for (int i = 0; i < 3; i++) {
                    pileTresor.add(new MonteeEau()); // 3 cartes montée des eaux
                    pileTresor.add(new Helicoptere()); // 3 cartes hélicoptère
                }
                pileTresor.add(new SacSable());
                pileTresor.add(new SacSable()); // 2 cartes sacs de sable
                Utils.melangerTresor(pileTresor);
                
                    // Pile "Aventurier"
                pileAventurier.add(new Explorateur());
                pileAventurier.add(new Ingenieur());
                pileAventurier.add(new Messager());
                pileAventurier.add(new Navigateur());
                pileAventurier.add(new Pilote());
                pileAventurier.add(new Plongeur());
                Utils.melangerAventuriers(pileAventurier);
                
                // 4. L'Île commence à sombrer :
                for (int i = 0; i < 6; i++) {
                    this.tirerCarteInondation(); // 6 fois
                }
                
                // 5. Les aventuriers débarquent :
                    // Création des aventuriers...
                for (int i = 0; i < nbJoueurs; i++) {
                    String nomAventurier = listeJoueurs[i];
                    CarteAventurier role = pileAventurier.get(i);
                    Aventurier nouveauJoueur = new Aventurier(nomAventurier, role);
                    role.setJoueur(nouveauJoueur);
                    // Détermination de la position du futur joueur...
                    Pion pionJoueur = nouveauJoueur.getPion();
                    int[] posJoueur = grille.getPosFromTuile(grille.getTuileFromSpawnPion(pionJoueur));
                    this.addPosition(nouveauJoueur, posJoueur[0], posJoueur[1]);
                    
                    joueurs.put(nomAventurier, nouveauJoueur);
                }
                
                // 6. Distribuer les cartes Trésor :
                for (String nomJoueur : listeJoueurs) {
                    Aventurier joueur = joueurs.get(nomJoueur);
                    while (joueur.getNbCartes() < 2) {
                        this.tirerCarteTresorDemarrage(joueur); // comportement différent entre au début et pendant le jeu
                    }
                }
                
                // 7. Déterminer du niveau d'eau :
                niveauDEau = new NivEau(difficulte); // cf. Constructeur + méthode NivEau
                
                // + Finalisation + //
                plateauJeu = new PlateauJeu(grille, listeJoueurs); // plateau de TEST
                plateauJeu.addObservateur(this);
                plateauJeu.afficher();
                
                for (String nomJoueur : listeJoueurs) { // création des vues des aventuriers
                    VueAventurier vueAventurier = new VueAventurier(joueurs.get(nomJoueur), listeJoueurs);
                    vueAventurier.addObservateur(this);
                    vueAventurier.desactiver();
                    vueAventurier.afficher();
                }
                
                VueAventurier.vuesAventuriers.get(listeJoueurs[indexOrdre]).activer(); // pour le joueur qui commence
                
                System.out.println("INITIALISATION PARTIE TERMINEE !");
                this.verifEtatPartie(); // test
                break;
                
            // === ACTIONS ==================
            case DONNER_CARTE:
                destinateur = joueurs.get(msg.destinateur);
                Aventurier destinataire = joueurs.get(msg.destinataire);
                
                destinateur.donnerCarte(destinataire, msg.nomCarteT);
                
                this.verifDeckTresorJoueurs(); // test
                break;
                
            case SE_DEPLACER:
                destinateur = joueurs.get(msg.destinateur);
                posL = msg.posL;
                posC = msg.posC;
                
                destinateur.seDeplacer(posL, posC);
                
                this.verifEtatJoueurs(); // test
                break;
                
            case ASSECHER:
                destinateur = joueurs.get(msg.destinateur);
                posL = msg.posL;
                posC = msg.posC;
                
                destinateur.assecherTuile(posL, posC);
                
                this.verifTuilesInondees(); // test
                this.verifEtatJoueurs(); // test
                break;
                
            case FINIR_TOUR:
                destinateur = joueurs.get(msg.destinateur);
                // 1. Les 3 actions max ont été faites... (PA = 0 ou "TERMINER TOUR")
                
                // 2. Tirer 2 cartes trésor :
                this.tirerCarteTresor(destinateur);
                this.tirerCarteTresor(destinateur);
                
                // 3. tirer 2 cartes Inondation :
                for (int i = 0; i < niveauDEau.getWaterLevel(); i++) {
                    this.tirerCarteInondation();
                }
                
                // Passage du tour de jeu :
                VueAventurier.vuesAventuriers.get(listeJoueurs[indexOrdre]).initJDialogDonnerCarte(); // actualisé puisqu'il a pioché 2 cartes
                VueAventurier.vuesAventuriers.get(listeJoueurs[indexOrdre]).desactiver(); // plus son tour de jeu
                    // détermination du joueur suivant :
                this.joueurSuivant();
                VueAventurier.vuesAventuriers.get(listeJoueurs[indexOrdre]).activer(); // activation du joueur suivant
                
                destinateur.reinitialiserPA(); // on remet les PA du joueur précédent à 3
                
                this.verifEtatPartie(); // test
        }
    }
    
    // === UTILITAIRE ==========================================================
    
    private void tirerCarteInondation() {
        CarteInondation carteTiree = pileInondation.remove(0);
        String nomTuile = carteTiree.getNomCarteI();
        grille.getTuileFromName(nomTuile).inonderTuile();
        if (grille.getTuileFromName(nomTuile).getEtat() != EtatTuile.COULEE) {
            defausseInondation.add(carteTiree);
        } // sinon, elle est retirée définitivement du jeu.
        //
        // La nage d'urgence vers une carte adjacente et donc le game over ne sont pas encore traité...
        //
        // cas où il n'y a plus de cartes dans la pile :
        if (pileInondation.isEmpty()) {
            Utils.melangerInondation(defausseInondation); // on mélange la défausse...
            for (int i = 0; i < defausseInondation.size(); i++) {
                CarteInondation carte = defausseInondation.remove(0);
                pileInondation.add(carte); // ... puis on la remet dans la pile.
            }
        }
    }
    
    // Les 2 premières cartes du démarrage
    private void tirerCarteTresorDemarrage(Aventurier aventurier) {
        CarteTresor carteTiree = pileTresor.remove(0);
        while (carteTiree.getNomCarteT().equals("Montée des Eaux")) { // cf. Règles
            pileTresor.add(carteTiree);
            Utils.melangerTresor(pileTresor);
            carteTiree = pileTresor.remove(0);
        }
        aventurier.addCarteTresor(carteTiree);
    }
    
    // à la fin de chaque tour
    private void tirerCarteTresor(Aventurier aventurier) {
        // l'utilisation des cartes, n'étant pas encore faite, on décide de ne pas
        // faire piocher le joueur si son deck est plein...
        if (!aventurier.hasFullDeck()) { // temporaire
            CarteTresor carteTiree = pileTresor.remove(0);
            if (carteTiree.getNomCarteT().equals("Montée des Eaux")) {
                // Monter le niveau d'eau d'un cran :
                niveauDEau.monterNiveau();
                // Mélanger + Remettre défausse Trésor :
                if (!defausseInondation.isEmpty()) {
                    Utils.melangerInondation(defausseInondation);
                    for (int i = 0; i < defausseInondation.size(); i++) {
                        CarteInondation carte = defausseInondation.remove(0);
                        pileInondation.add(0, carte); // on remet dans la pile, AU DESSUS !
                    }
                }
                // Mettre carte Montée des eaux dans la défausse :
                defausseTresor.add(carteTiree);
            } else {
                aventurier.addCarteTresor(carteTiree);
            }
        }
        // Quand il n'y a plus de cartes dans la pile Trésor...
        if (pileTresor.isEmpty()) {
            Utils.melangerTresor(defausseTresor);
            for (int i = 0; i < defausseTresor.size(); i++) {
                CarteTresor carte = defausseTresor.remove(0);
                pileTresor.add(carte);
            }
        }
    }
    
    private void addPosition(Aventurier aventurier, int ligne, int colonne) {
        Position position = new Position(grille, aventurier, ligne, colonne);
        grille.setPosJoueur(aventurier.getNomAventurier(), position);
        aventurier.setPosition(position);
    }
    
    private void joueurSuivant() {
        if (indexOrdre == listeJoueurs.length - 1) {
            indexOrdre = 0;
        } else {
            indexOrdre += 1;
        }
    }
    
    // === METHODES POUR LA PHASE DE TEST ======================================
    
    private void verifEtatPartie() {
        // 1. Créer l'île interdite :
        // cf. affichage du plateau de jeu test

        // 2. Placer les trésors :
        this.verifTresors();
        
        // 3. Séparer les cartes :
        this.verifPilesCarte();
        
        // 4. L'ile commence à sombrer :
        this.verifTuilesInondees();
        
        // 5. Les aventuriers débarquent :
        this.verifEtatJoueurs();
        
        // 6. distribuer les cartes Trésor :
        this.verifDeckTresorJoueurs();
        
        // 7. Déterminer le niveau d'eau :
        this.verifNiveauEau();
    }
    
    private void verifTresors() {
        System.out.println("Vérification de la collection de trésors : ");
        System.out.println("\tfalse : pas obtenu // true : obtenu");
        for (Utils.Tresor tresor : Utils.Tresor.values()) {
            System.out.println("\t" + tresor + " : " + collectionTresor.get(tresor));
        }
        System.out.println();
    }
    
    private void verifPilesCarte() {
        // Pile / Défausse Tresor :
        System.out.println("Vérification Pile TRESOR : ");
        for (CarteTresor carte : pileTresor) {
            System.out.println("\t- " + carte.getNomCarteT());
        }
        System.out.println(pileTresor.size() + " cartes.");
        System.out.println();
        
        System.out.println("Vérification Defausse TRESOR : ");
        for (CarteTresor carte : defausseTresor) {
            System.out.println("\t- " + carte.getNomCarteT());
        }
        System.out.println(defausseTresor.size() + " cartes.");
        System.out.println();
        
        // Pile / Défausse Inondation :
        System.out.println("Vérification Pile INONDATION : ");
        for (CarteInondation carte : pileInondation) {
            System.out.println("\t- " + carte.getNomCarteI());
        }
        System.out.println(pileInondation.size() + " cartes.");
        System.out.println();
        
        System.out.println("Vérification Defausse INONDATION : ");
        for (CarteInondation carte : defausseInondation) {
            System.out.println("\t- " + carte.getNomCarteI());
        }
        System.out.println(defausseInondation.size() + " cartes.");
        System.out.println();
        
        // Pile Aventurier :
        System.out.println("Vérification Pile AVENTURIER : ");
        for (CarteAventurier carte : pileAventurier) {
            System.out.print("\t- " + carte.getNomRole() + " - ");
            System.out.println(carte.getPion().toString());
        }
        System.out.println(pileAventurier.size() + " cartes.");
        System.out.println();
    }
    
    private void verifTuilesInondees() {
        System.out.println("Vérification des tuiles inondées :");
        Tuile tuile;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                tuile = grille.getTuile(i, j);
                if (tuile != null && tuile.getEtat() == Utils.EtatTuile.INONDEE) {
                    System.out.println("\t" + tuile.getNomTuile() + " - " + tuile.getEtat());
                }
            }
        }
        System.out.println();
    }
    
    private void verifTuilesCoulees() {
        System.out.println("Vérification des tuiles coulées :");
        Tuile tuile;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                tuile = grille.getTuile(i, j);
                if (tuile != null && tuile.getEtat() == Utils.EtatTuile.COULEE) {
                    System.out.println("\t" + tuile.getNomTuile() + " - " + tuile.getEtat());
                }
            }
        }
        System.out.println();
    }
    
    private void verifEtatJoueurs() {
        System.out.println("Vérification des joueurs :");
        for (String nomJoueur : listeJoueurs) {
            Aventurier aventurier = joueurs.get(nomJoueur);
            System.out.print("\t" + aventurier.getNomAventurier() + " - ");
            System.out.print(aventurier.getRole().getNomRole() + " - ");
            System.out.println(aventurier.getPion().toString());
            System.out.print("\t\tLigne : " + aventurier.getPosition().getLigne());
            System.out.println(" / Colonne : " + aventurier.getPosition().getColonne());
            System.out.println("\t\tPA : " + aventurier.getPointAction());
        }
        System.out.println();
    }
    
    private void verifDeckTresorJoueurs() {
        System.out.println("Vérification des decks des joueurs :");
        for (String nomJoueur : listeJoueurs) {
            Aventurier aventurier = joueurs.get(nomJoueur);
            System.out.println("\t" + aventurier.getNomAventurier() + " :");
            for (CarteTresor carte : aventurier.getDeckTresor()) {
                System.out.println("\t\t- " + carte.getNomCarteT());
            }
        }
        System.out.println();
    }
    
    private void verifNiveauEau() {
        System.out.println("Vérification du niveau d'eau :");
        System.out.println("\tdifficulte : " + difficulte);
        System.out.println("\tIndex : " + niveauDEau.getIndexLevel());
        System.out.println("\tNiveau d'eau : " + niveauDEau.getWaterLevel());
        System.out.println();
    }

    
}
