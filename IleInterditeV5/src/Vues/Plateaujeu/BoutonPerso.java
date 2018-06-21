/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vues.Plateaujeu;

import MVC.Message;
import MVC.TypesMessages;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Aymerick
 */
public class BoutonPerso extends JPanel {

    private PlateauJeu plateau;

    private boolean hovered = false;
    private int niveauDeau = -1;
    private String nomCarte;

    private int type;
    public static final int ACTION_SPECIALE = 0;
    public static final int SE_DEPLACER = 1;
    public static final int ASSECHER = 2;
    public static final int DONNER_CARTE = 3;
    public static final int GAGNER_TRESOR = 4;
    public static final int TRESOR = 5;
    public static final int INONDATION = 6;
    public static final int ABANDONNER = 7;
    public static final int FINIR_TOUR = 8;

    private int filling;
    public static final int FILL = 0;
    public static final int SQUARE = 1;

    public BoutonPerso(int type, int filling, PlateauJeu plateau) {
        this.setOpaque(false);
        this.type = type;
        this.filling = filling;
        this.setOpaque(false);
        this.plateau = plateau;

    }

    public BoutonPerso(int niveauDeau, PlateauJeu plateau) {
        this.niveauDeau = niveauDeau;
        this.plateau = plateau;
    }

    // cartes inondation, helicoptere et sacs de sable
    public BoutonPerso(String nomCarte, int filling, PlateauJeu plateau) {
        this.setOpaque(false);
        this.nomCarte = nomCarte;
        this.filling = filling;
        this.plateau = plateau;
        this.labelConfig();
        this.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println(nomCarte);
                if (nomCarte.equals("Sacs de Sable")) {
                    Message m = new Message();
                    m.type = TypesMessages.CARTE_SABLE;
                    plateau.notifierObservateur(m);
                } else if (nomCarte.equals("Helicoptere")) {
                    Message m = new Message();
                    m.type = TypesMessages.CARTE_HELICOPTERE;
                    plateau.notifierObservateur(m);
                }

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
        }
        );

    }

    private void labelConfig() {
        if (nomCarte.equals("Helicoptere") || nomCarte.equals("Sacs de Sable")) {
            this.setLayout(new BorderLayout());
            JPanel panelNom = new JPanel();
            panelNom.setOpaque(false);
            JLabel labelNom = new JLabel(nomCarte);
            labelNom.setForeground(Color.WHITE);
            labelNom.setFont(new Font("Impact", Font.PLAIN, (int) (getWidth() * 0.15)));
            panelNom.add(labelNom);
            this.add(panelNom, BorderLayout.NORTH);

            this.addComponentListener(new ComponentListener() {
                @Override
                public void componentResized(ComponentEvent ce) {
                    labelNom.setFont(new Font("Impact", Font.PLAIN, (int) (getWidth() * 0.15)));
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
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        int largeur = this.getWidth();
        int hauteur = this.getHeight();
        Image img = null;

        if (nomCarte == null) {
            if (!hovered) {
                switch (type) {
                    case ACTION_SPECIALE:
                        img = new ImageIcon(getClass().getResource("/plateau_jeu/actionSpeciale.png")).getImage();
                        break;
                    case SE_DEPLACER:
                        img = new ImageIcon(getClass().getResource("/plateau_jeu/deplacer.png")).getImage();
                        break;
                    case ASSECHER:
                        img = new ImageIcon(getClass().getResource("/plateau_jeu/assecher.png")).getImage();
                        break;
                    case DONNER_CARTE:
                        img = new ImageIcon(getClass().getResource("/plateau_jeu/donnerCarte.png")).getImage();
                        break;
                    case GAGNER_TRESOR:
                        img = new ImageIcon(getClass().getResource("/plateau_jeu/gagnerTresor.png")).getImage();
                        break;
                    case TRESOR:
                        img = new ImageIcon(getClass().getResource("/carte_tresor/dosTresor.png")).getImage();
                        break;
                    case INONDATION:
                        img = new ImageIcon(getClass().getResource("/carte_tresor/dosInondation.png")).getImage();
                        break;
                    case ABANDONNER:
                        img = new ImageIcon(getClass().getResource("/plateau_jeu/abandonner.png")).getImage();
                        break;
                    case FINIR_TOUR:
                        img = new ImageIcon(getClass().getResource("/plateau_jeu/finirTour.png")).getImage();
                        break;
                }
            } else {
                switch (type) {
                    case ACTION_SPECIALE:
                        img = new ImageIcon(getClass().getResource("/plateau_jeu/actionSpeciale_hover.png")).getImage();
                        break;
                    case SE_DEPLACER:
                        img = new ImageIcon(getClass().getResource("/plateau_jeu/deplacer_hover.png")).getImage();
                        break;
                    case ASSECHER:
                        img = new ImageIcon(getClass().getResource("/plateau_jeu/assecher_hover.png")).getImage();
                        break;
                    case DONNER_CARTE:
                        img = new ImageIcon(getClass().getResource("/plateau_jeu/donnerCarte_hover.png")).getImage();
                        break;
                    case GAGNER_TRESOR:
                        img = new ImageIcon(getClass().getResource("/plateau_jeu/gagnerTresor_hover.png")).getImage();
                        break;
                    case TRESOR:
                        img = new ImageIcon(getClass().getResource("/carte_tresor/dosTresor_hover.png")).getImage();
                        break;
                    case INONDATION:
                        img = new ImageIcon(getClass().getResource("/carte_tresor/dosInondation_hover.png")).getImage();
                        break;
                    case ABANDONNER:
                        img = new ImageIcon(getClass().getResource("/plateau_jeu/abandonner_hover.png")).getImage();
                        break;
                    case FINIR_TOUR:
                        img = new ImageIcon(getClass().getResource("/plateau_jeu/finirTour_hover.png")).getImage();
                        break;
                }
            }

            if (img != null) {
                switch (filling) {
                    case FILL:
                        g.drawImage(img, 0, 0, largeur, hauteur, this);
                        break;
                    case SQUARE:
                        int cote = Math.min(largeur, hauteur);
                        int x = largeur / 2 - cote / 2;
                        int y = hauteur / 2 - cote / 2;
                        g.drawImage(img, x, y, cote, cote, this);
                        break;
                }
            }
        } else {
            if (!hovered) {
                try {
                    img = new ImageIcon(getClass().getResource("/carte_tresor/" + nomCarte + ".png")).getImage();
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(nomCarte);
                }

            } else {
                img = new ImageIcon(getClass().getResource("/carte_tresor/" + nomCarte + "_hover.png")).getImage();
            }
            g.drawImage(img, 0, 0, largeur, hauteur, this);
        }

        if (niveauDeau != -1) {
            img = new ImageIcon(getClass().getResource("/niveau_eau/niveau" + niveauDeau + ".png")).getImage();
            g.drawImage(img, 0, 0, largeur, hauteur, this);
        }
    }

    public void setHovered(boolean b) {
        this.hovered = b;
    }

    public String getNomCarte() {
        return nomCarte;
    }

}
