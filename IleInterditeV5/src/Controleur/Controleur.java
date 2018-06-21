/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controleur;

import MVC.Message;
import MVC.Observateur;
import MVC.TypesMessages;
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
import Musique.SonLauncher;
import Util.Parameters;
import Util.Utils;
import Util.Utils.EtatTuile;
import Util.Utils.Pion;
import Util.Utils.Tresor;
import Vues.EcranPrincipal1.EcranPrincipal;
import Vues.PlateauJeu1.PlateauJeu;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Aymerick
 */
public class Controleur implements Observateur {
    // Message
    // DEMARRER_PARTIE:

    private int nbJoueurs;
    private ArrayList<String> listeJoueurs;
    private int indexOrdre = 0;
    private String difficulte;
    private String actionCourante;
    private boolean actionPrecAss = false;
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
    private SonLauncher son;

    public Controleur() {
        ecranPrincipal = new EcranPrincipal();
        ecranPrincipal.addObservateur(this);
        ecranPrincipal.afficher();
        son = new SonLauncher();
        //son.playSound();
        son.music();
    }

    @Override
    public void traiterMessage(Message msg) {
        Aventurier destinateur;
        int posL, posC;

        switch (msg.type) {
            case DEMARRER_PARTIE:
                son.stopSound();
                ecranPrincipal.fermer();

                // récupération des données Message
                listeJoueurs = msg.listeJoueurs;
                difficulte = msg.difficulte;
                nbJoueurs = listeJoueurs.size();

                // Initialisation des Collections :
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

                // 2. Placer les trésors :
                for (Tresor tresor : Tresor.values()) {
                    collectionTresor.put(tresor, Boolean.FALSE);
                }

                // 3. Séparer les cartes :
                // Carte Inondation
                for (String args : Parameters.tuilesJeu) {
                    String parts[] = args.split(",");
                    String nomCarteI = parts[0];
                    pileInondation.add(new CarteInondation(nomCarteI));
                }
                Utils.melangerInondation(pileInondation);

                // Carte Tresor
                for (Tresor tresor : Tresor.values()) {
                    for (int i = 0; i < 5; i++) {
                        pileTresor.add(new CarteButin(tresor));
                    }
                }
                for (int i = 0; i < 3; i++) {
                    pileTresor.add(new MonteeEau());
                    pileTresor.add(new Helicoptere());
                }
                pileTresor.add(new SacSable());
                pileTresor.add(new SacSable());
                Utils.melangerTresor(pileTresor);

                // Carte Aventurier
                pileAventurier.add(new Explorateur());
                pileAventurier.add(new Ingenieur());
                pileAventurier.add(new Messager());
                pileAventurier.add(new Navigateur());
                pileAventurier.add(new Pilote());
                pileAventurier.add(new Plongeur());
                Utils.melangerAventuriers(pileAventurier);

                // 4. L'Île commence à sombrer :
                for (int i = 0; i < 6; i++) {
                    this.tirerCarteInondation();
                }

                // 5. Les aventuriers débarquent :
                // Création des aventuriers :
                for (int i = 0; i < nbJoueurs; i++) {
                    String nomAventurier = listeJoueurs.get(i);
                    CarteAventurier role = pileAventurier.get(i);
                    Aventurier nouveauJoueur = new Aventurier(nomAventurier, role);
                    role.setJoueur(nouveauJoueur);

                    Pion pionJoueur = nouveauJoueur.getPion();
                    int[] posJoueur = grille.getPosFromTuile(grille.getTuileFromSpawnPion(pionJoueur));
                    this.addPosition(nouveauJoueur, posJoueur[0], posJoueur[1]);

                    joueurs.put(nomAventurier, nouveauJoueur);
                }

                // 6. Distribuer les cartes Trésor :
                for (String nomJoueur : listeJoueurs) {
                    Aventurier joueur = joueurs.get(nomJoueur);
                    while (joueur.getNbCartes() < 2) {
                        this.tirerCarteTresorDemarrage(joueur);
                    }
                }

                // 7. Déterminer le niveau d'eau :
                niveauDEau = new NivEau(difficulte);

                // 8. Finalisation :
                plateauJeu = new Vues.PlateauJeu1.PlateauJeu(nbJoueurs);
                plateauJeu.addObservateur(this);
                plateauJeu.initGrille(grille, listeJoueurs);
                plateauJeu.updateCurrentPlayer(joueurs.get(listeJoueurs.get(indexOrdre)));
                plateauJeu.updatePileTresor(defausseTresor);
                plateauJeu.updatePileInondation(defausseInondation);
                plateauJeu.updateJ1(joueurs.get(listeJoueurs.get(0)));
                plateauJeu.updateJ2(joueurs.get(listeJoueurs.get(1)));
                if (nbJoueurs >= 3) {
                    plateauJeu.updateJ3(joueurs.get(listeJoueurs.get(2)));
                }
                if (nbJoueurs == 4) {
                    plateauJeu.updateJ4(joueurs.get(listeJoueurs.get(3)));
                }
                plateauJeu.afficher();

                System.out.println("INITIALISATION PARTIE TERMINEE !");
                this.verifEtatPartie(); // test
                break;

            // === ACTIONS ==================
            case DONNER_CARTE:
                //penser à donner carte
                destinateur = joueurs.get(msg.destinateur);
                Aventurier destinataire = joueurs.get(msg.destinataire);
                if (destinateur.donnerCarte(destinataire, msg.nomCarteT)) {
                    destinateur.utiliserPA();
                }
                this.verifDeckTresorJoueurs(); // test
                break;

            case AFFICHER_CASES_DEPLACEMENT:
                for (String s : joueurCourant().getRole().getJoueursTuile()) {
                    System.out.println(s);
                }
                plateauJeu.resetHlight();
                for (Tuile t : joueurCourant().getTuilesDeplacement()) {
                    int[] pos = joueurCourant().getGrille().getPosFromTuile(t);
                    plateauJeu.getTuileGraphique(pos[0], pos[1]).setHlight(joueurCourant().getPion().getCouleur());
                }
                actionCourante = "deplacement";
                break;

            case AFFICHER_CASES_ASSECHEMENT:
                plateauJeu.resetHlight();
                for (Tuile t : joueurCourant().getTuilesAssechement()) {
                    int[] pos = joueurCourant().getGrille().getPosFromTuile(t);
                    plateauJeu.getTuileGraphique(pos[0], pos[1]).setHlight(joueurCourant().getPion().getCouleur());
                }
                actionCourante = "assechement";
                break;

            case POSITION:
                plateauJeu.resetHlight();
                switch (actionCourante) {
                    case "deplacement":
                        if (joueurCourant().getPointAction() > 0) {
                            if (joueurCourant().seDeplacer(msg.posL, msg.posC)) {
                                joueurCourant().utiliserPA();
                            }
                        }
                        break;

                    case "assechement":
                        if (joueurCourant().getRole().getNomRole().equals("Ingénieur")) {
                            if (joueurCourant().getPointAction() > 0) {
                                if (joueurCourant().assecherTuile(msg.posL, msg.posC)) {
                                    if (!actionPrecAss) {
                                        if (!joueurCourant().getTuilesAssechement().isEmpty()) {
                                            for (Tuile t : joueurCourant().getTuilesAssechement()) {
                                                int[] pos = joueurCourant().getGrille().getPosFromTuile(t);
                                                plateauJeu.getTuileGraphique(pos[0], pos[1]).setHlight(joueurCourant().getPion().getCouleur());
                                            }
                                            actionPrecAss = true;
                                        } else {
                                            joueurCourant().utiliserPA();
                                        }
                                    } else {
                                        joueurCourant().utiliserPA();
                                        actionPrecAss = false;
                                    }
                                }
                            }
                        } else {
                            if (joueurCourant().getPointAction() > 0) {
                                if (joueurCourant().assecherTuile(msg.posL, msg.posC)) {
                                    joueurCourant().utiliserPA();
                                }
                            }
                        }
                        break;
                    case "sacSable":
                        joueurCourant().assecheSacSable(msg.posL, msg.posC);
                        break;
                    case "helicoptere":
//                        joueurCourant().utiliserHelicoptere(,posL, msg.posC);
                }
                break;

            case CARTE_SABLE:
                plateauJeu.resetHlight();
                for (Tuile t : grille.getToutesTuilesInondees()) {
                    int[] pos = joueurCourant().getGrille().getPosFromTuile(t);
                    plateauJeu.getTuileGraphique(pos[0], pos[1]).setHlight(joueurCourant().getPion().getCouleur());
                }
                actionCourante = "sacSable";
                break;

            case CARTE_HELICOPTERE://ajouter gagner
                plateauJeu.resetHlight();
                if (!prendreHelicoptere(joueurCourant())) {
                    for (Tuile t : grille.getToutesTuilesInondeesAssechees()) {
                        int[] pos = joueurCourant().getGrille().getPosFromTuile(t);
                        plateauJeu.getTuileGraphique(pos[0], pos[1]).setHlight(joueurCourant().getPion().getCouleur());
                    }
                    actionCourante = "helicoptere";
                }

                break;
            case FINIR_TOUR:
                plateauJeu.resetHlight();
                destinateur = joueurCourant();

                // 1. Les 3 actions max ont été faites... (PA = 0 ou "TERMINER TOUR")
                // 2. Tirer 2 cartes trésor :
                this.tirerCarteTresor(destinateur);
                this.tirerCarteTresor(destinateur);
                // 3. tirer 2 cartes Inondation :
                for (int i = 0; i < niveauDEau.getWaterLevel(); i++) {
                    this.tirerCarteInondation();
                }
                deplacementCoulee();//deplace les joueurs qui se trouvent sur une case coulee

                // Passage du tour de jeu :
                //afficher panel joueur
                // détermination du joueur suivant :
                this.joueurSuivant();
                //VueAventurier.vuesAventuriers.get(listeJoueurs[indexOrdre]).activer(); // activation du joueur suivant

                destinateur.reinitialiserPA(); // on remet les PA du joueur précédent à 3

                plateauJeu.updateCurrentPlayer(joueurCourant());
                plateauJeu.updatePileTresor(defausseTresor);
                plateauJeu.updatePileInondation(defausseInondation);
                plateauJeu.update();
                this.verifEtatPartie(); // test
        }

        //verifier si la partie est perdue
        if (Perdu()) {
            finirPartie("perdu");
        }
        //passer un tour si le joueur n'a plus de PA
        if (joueurCourant().getPointAction() == 0) {
            Message m = new Message();
            m.type = TypesMessages.FINIR_TOUR;
            m.destinateur = joueurCourant().getNomAventurier();
            traiterMessage(m);
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
        if (indexOrdre == listeJoueurs.size() - 1) {
            indexOrdre = 0;
        } else {
            indexOrdre += 1;
        }
    }

    public Aventurier joueurCourant() {
        return joueurs.get(listeJoueurs.get(indexOrdre));
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

    public boolean Perdu() {//rajouter message
        boolean flag = false;
        //si les 2 tuile d'un trésor sombrent avant d'avoir recup le trésor
        for (Tresor t : collectionTresor.keySet()) {
            if (!collectionTresor.get(t)) {
                switch (t) {
                    case PIERRE:
                        if (grille.getTuileFromName("Le Temple de la Lune").getEtat() == EtatTuile.COULEE && grille.getTuileFromName("Le Temple du Soleil").getEtat() == EtatTuile.COULEE) {
                            flag = true;
                        }
                        break;
                    case STATUE:
                        if (grille.getTuileFromName("Le Jardin des Murmures").getEtat() == EtatTuile.COULEE && grille.getTuileFromName("Le Jardin des Hurlements").getEtat() == EtatTuile.COULEE) {
                            flag = true;
                        }
                        break;
                    case CRISTAL:
                        if (grille.getTuileFromName("La Caverne du Brasier").getEtat() == EtatTuile.COULEE && grille.getTuileFromName("La Caverne des Ombres").getEtat() == EtatTuile.COULEE) {
                            flag = true;
                        }
                        break;
                    case CALICE:
                        if (grille.getTuileFromName("Le Palais de Corail").getEtat() == EtatTuile.COULEE && grille.getTuileFromName("Le Palais des Marees").getEtat() == EtatTuile.COULEE) {
                            flag = true;
                        }
                        break;
                }
            }
        }

        //si l'heliport sombre
        if (grille.getTuileFromName("Heliport").getEtat() == EtatTuile.COULEE) {
            flag = true;
        }

        //si un pion sur une tuile coulee ne peut pas nager sur une autre tuile 
        for (String s : joueurs.keySet()) {
            Aventurier j = joueurs.get(s);
            if (j.getTuile().getEtat() == EtatTuile.COULEE) {
                if (j.getTuilesDeplacement().isEmpty()) {
                    flag = true;
                }
            }
        }
        //si le marqueur atteint la mort
        if (niveauDEau.getWaterLevel() == 0) {
            flag = true;
        }
        return flag;
    }

    public void deplacementCoulee() {
        for (String nom : joueurs.keySet()) {
            Aventurier a = joueurs.get(nom);
            if (a.getTuile().getEtat() == EtatTuile.COULEE && !a.getTuilesDeplacement().isEmpty()) {
                a.seDeplacer(grille.getPosFromTuile(a.getTuilesDeplacement().get(0))[0], grille.getPosFromTuile(a.getTuilesDeplacement().get(0))[1]);
            }
        }
    }
    //carte helicoptère    
    //public Boolean Gagne(){

    public boolean prendreHelicoptere(Aventurier joueur) {
        boolean toutLesTresors = true;

        for (Tresor t : collectionTresor.keySet()) {
            if (collectionTresor.get(t).equals(false)) {
                toutLesTresors = false;
            }
        }

        if (joueur.getRole().getJoueursTuile().size() == nbJoueurs - 1 && toutLesTresors == true) {
            finirPartie("gagner");
            return true;
        }
        return false;
    }

    public void finirPartie(String resultat) {
        if (resultat.equals("gagner")) {
            System.out.println("Vous avez gagné !");
        } else {
            System.out.println("Vous avez perdu !");
        }

    }
}
