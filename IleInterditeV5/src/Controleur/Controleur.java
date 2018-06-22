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
import Util.Parameters;
import Util.Utils;
import Util.Utils.EtatTuile;
import Util.Utils.Pion;
import Util.Utils.Tresor;
import Vues.EcranPrincipal.EcranPrincipal;
import Vues.PlateauJeu.FinPartie;
import Vues.PlateauJeu.PlateauJeu;
import Vues.PlateauJeu.TuileGraphique;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.border.LineBorder;

/**
 *
 * @author Aymerick_PC
 */
public class Controleur implements Observateur {

    // Variables Message
    private int nbJoueurs;
    private ArrayList<String> listeJoueurs;
    private String difficulte;

    // Vues
    private final EcranPrincipal ecranPrincipal;
    private PlateauJeu plateauJeu;
    private FinPartie fin;
    // Modèle :
    private Grille grille;
    private int indexOrdre = 0;

    private HashMap<Tresor, Boolean> collectionTresor;

    private ArrayList<CarteInondation> pileInondation;
    private ArrayList<CarteInondation> defausseInondation;
    private ArrayList<CarteTresor> pileTresor;
    private ArrayList<CarteTresor> defausseTresor;
    private ArrayList<CarteAventurier> pileAventurier;

    private HashMap<String, Aventurier> joueurs;

    private NivEau niveauDEau;

    private Aventurier joueurDeplace;
    // Variables utilitaires :
    private boolean mode_carte = false;
    private boolean actPrecAss = false;

    public Controleur() {
        ecranPrincipal = new EcranPrincipal();
        ecranPrincipal.addObservateur(this);
        ecranPrincipal.afficher();
        fin = new FinPartie(false);
        fin.addObservateur(this);
    }

    @Override
    public void traiterMessage(Message msg) {
        switch (msg.type) {
            case DEMARRER_PARTIE:
                ecranPrincipal.fermer();

                // récupération des données message :
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
                plateauJeu = new PlateauJeu(nbJoueurs);
                plateauJeu.addObservateur(this);

                plateauJeu.updateGrille(grille, listeJoueurs);
                plateauJeu.updateCurrentPlayer(joueurActuel(), niveauDEau.getIndexLevel());
                plateauJeu.updateDefausseTresor(defausseTresor);
                plateauJeu.updateDefausseInondation(defausseInondation);
                plateauJeu.updateJ1(joueurs.get(listeJoueurs.get(0)));
                plateauJeu.updateJ2(joueurs.get(listeJoueurs.get(1)));
                if (nbJoueurs >= 3) {
                    plateauJeu.updateJ3(joueurs.get(listeJoueurs.get(2)));
                }
                if (nbJoueurs == 4) {
                    plateauJeu.updateJ4(joueurs.get(listeJoueurs.get(3)));
                }

                plateauJeu.afficher();
                break;

            case AFFICHER_CASES_DEPLACEMENT:
                plateauJeu.updateGrille(grille, listeJoueurs);
                ArrayList<Tuile> tuilesDep;
                tuilesDep = null;
                if (!msg.cardMode) {
                    tuilesDep = joueurActuel().getTuilesDeplacement();
                } else {
                    if (!prendreHelicoptere(joueurActuel())) {
                        tuilesDep = grille.getTuilesInondeesAssechees();
                        this.setModeCarte(true);
                    }
                }
                for (Tuile tuile : tuilesDep) {
                    int[] pos = grille.getPosFromTuile(tuile);
                    TuileGraphique tg = plateauJeu.getTuileGraphique(pos[0], pos[1]);
                    plateauJeu.activerTuile(tg, "déplacer");
                    tg.setBorder(new LineBorder(Color.GREEN, 6));
                }
                plateauJeu.refresh();
                break;

            case SE_DEPLACER:
                if (!mode_carte) {
                    joueurActuel().seDeplacer(msg.posL, msg.posC);
                    joueurActuel().utiliserPA();
                } else {
                    System.out.println(joueurDeplace.getNomAventurier());
                    joueurDeplace.seDeplacer(msg.posL, msg.posC);
                    this.setModeCarte(false);
                    defausseTresor.add(joueurActuel().removeOccurenceCarte("Helicoptere"));
                    plateauJeu.updateDefausseTresor(defausseTresor);
                }
                plateauJeu.updateGrille(grille, listeJoueurs);
                plateauJeu.updateCurrentPlayer(joueurActuel(), niveauDEau.getIndexLevel());
                plateauJeu.refresh();
                break;

            case JETER_CARTE:
                this.jeterCarte(joueurActuel(), joueurActuel().getCarteTresorFromName(msg.nomCarteT));
                plateauJeu.updateCurrentPlayer(joueurActuel(), niveauDEau.getIndexLevel());
                plateauJeu.refresh();
                plateauJeu.updateGrille(grille, listeJoueurs);
                plateauJeu.updateDefausseTresor(defausseTresor);
                break;

            case AFFICHER_CASES_ASSECHEMENT:
                plateauJeu.updateGrille(grille, listeJoueurs);
                ArrayList<Tuile> tuilesAss;
                if (!msg.cardMode) {
                    tuilesAss = joueurActuel().getTuilesAssechement();
                } else {
                    tuilesAss = grille.getTuilesInondees();
                    this.setModeCarte(true);
                }
                for (Tuile tuile : tuilesAss) {
                    int[] pos = grille.getPosFromTuile(tuile);
                    TuileGraphique tg = plateauJeu.getTuileGraphique(pos[0], pos[1]);
                    plateauJeu.activerTuile(tg, "assécher");
                    tg.setBorder(new LineBorder(Color.CYAN, 6));
                }
                plateauJeu.refresh();
                break;

            case ASSECHER:
                joueurActuel().assecherTuile(msg.posL, msg.posC);
                joueurActuel().utiliserPA();
                if (!mode_carte) {
                    if (joueurActuel().getRole().getNomRole().equals("Ingénieur")) {
                        if (!actPrecAss) {
                            if (!joueurActuel().getTuilesAssechement().isEmpty()) {
                                Message m = new Message();
                                m.type = TypesMessages.AFFICHER_CASES_ASSECHEMENT;
                                m.cardMode = false;
                                traiterMessage(m);
                                actPrecAss = true;
                            }

                        } else {
                            actPrecAss = false;
                            plateauJeu.updateGrille(grille, listeJoueurs);
                            plateauJeu.updateCurrentPlayer(joueurActuel(), niveauDEau.getIndexLevel());
                            plateauJeu.refresh();
                        }

                    } else {
                        joueurActuel().utiliserPA();
                        plateauJeu.updateGrille(grille, listeJoueurs);
                        plateauJeu.updateCurrentPlayer(joueurActuel(), niveauDEau.getIndexLevel());
                        plateauJeu.refresh();
                    }

                } else {
                    this.setModeCarte(false);
                    defausseTresor.add(joueurActuel().removeOccurenceCarte("Sacs de Sable"));
                    plateauJeu.updateDefausseTresor(defausseTresor);
                    plateauJeu.updateGrille(grille, listeJoueurs);
                    plateauJeu.updateCurrentPlayer(joueurActuel(), niveauDEau.getIndexLevel());
                    plateauJeu.refresh();
                }
                break;

            case DONNER_CARTE:
                //highlight les joueur pouvant recevoir une carte+ add actionlistener
                //
                break;

            case GAGNER_TRESOR:
                Tresor t = joueurActuel().getRole().gagnerTresor();
                if (t != null) {
                    collectionTresor.remove(t);
                    collectionTresor.put(t, Boolean.TRUE);
                }
                break;

            case FINIR_TOUR:
                // 1. Les 3 actions max ont été faites... (PA = 0 ou "TERMINER TOUR")
                // 2. Tirer 2 cartes trésor :
                this.tirerCarteTresor(joueurActuel());
                this.tirerCarteTresor(joueurActuel());
                // 3. tirer cartes Inondation en fonction du niveau d'eau :
                for (int i = 0; i < niveauDEau.getWaterLevel(); i++) {
                    this.tirerCarteInondation();
                }

                // nage d'urgence à compléter...
                deplacementCoulee();//deplace les joueurs qui se trouvent sur une case coulee

                // au tour du joueur suivant :
                this.joueurActuel().reinitialiserPA(); // on remet les PA du joueur à 3 pour la prochaine fois
                this.nextPlayer();

                plateauJeu.updateGrille(grille, listeJoueurs);
                plateauJeu.updateCurrentPlayer(joueurActuel(), niveauDEau.getIndexLevel());
                plateauJeu.updateDefausseTresor(defausseTresor);
                plateauJeu.updateDefausseInondation(defausseInondation);
                plateauJeu.refresh();

                Perdu();
                break;

            case JOUEUR:
                System.out.println(msg.destinataire);
                if (mode_carte) {//helicoptere

                    joueurDeplace = joueurs.get(msg.destinataire);
                    Message m = new Message();
                    m.type = TypesMessages.AFFICHER_CASES_DEPLACEMENT;
                    m.cardMode = true;
                    traiterMessage(m);

                } else {//donnercarte

                }
                break;

            case HELICOPTERE:
                mode_carte = true;
                break;
            case ABANDONNER:
                plateauJeu.fermer();
                ecranPrincipal.afficher();
                fin.afficher(false);
                break;
        }
    }

    // === UTILITAIRE ==========================================================
    private void tirerCarteInondation() {
        CarteInondation carteTiree = pileInondation.remove(0);
        Tuile tuile = grille.getTuileFromName(carteTiree.getNomCarteI());
        tuile.inonderTuile();
        if (tuile.getEtat() != EtatTuile.COULEE) {
            defausseInondation.add(carteTiree);
        }

        // traitement du Game Over...
        if (pileInondation.isEmpty()) {
            Utils.melangerInondation(defausseInondation);
            for (int i = 0; i < defausseInondation.size(); i++) {
                pileInondation.add(defausseInondation.remove(0));
            }
        }
    }

    private void addPosition(Aventurier aventurier, int ligne, int colonne) {
        Position pos = new Position(grille, aventurier, ligne, colonne);
        grille.setPosJoueur(aventurier.getNomAventurier(), pos);
        aventurier.setPosition(pos);
    }

    private void tirerCarteTresorDemarrage(Aventurier aventurier) {
        CarteTresor carteTiree = pileTresor.remove(0);
        while (carteTiree.getNomCarteT().equals("Montée des Eaux")) {
            pileTresor.add(carteTiree);
            Utils.melangerTresor(pileTresor);
            carteTiree = pileTresor.remove(0);
        }
        aventurier.addCarteTresor(carteTiree);
    }

    private void tirerCarteTresor(Aventurier aventurier) {
        CarteTresor carteTiree = pileTresor.remove(0);
        if (carteTiree.getNomCarteT().equals("Montée des Eaux")) {
            // Monter le niveau d'eau d'un cran :
            niveauDEau.monterNiveau();
            plateauJeu.updateCurrentPlayer(joueurActuel(), niveauDEau.getIndexLevel());
            // Mélanger + Remettre défausse Trésor :
            if (!defausseInondation.isEmpty()) {
                Utils.melangerInondation(defausseInondation);
                for (int i = 0; i < defausseInondation.size(); i++) {
                    pileInondation.add(0, defausseInondation.remove(0)); // on remet dans la pile, AU DESSUS !
                }
            }
            // Mettre carte Montée des Eaux dans la défausse :
            defausseTresor.add(carteTiree);
        } else {
            aventurier.addCarteTresor(carteTiree);
        }
        // Quand il n'y a plus de cartes dans la pile Trésor...
        if (pileTresor.isEmpty()) {
            Utils.melangerTresor(defausseTresor);
            for (int i = 0; i < defausseTresor.size(); i++) {
                CarteTresor carte = defausseTresor.remove(0);
                pileTresor.add(carte);
            }
        }
        if (aventurier.getNbCartes() > 5) {

        }

    }

    public void jeterCarte(Aventurier aventurier, CarteTresor carte) {
        aventurier.getDeckTresor().remove(carte);
        defausseTresor.add(carte);
    }

    private void setModeCarte(boolean b) {
        this.mode_carte = b;
    }

    private Aventurier joueurActuel() {
        return joueurs.get(listeJoueurs.get(indexOrdre));
    }

    private void nextPlayer() {
        if (indexOrdre == listeJoueurs.size() - 1) {
            indexOrdre = 0;
        } else {
            indexOrdre += 1;
        }
    }

    // === VERIFICATIONS =======================================================
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
        if (flag) {
            finirPartie(false);
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

    public boolean prendreHelicoptere(Aventurier joueur) {
        boolean toutLesTresors = true;

        for (Tresor t : collectionTresor.keySet()) {
            if (collectionTresor.get(t).equals(false)) {
                toutLesTresors = false;
            }
        }

        if (joueur.getRole().getJoueursTuile().size() == nbJoueurs - 1 && toutLesTresors == true) {
            finirPartie(true);
            return true;
        }
        return false;
    }

    public void finirPartie(boolean b) {
        fin.setEtat(b);
        fin.afficher(true);
    }

}
