/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vues.PlateauJeu1;

import MVC.Message;
import MVC.Observe;
import MVC.TypesMessages;
import static MVC.TypesMessages.POSITION;
import Modele.Aventurier.Aventurier;
import Modele.CarteTresor.CarteTresor;
import Modele.Divers.CarteInondation;
import Modele.Plateau.Grille;
import Vues.EcranPrincipal1.JPanelBackground;
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
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Aymerick
 */
public class PlateauJeu extends Observe {

    private JFrame window;

    // VARIABLES (GRILLE DE JEU) :
    private JPanel panelGrille;
    private TuileGraphique[][] grilleGraphique = new TuileGraphique[6][6];

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
    private BoutonPerso finirTour;
    private BoutonPerso panelCentre;

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
    MouseListener ml;

    // VARIABLES UTILITAIRES :
    int nbJoueur;
    int nbDeck = 5;

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

        window.add(panelMain);
    }

    // === UTILITAIRE ==========================================================
    public void initGrille(Grille grille, ArrayList<String> listeJoueurs) {
        panelGrille.removeAll();
        for (int ligne = 0; ligne < 6; ligne++) {
            for (int colonne = 0; colonne < 6; colonne++) {
                TuileGraphique tuile = new TuileGraphique(grille, listeJoueurs, ligne, colonne);
                panelGrille.add(tuile);
                tuile.setPlateau(this);
                grilleGraphique[ligne][colonne] = tuile;
            }
        }
    }

    public void updateCurrentPlayer(Aventurier aventurier) {
        panelCP.removeAll();
        nbDeck = aventurier.getNbCartes();
        ArrayList<CarteTresor> deck = aventurier.getDeckTresor();
        panelCP.setLayout(new BorderLayout(5, 5));
        // bouton action spéciale
        panelCentre = new BoutonPerso(BoutonPerso.ACTION_SPECIALE, BoutonPerso.SQUARE);
        panelCentre.addMouseListener(ml);
        panelCP.add(panelCentre, BorderLayout.CENTER);

        // deck carte tresor :
        panelGauche = new JPanel();
        panelGauche.setOpaque(false);
        panelCarte = new JPanel(new GridLayout(1, nbDeck, 20, 20));
        panelCarte.setOpaque(false);
        for (int i = 0; i < nbDeck; i++) {
            String nomCarte = deck.get(i).getNomCarteT();
            panelCarte.add(new BoutonPerso(nomCarte, BoutonPerso.FILL));
        }
        panelGauche.add(panelCarte);
        panelCP.add(panelGauche, BorderLayout.WEST);

        // boutons actions :
        panelDroite = new JPanel();
        panelDroite.setOpaque(false);
        panelAction = new JPanel(new GridLayout(1, 2, 50, 50));
        panelAction.setOpaque(false);
        JPanel panelAct = new JPanel(new GridLayout(2, 2, 5, 5));
        panelAct.setOpaque(false);
        deplacer = new BoutonPerso(BoutonPerso.SE_DEPLACER, BoutonPerso.FILL);
        assecher = new BoutonPerso(BoutonPerso.ASSECHER, BoutonPerso.FILL);
        donnerCarte = new BoutonPerso(BoutonPerso.DONNER_CARTE, BoutonPerso.FILL);
        gagnerTresor = new BoutonPerso(BoutonPerso.GAGNER_TRESOR, BoutonPerso.FILL);
        panelAct.add(deplacer);
        deplacer.addMouseListener(ml);
        panelAct.add(assecher);
        assecher.addMouseListener(ml);
        panelAct.add(donnerCarte);
        donnerCarte.addMouseListener(ml);
        panelAct.add(gagnerTresor);
        gagnerTresor.addMouseListener(ml);
        panelAction.add(panelAct);
        JPanel panelAb = new JPanel(new GridLayout(2, 1, 5, 5));
        panelAb.setOpaque(false);
        abandonner = new BoutonPerso(BoutonPerso.DONNER_CARTE, BoutonPerso.FILL);
        finirTour = new BoutonPerso(BoutonPerso.GAGNER_TRESOR, BoutonPerso.FILL);
        panelAb.add(abandonner);
        abandonner.addMouseListener(ml);
        panelAb.add(finirTour);
        finirTour.addMouseListener(ml);
        panelAction.add(panelAb);
        panelDroite.add(panelAction);
        panelCP.add(panelDroite, BorderLayout.EAST);
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
        labelRoleJ1 = new JLabel(joueur1.getRole().getNomRole());
        labelRoleJ1.setForeground(Color.BLACK);
        panelRole.add(labelRoleJ1);

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
        labelRoleJ2 = new JLabel(joueur2.getRole().getNomRole());
        labelRoleJ2.setForeground(Color.BLACK);
        panelRole.add(labelRoleJ2);

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
        labelRoleJ3 = new JLabel(joueur3.getRole().getNomRole());
        labelRoleJ3.setForeground(Color.BLACK);
        panelRole.add(labelRoleJ3);

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
        labelRoleJ4 = new JLabel(joueur4.getRole().getNomRole());
        labelRoleJ4.setForeground(Color.BLACK);
        panelRole.add(labelRoleJ4);

        panelJ4H.add(labelNomJ4);
        panelJ4H.add(panelRole);
    }

    public void updatePileTresor(ArrayList<CarteTresor> defausseTresor) {
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

    public void updatePileInondation(ArrayList<CarteInondation> defausseInondation) {
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

                int hauteurDeck = (int) (hauteurGD * 0.8);
                int largeurDeck = (int) (hauteurDeck * 0.55 * nbDeck + (nbDeck - 1) * 20);
                panelCarte.setPreferredSize(new Dimension(largeurDeck, hauteurDeck));
                panelCarte.setBorder(new EmptyBorder((int) (hauteurGD * 0.2), 0, 0, 0));

                panelAction.setPreferredSize(new Dimension((int) (hauteurGD * 0.6 * 2 + 50), (int) (hauteurGD * 0.8)));
                panelAction.setBorder(new EmptyBorder((int) (hauteurGD * 0.2), 0, 0, 0));

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
        ml = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {

            }

            @Override
            public void mousePressed(MouseEvent me) {
                if (me.getSource() == deplacer) {
                    Message m = new Message();
                    m.type = TypesMessages.AFFICHER_CASES_DEPLACEMENT;
                    notifierObservateur(m);
                }
                if (me.getSource() == assecher) {
                    Message m = new Message();
                    m.type = TypesMessages.AFFICHER_CASES_ASSECHEMENT;
                    notifierObservateur(m);
                }
                if (me.getSource() == donnerCarte) {
                    donnerCarte.setHovered(true);
                    donnerCarte.repaint();
                }
                if (me.getSource() == gagnerTresor) {
                    gagnerTresor.setHovered(true);
                    gagnerTresor.repaint();
                }
                if (me.getSource() == abandonner) {
                    abandonner.setHovered(true);
                    abandonner.repaint();
                }
                if (me.getSource() == finirTour) {
                    Message m = new Message();
                    m.type = TypesMessages.FINIR_TOUR;
                    notifierObservateur(m);
                }
                if (me.getSource() == panelCentre) {
                    panelCentre.setHovered(true);
                    panelCentre.repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent me) {

            }

            @Override
            public void mouseEntered(MouseEvent me) {
                window.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                if (me.getSource() == deplacer) {
                    deplacer.setHovered(true);
                    deplacer.repaint();
                }
                if (me.getSource() == assecher) {
                    assecher.setHovered(true);
                    assecher.repaint();
                }
                if (me.getSource() == donnerCarte) {
                    donnerCarte.setHovered(true);
                    donnerCarte.repaint();
                }
                if (me.getSource() == gagnerTresor) {
                    gagnerTresor.setHovered(true);
                    gagnerTresor.repaint();
                }
                if (me.getSource() == abandonner) {
                    abandonner.setHovered(true);
                    abandonner.repaint();
                }
                if (me.getSource() == finirTour) {
                    finirTour.setHovered(true);
                    finirTour.repaint();
                }
                if (me.getSource() == panelCentre) {
                    panelCentre.setHovered(true);
                    panelCentre.repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent me) {
                window.setCursor(Cursor.getDefaultCursor());
                if (me.getSource() == deplacer) {
                    deplacer.setHovered(false);
                    deplacer.repaint();
                }
                if (me.getSource() == assecher) {
                    assecher.setHovered(false);
                    assecher.repaint();
                }
                if (me.getSource() == donnerCarte) {
                    donnerCarte.setHovered(false);
                    donnerCarte.repaint();
                }
                if (me.getSource() == gagnerTresor) {
                    gagnerTresor.setHovered(false);
                    gagnerTresor.repaint();
                }
                if (me.getSource() == abandonner) {
                    abandonner.setHovered(false);
                    abandonner.repaint();
                }
                if (me.getSource() == finirTour) {
                    finirTour.setHovered(false);
                    finirTour.repaint();
                }
                if (me.getSource() == panelCentre) {
                    panelCentre.setHovered(false);
                    panelCentre.repaint();
                }
            }
        };
    }

    public TuileGraphique getTuileGraphique(int x, int y) {
        return grilleGraphique[x][y];
    }



    public void resetHlight() {
        for (int i = 0; i < 6; i++) { // lignes
            for (int j = 0; j < 6; j++) { // colonnes
                grilleGraphique[i][j].setHlight(Color.BLACK);
            }
        }
    }

    // === AFFICHAGE / FERMETURE ===============================================
    public void afficher() {
        window.setVisible(true);
    }

    public void fermer() {
        window.setVisible(false);
    }

    public void update() {
                window.repaint();
    }
}
