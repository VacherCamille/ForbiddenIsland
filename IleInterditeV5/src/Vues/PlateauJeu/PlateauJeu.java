/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vues.PlateauJeu;

import MVC.Message;
import MVC.Observe;
import MVC.TypesMessages;
import Modele.Aventurier.Aventurier;
import Modele.CarteTresor.CarteTresor;
import Modele.Divers.CarteInondation;
import Modele.Plateau.Grille;
import Vues.EcranPrincipal.JPanelBackground;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Aymerick
 */
public class PlateauJeu extends Observe {

    private JFrame window;

    // VARIABLES (GRILLE DE JEU) :
    private JPanel panelGrille;

    // VARIABLES (JOUEUR COURANT) :
    private JPanelBackground panelCP;
    private JPanel panelGauche;
    private JPanel panelCarte;
    private JPanel panelDroite;
    private JPanel panelAction;
    private BoutonPerso deplacer;
    private BoutonPerso assecher;
    private BoutonPerso donnerCarte;
    private BoutonPerso gagnerTresor;
    private BoutonPerso abandonner;
    private JPanel panelAb;
    private JLabel labelAbandonner;
    private BoutonPerso finirTour;
    private JLabel labelFinirTour;
    private BoutonPerso actionSpeciale;

    // VARIABLES (JOUEUR 1 - JOUEUR 3 - CT)
    private JPanelBackground panelLeft;
    private JPanel panelJ1H, panelJ1F;
    private JPanel panelJ3H, panelJ3F;
    private JPanel panelTresor;

    // VARIABLES (JOUEUR 2 - JOUEUR 4 - CI)
    private JPanelBackground panelRight;
    private JPanel panelJ2H, panelJ2F;
    private JPanel panelJ4H, panelJ4F;
    private JPanel panelInondation;
    private JLabel labelNomJ1, labelNomJ2, labelNomJ3, labelNomJ4;
    private JLabel labelRoleJ1, labelRoleJ2, labelRoleJ3, labelRoleJ4;

    // VARIABLES LISTENERS :
    MouseListener mlBouton;
    MouseListener mlDeplacer;
    MouseListener mlAssecher;

    // VARIABLES UTILITAIRES :
    int nbJoueur;
    int nbDeck;
    private ArrayList<CarteTresor> deckCourant;
    private TuileGraphique[][] grilleGraphique = new TuileGraphique[6][6];

    public PlateauJeu(int nbJoueur) {
        this.nbJoueur = nbJoueur;

        window = new JFrame("L'île Interdite");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(1080, 720);
        window.setMinimumSize(new Dimension(720, 480));
        window.setLocationRelativeTo(null);
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // BackGround
        JPanelBackground panelMain = new JPanelBackground(JPanelBackground.PLATEAU_JEU);
        panelMain.setLayout(new BorderLayout(5, 5));

        // GRILLE DE JEU:
        panelGrille = new JPanel(new GridLayout(6, 6, 5, 5));
        panelGrille.setOpaque(false);
        panelMain.add(panelGrille, BorderLayout.CENTER);

        // HUD - JOUEUR COURANT ================================================
        panelCP = new JPanelBackground(JPanelBackground.CURRENT_PLAYER);
        panelMain.add(panelCP, BorderLayout.SOUTH);

        // HUD - J1 / J3 / CT ==================================================
        panelLeft = new JPanelBackground(JPanelBackground.PANEL_LEFT_RIGHT);
        panelLeft.setLayout(new GridLayout(5, 1, 5, 5));
        panelLeft.setOpaque(false);
        panelJ1H = new JPanel(new GridLayout(2, 1, 2, 2));
        panelJ1H.setOpaque(false);
        panelJ1F = new JPanel();
        panelJ1F.setOpaque(false);
        panelTresor = new JPanel(new GridLayout(1, 2, 2, 2));
        panelTresor.setOpaque(false);
        panelTresor.setBorder(new EmptyBorder(5, 5, 5, 5));
        panelJ3H = new JPanel(new GridLayout(2, 1, 2, 2));
        panelJ3H.setOpaque(false);
        panelJ3F = new JPanel();
        panelJ3F.setOpaque(false);

        panelLeft.add(panelJ1H);
        panelLeft.add(panelJ1F);
        panelLeft.add(panelTresor);
        panelLeft.add(panelJ3H);
        panelLeft.add(panelJ3F);
        panelMain.add(panelLeft, BorderLayout.WEST);

        // HUD - J2 / J4 / CI ==================================================
        panelRight = new JPanelBackground(JPanelBackground.PANEL_LEFT_RIGHT);
        panelRight.setLayout(new GridLayout(5, 1, 5, 5));
        panelRight.setOpaque(false);
        panelJ2H = new JPanel(new GridLayout(2, 1, 2, 2));
        panelJ2H.setOpaque(false);
        panelJ2F = new JPanel();
        panelJ2F.setOpaque(false);
        panelInondation = new JPanel(new GridLayout(1, 2, 2, 2));
        panelInondation.setOpaque(false);
        panelInondation.setBorder(new EmptyBorder(5, 5, 5, 5));
        panelJ4H = new JPanel(new GridLayout(2, 1, 2, 2));
        panelJ4H.setOpaque(false);
        panelJ4F = new JPanel();
        panelJ4F.setOpaque(false);

        panelRight.add(panelJ2H);
        panelRight.add(panelJ2F);
        panelRight.add(panelInondation);
        panelRight.add(panelJ4H);
        panelRight.add(panelJ4F);
        panelMain.add(panelRight, BorderLayout.EAST);

        // LISTENERS:
        this.redimension();
        this.initListener();

        panelJ1H.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                Message m = new Message();
                m.type = TypesMessages.JOUEUR;
                m.destinataire = "1";
                notifierObservateur(m);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        panelJ2H.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                Message m = new Message();
                m.type = TypesMessages.JOUEUR;
                m.destinataire = "2";
                notifierObservateur(m);

            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        panelJ3H.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                Message m = new Message();
                m.type = TypesMessages.JOUEUR;
                m.destinataire = "3";
                notifierObservateur(m);

            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        panelJ4H.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                Message m = new Message();
                m.type = TypesMessages.JOUEUR;
                m.destinataire = "4";
                notifierObservateur(m);

            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        window.add(panelMain);
    }

    // === UTILITAIRE ==========================================================
    public TuileGraphique getTuileGraphique(int ligne, int colonne) {
        return grilleGraphique[ligne][colonne];
    }

    public int[] getLocationTuile(TuileGraphique tuile) {
        boolean trouve = false;
        int[] pos = new int[2];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (tuile.equals(grilleGraphique[i][j])) {
                    trouve = true;
                    pos[0] = i;
                    pos[1] = j;
                    break;
                }
            }
            if (trouve) {
                break;
            }
        }
        return pos;
    }

    // === UPDATER =============================================================
    public void updateGrille(Grille grille, ArrayList<String> listeJoueurs) {
        panelGrille.removeAll();
        for (int ligne = 0; ligne < 6; ligne++) {
            for (int colonne = 0; colonne < 6; colonne++) {
                TuileGraphique tuile = new TuileGraphique(grille, listeJoueurs, ligne, colonne);
                panelGrille.add(tuile);
                grilleGraphique[ligne][colonne] = tuile;
            }
        }
    }

    public void updateCurrentPlayer(Aventurier aventurier, int waterLevel) {
        panelCP.removeAll();

        nbDeck = aventurier.getNbCartes();
        deckCourant = aventurier.getDeckTresor();
        ArrayList<CarteTresor> deck = aventurier.getDeckTresor();

        panelCP.setLayout(new BorderLayout(5, 5));
        // bouton action spéciale
        actionSpeciale = new BoutonPerso(BoutonPerso.ACTION_SPECIALE, BoutonPerso.SQUARE);
        panelCP.add(actionSpeciale, BorderLayout.CENTER);

        // deck carte tresor :
        panelGauche = new JPanel();
        panelGauche.setOpaque(false);
        panelCarte = new JPanel(new GridLayout(1, nbDeck, 20, 20));
        panelCarte.setOpaque(false);
        for (int i = 0; i < nbDeck; i++) {
            String nomCarte = deck.get(i).getNomCarteT();
            BoutonPerso carte = new BoutonPerso(nomCarte, BoutonPerso.FILL);
            this.activerBouton(carte);
            panelCarte.add(carte);
        }
        panelGauche.add(panelCarte);
        panelCP.add(panelGauche, BorderLayout.WEST);

        // boutons actions :
        panelDroite = new JPanel(new BorderLayout(5, 5));
        panelDroite.setOpaque(false);
        panelAction = new JPanel(new GridLayout(2, 2, 5, 5));
        panelAction.setOpaque(false);
        deplacer = new BoutonPerso(BoutonPerso.SE_DEPLACER, BoutonPerso.FILL);
        assecher = new BoutonPerso(BoutonPerso.ASSECHER, BoutonPerso.FILL);
        donnerCarte = new BoutonPerso(BoutonPerso.DONNER_CARTE, BoutonPerso.FILL);
        gagnerTresor = new BoutonPerso(BoutonPerso.GAGNER_TRESOR, BoutonPerso.FILL);
        panelAction.add(deplacer);
        panelAction.add(assecher);
        panelAction.add(donnerCarte);
        panelAction.add(gagnerTresor);
        panelDroite.add(panelAction, BorderLayout.WEST);

        BoutonPerso niveauDeau = new BoutonPerso(waterLevel);
        niveauDeau.setOpaque(false);
        panelDroite.add(niveauDeau, BorderLayout.CENTER);

        panelAb = new JPanel(new GridLayout(2, 1, 5, 5));
        panelAb.setOpaque(false);
        abandonner = new BoutonPerso(BoutonPerso.ABANDONNER, BoutonPerso.FILL);
        abandonner.setLayout(new BorderLayout());
        labelAbandonner = new JLabel("ABANDONNER");
        labelAbandonner.setHorizontalAlignment(JLabel.CENTER);
        abandonner.add(labelAbandonner, BorderLayout.CENTER);
        finirTour = new BoutonPerso(BoutonPerso.FINIR_TOUR, BoutonPerso.FILL);
        finirTour.setLayout(new BorderLayout());
        labelFinirTour = new JLabel("FINIR TOUR");
        labelFinirTour.setHorizontalAlignment(JLabel.CENTER);
        finirTour.add(labelFinirTour, BorderLayout.CENTER);
        panelAb.add(abandonner);
        panelAb.add(finirTour);
        panelDroite.add(panelAb, BorderLayout.EAST);
        panelCP.add(panelDroite, BorderLayout.EAST);

        if (aventurier.getPointAction() > 0) {
            activerBouton(deplacer);
            activerBouton(assecher);
            activerBouton(donnerCarte);
            activerBouton(gagnerTresor);
            activerBouton(actionSpeciale);
        }
        activerBouton(abandonner);
        activerBouton(finirTour);
    }

    public void updateJ1(Aventurier joueur1) {
        int couleur;
        switch (joueur1.getRole().getPion()) {
            case VIOLET:
                couleur = JPanelBackground.VIOLET;
                break;
            case BLEU:
                couleur = JPanelBackground.BLEU;
                break;
            case JAUNE:
                couleur = JPanelBackground.JAUNE;
                break;
            case ORANGE:
                couleur = JPanelBackground.ORANGE;
                break;
            case ROUGE:
                couleur = JPanelBackground.ROUGE;
                break;
            case VERT:
                couleur = JPanelBackground.VERT;
                break;
            default:
                couleur = JPanelBackground.CURRENT_PLAYER;
                break;
        }

        labelNomJ1 = new JLabel(joueur1.getNomAventurier());
        labelNomJ1.setForeground(Color.BLACK);
        labelNomJ1.setHorizontalAlignment(JLabel.CENTER);

        JPanelBackground panelRole = new JPanelBackground(couleur);
        panelRole.setLayout(new BorderLayout());
        labelRoleJ1 = new JLabel(joueur1.getRole().getNomRole());
        labelRoleJ1.setForeground(Color.BLACK);
        labelRoleJ1.setHorizontalAlignment(JLabel.CENTER);
        panelRole.add(labelRoleJ1, BorderLayout.CENTER);

        panelJ1H.add(labelNomJ1);
        panelJ1H.add(panelRole);
    }

    public void updateJ2(Aventurier joueur2) {
        int couleur;
        switch (joueur2.getRole().getPion()) {
            case VIOLET:
                couleur = JPanelBackground.VIOLET;
                break;
            case BLEU:
                couleur = JPanelBackground.BLEU;
                break;
            case JAUNE:
                couleur = JPanelBackground.JAUNE;
                break;
            case ORANGE:
                couleur = JPanelBackground.ORANGE;
                break;
            case ROUGE:
                couleur = JPanelBackground.ROUGE;
                break;
            case VERT:
                couleur = JPanelBackground.VERT;
                break;
            default:
                couleur = JPanelBackground.CURRENT_PLAYER;
                break;
        }

        labelNomJ2 = new JLabel(joueur2.getNomAventurier());
        labelNomJ2.setForeground(Color.BLACK);
        labelNomJ2.setHorizontalAlignment(JLabel.CENTER);

        JPanelBackground panelRole = new JPanelBackground(couleur);
        panelRole.setLayout(new BorderLayout());
        labelRoleJ2 = new JLabel(joueur2.getRole().getNomRole());
        labelRoleJ2.setForeground(Color.BLACK);
        labelRoleJ2.setHorizontalAlignment(JLabel.CENTER);
        panelRole.add(labelRoleJ2, BorderLayout.CENTER);

        panelJ2H.add(labelNomJ2);
        panelJ2H.add(panelRole);
    }

    public void updateJ3(Aventurier joueur3) {
        int couleur;
        switch (joueur3.getRole().getPion()) {
            case VIOLET:
                couleur = JPanelBackground.VIOLET;
                break;
            case BLEU:
                couleur = JPanelBackground.BLEU;
                break;
            case JAUNE:
                couleur = JPanelBackground.JAUNE;
                break;
            case ORANGE:
                couleur = JPanelBackground.ORANGE;
                break;
            case ROUGE:
                couleur = JPanelBackground.ROUGE;
                break;
            case VERT:
                couleur = JPanelBackground.VERT;
                break;
            default:
                couleur = JPanelBackground.CURRENT_PLAYER;
                break;
        }

        labelNomJ3 = new JLabel(joueur3.getNomAventurier());
        labelNomJ3.setForeground(Color.BLACK);
        labelNomJ3.setHorizontalAlignment(JLabel.CENTER);

        JPanelBackground panelRole = new JPanelBackground(couleur);
        panelRole.setLayout(new BorderLayout());
        labelRoleJ3 = new JLabel(joueur3.getRole().getNomRole());
        labelRoleJ3.setForeground(Color.BLACK);
        labelRoleJ3.setHorizontalAlignment(JLabel.CENTER);
        panelRole.add(labelRoleJ3, BorderLayout.CENTER);

        panelJ3H.add(labelNomJ3);
        panelJ3H.add(panelRole);
    }

    public void updateJ4(Aventurier joueur4) {
        int couleur;
        switch (joueur4.getRole().getPion()) {
            case VIOLET:
                couleur = JPanelBackground.VIOLET;
                break;
            case BLEU:
                couleur = JPanelBackground.BLEU;
                break;
            case JAUNE:
                couleur = JPanelBackground.JAUNE;
                break;
            case ORANGE:
                couleur = JPanelBackground.ORANGE;
                break;
            case ROUGE:
                couleur = JPanelBackground.ROUGE;
                break;
            case VERT:
                couleur = JPanelBackground.VERT;
                break;
            default:
                couleur = JPanelBackground.CURRENT_PLAYER;
                break;
        }

        labelNomJ4 = new JLabel(joueur4.getNomAventurier());
        labelNomJ4.setForeground(Color.BLACK);
        labelNomJ4.setHorizontalAlignment(JLabel.CENTER);

        JPanelBackground panelRole = new JPanelBackground(couleur);
        panelRole.setLayout(new BorderLayout());
        labelRoleJ4 = new JLabel(joueur4.getRole().getNomRole());
        labelRoleJ4.setForeground(Color.BLACK);
        labelRoleJ4.setHorizontalAlignment(JLabel.CENTER);
        panelRole.add(labelRoleJ4, BorderLayout.CENTER);

        panelJ4H.add(labelNomJ4);
        panelJ4H.add(panelRole);
    }

    public void updateDefausseTresor(ArrayList<CarteTresor> defausseTresor) {
        panelTresor.removeAll();
        panelTresor.add(new BoutonPerso(BoutonPerso.TRESOR, BoutonPerso.FILL));
        if (!defausseTresor.isEmpty()) {
            int derniereCarte = defausseTresor.size() - 1;
            String nomDerniereCarte = defausseTresor.get(derniereCarte).getNomCarteT();
            panelTresor.add(new BoutonPerso(nomDerniereCarte, BoutonPerso.FILL));
        } else {
            JPanel empty = new JPanel();
            empty.setOpaque(false);
            panelTresor.add(empty);
        }
    }

    public void updateDefausseInondation(ArrayList<CarteInondation> defausseInondation) {
        panelInondation.removeAll();
        panelInondation.add(new BoutonPerso(BoutonPerso.INONDATION, BoutonPerso.FILL));
        if (!defausseInondation.isEmpty()) {
            int derniereCarte = defausseInondation.size() - 1;
            String nomDerniereCarte = defausseInondation.get(derniereCarte).getNomCarteI();
            panelInondation.add(new BoutonPerso(nomDerniereCarte, BoutonPerso.FILL));
        } else {
            JPanel empty = new JPanel();
            empty.setOpaque(false);
            panelInondation.add(empty);
        }
    }

    // === LISTENERS ===========================================================
    private void redimension() {
        window.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent ce) {
                int largeurRef = window.getWidth();
                int hauteurRef = window.getHeight();

                // SOUTH
                panelCP.setPreferredSize(new Dimension(largeurRef, (int) (hauteurRef * 0.2)));

                int largeurCP = largeurRef;
                int hauteurCP = (int) (hauteurRef * 0.2);
                panelGauche.setPreferredSize(new Dimension(largeurCP / 2 - hauteurCP / 2, hauteurCP));
                panelDroite.setPreferredSize(new Dimension(largeurCP / 2 - hauteurCP / 2, hauteurCP));

                int largeurGD = largeurCP / 2 - hauteurCP / 2;
                int hauteurGD = hauteurCP;

                panelDroite.setBorder(new EmptyBorder((int) (hauteurGD * 0.18), (int) (largeurGD * 0.02), (int) (hauteurGD * 0.12), (int) (largeurGD * 0.08)));

                panelAction.setPreferredSize(new Dimension((int) (hauteurGD * 0.8), (int) (hauteurGD * 0.8)));
                panelAb.setPreferredSize(new Dimension((int) (hauteurGD * 0.8), (int) (hauteurGD * 0.8)));

                int hauteurDeck = (int) (hauteurGD * 0.8);
                int largeurDeck = (int) (hauteurDeck * 0.55 * nbDeck + (nbDeck - 1) * 20);
                panelCarte.setPreferredSize(new Dimension(largeurDeck, hauteurDeck));
                panelCarte.setBorder(new EmptyBorder((int) (hauteurGD * 0.2), 0, 0, 0));

                labelAbandonner.setFont(new Font("Impact", Font.PLAIN, (int) (largeurGD * 0.02)));
                labelFinirTour.setFont(new Font("Impact", Font.PLAIN, (int) (largeurGD * 0.02)));

                // EAST - WEST
                panelLeft.setPreferredSize(new Dimension((int) (largeurRef * 0.15), hauteurRef));
                panelRight.setPreferredSize(new Dimension((int) (largeurRef * 0.15), hauteurRef));
                Font font = new Font("Impact", Font.PLAIN, (int) (largeurRef * 0.02));
                labelNomJ1.setFont(font);
                labelNomJ2.setFont(font);
                labelRoleJ1.setFont(font);
                labelRoleJ2.setFont(font);
                if (nbJoueur >= 3) {
                    labelNomJ3.setFont(font);
                    labelRoleJ3.setFont(font);
                }
                if (nbJoueur == 4) {
                    labelNomJ4.setFont(font);
                    labelRoleJ4.setFont(font);
                }
            }

            @Override
            public void componentMoved(ComponentEvent ce) {
            }

            @Override
            public void componentShown(ComponentEvent ce) {
            }

            @Override
            public void componentHidden(ComponentEvent ce) {
            }
        });
    }

    private void initListener() {
        JPopupMenu popup;
        mlBouton = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
            }

            @Override
            public void mousePressed(MouseEvent me) {
                if (me.getSource() instanceof BoutonPerso) {
                    BoutonPerso bouton = (BoutonPerso) me.getSource();
                    if (bouton.getNomCarte() == null) {
                        if (bouton == deplacer) {
                            Message m = new Message();
                            m.type = TypesMessages.AFFICHER_CASES_DEPLACEMENT;
                            notifierObservateur(m);
                        }
                        if (bouton == assecher) {
                            Message m = new Message();
                            m.type = TypesMessages.AFFICHER_CASES_ASSECHEMENT;
                            notifierObservateur(m);
                        }
                        if (bouton == finirTour) {
                            Message m = new Message();
                            m.type = TypesMessages.FINIR_TOUR;
                            notifierObservateur(m);
                        }
                        if (bouton == abandonner) {
                            Message m = new Message();
                            m.type = TypesMessages.ABANDONNER;
                            notifierObservateur(m);
                        }
                    } else {
                        if (bouton.getNomCarte().equals("Helicoptere")) {
                            if (me.getButton() == MouseEvent.BUTTON3) {
                                PopupMenu popup = new PopupMenu(bouton.getNomCarte(), PlateauJeu.this);
                                popup.afficher(bouton, me.getX(), me.getY());
                            } else {
                                Message m = new Message();
                                m.type = TypesMessages.AFFICHER_CASES_DEPLACEMENT;
                                m.cardMode = true;
                                notifierObservateur(m);
                            }

                        }
                        if (bouton.getNomCarte().equals("Sacs de Sable")) {
                            if (me.getButton() == MouseEvent.BUTTON3) {
                                PopupMenu popup = new PopupMenu(bouton.getNomCarte(), PlateauJeu.this);
                                popup.afficher(bouton, me.getX(), me.getY());
                            } else {
                                Message m = new Message();
                                m.type = TypesMessages.AFFICHER_CASES_ASSECHEMENT;
                                m.cardMode = true;
                                notifierObservateur(m);
                            }
                        } else {
                            if (me.getButton() == MouseEvent.BUTTON3) {
                                PopupMenu popup = new PopupMenu(bouton.getNomCarte(), PlateauJeu.this);
                                popup.afficher(bouton, me.getX(), me.getY());
                            }

                        }
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent me) {

            }

            @Override
            public void mouseEntered(MouseEvent me) {
                window.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

                BoutonPerso bouton = (BoutonPerso) me.getSource();
                bouton.setHovered(true);
                bouton.repaint();
            }

            @Override
            public void mouseExited(MouseEvent me) {
                window.setCursor(Cursor.getDefaultCursor());

                BoutonPerso bouton = (BoutonPerso) me.getSource();
                bouton.setHovered(false);
                bouton.repaint();
            }
        };

        mlDeplacer = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
            }

            @Override
            public void mousePressed(MouseEvent me) {
                if (me.getSource() instanceof TuileGraphique) {
                    TuileGraphique tuile = (TuileGraphique) me.getSource();
                    int[] pos = getLocationTuile(tuile);

                    Message m = new Message();
                    m.type = TypesMessages.SE_DEPLACER;
                    m.posL = pos[0];
                    m.posC = pos[1];
                    notifierObservateur(m);
                }
            }

            @Override
            public void mouseReleased(MouseEvent me) {
            }

            @Override
            public void mouseEntered(MouseEvent me) {
                window.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

                TuileGraphique tuile = (TuileGraphique) me.getSource();
                tuile.setHovered(true);
                tuile.repaint();
            }

            @Override
            public void mouseExited(MouseEvent me) {
                window.setCursor(Cursor.getDefaultCursor());

                TuileGraphique tuile = (TuileGraphique) me.getSource();
                tuile.setHovered(false);
                tuile.repaint();
            }
        };

        mlAssecher = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
            }

            @Override
            public void mousePressed(MouseEvent me) {
                if (me.getSource() instanceof TuileGraphique) {
                    TuileGraphique tuile = (TuileGraphique) me.getSource();
                    int[] pos = getLocationTuile(tuile);

                    Message m = new Message();
                    m.type = TypesMessages.ASSECHER;
                    m.posL = pos[0];
                    m.posC = pos[1];
                    notifierObservateur(m);
                }
            }

            @Override
            public void mouseReleased(MouseEvent me) {
            }

            @Override
            public void mouseEntered(MouseEvent me) {
                window.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

                TuileGraphique tuile = (TuileGraphique) me.getSource();
                tuile.setHovered(true);
                tuile.repaint();
            }

            @Override
            public void mouseExited(MouseEvent me) {
                window.setCursor(Cursor.getDefaultCursor());

                TuileGraphique tuile = (TuileGraphique) me.getSource();
                tuile.setHovered(false);
                tuile.repaint();
            }
        };
    }

    private void activerBouton(BoutonPerso bouton) {
        bouton.addMouseListener(mlBouton);
    }

    // inutile ?
    private void desactiverBouton(BoutonPerso bouton) {
        bouton.removeMouseListener(mlBouton);
    }

    public void activerTuile(TuileGraphique tuile, String action) {
        if (action.equals("déplacer")) {
            tuile.addMouseListener(mlDeplacer);
        } else if (action.equals("assécher")) {
            tuile.addMouseListener(mlAssecher);
        }
    }

    // inutile ?
    public void desactiverTuile(TuileGraphique tuile, String action) {
        if (action.equals("déplacer")) {
            tuile.removeMouseListener(mlDeplacer);
        }
        if (action.equals("assécher")) {
            tuile.removeMouseListener(mlAssecher);
        }
    }

    // === AFFICHAGE / FERMETURE ===============================================
    public void afficher() {
        window.setVisible(true);
    }

    public void fermer() {
        window.setVisible(false);
    }

    public void refresh() {
        Dimension dim = window.getSize();
        window.setSize(dim.width - 1, dim.height);
        window.setSize(dim.width, dim.height);
        window.setCursor(Cursor.getDefaultCursor());
    }
}
