/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vues.PlateauJeu;

import Modele.Plateau.Grille;
import Modele.Plateau.Position;
import Modele.Plateau.Tuile;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import static javax.swing.SwingConstants.CENTER;

/**
 *
 * @author Aymerick
 */
public class TuileGraphique extends JPanel {
    private boolean hovered = false;
    
    private final Grille grille;
    private final Tuile tuile;
    private final ArrayList<String> listeJoueurs;
    private final int ligne;
    private final int colonne;
    
    private Image imgPion;
    
    private JLabel labelTuile;
    
    public TuileGraphique(Grille grille, ArrayList<String> listeJoueurs, int ligne, int colonne) {
        this.setOpaque(false);
        
        this.grille = grille;
        this.ligne = ligne;
        this.colonne = colonne;
        this.listeJoueurs = listeJoueurs;
        this.tuile = grille.getTuile(ligne, colonne);
        
        if (tuile != null) {
            this.setLayout(new BorderLayout());
            
            labelTuile = new JLabel(tuile.getNomTuile());
            labelTuile.setHorizontalAlignment(CENTER);
            labelTuile.setOpaque(false);
            labelTuile.setFont(new Font("Impact", Font.PLAIN, 18));
            if (tuile.getNomTuile().equals("Heliport")) {
                labelTuile.setForeground(Color.WHITE);
            } else {
                labelTuile.setForeground(Color.BLACK);
            }
            this.add(labelTuile, BorderLayout.SOUTH);
            
            this.addComponentListener(new ComponentListener() {
                @Override
                public void componentResized(ComponentEvent ce) {
                    labelTuile.setFont(new Font("Impact", Font.PLAIN, (int) (getWidth() * 0.08)));
                }

                @Override
                public void componentMoved(ComponentEvent ce) {}

                @Override
                public void componentShown(ComponentEvent ce) {}

                @Override
                public void componentHidden(ComponentEvent ce) {}
            });
        }
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        int largeur = this.getWidth();
        int hauteur = this.getHeight();
        
        if (tuile != null) {
            switch(tuile.getEtat()) {
                case ASSECHEE:
                    Image imgDefault;
                    if (!hovered) {
                        imgDefault = new ImageIcon(getClass().getResource("/tuile_jeu/" + tuile.getNomTuile() + ".png")).getImage();
                    } else {
                        imgDefault = new ImageIcon(getClass().getResource("/tuile_jeu/" + tuile.getNomTuile() + "_hover.png")).getImage();
                    }
                    g.drawImage(imgDefault, 0, 0, largeur, hauteur, this);
                    break;
                case INONDEE:
                    Image imgFlood;
                    if (!hovered) {
                        imgFlood = new ImageIcon(getClass().getResource("/tuile_jeu/" + tuile.getNomTuile() + "_flood.png")).getImage();
                    } else {
                        imgFlood = new ImageIcon(getClass().getResource("/tuile_jeu/" + tuile.getNomTuile() + "_flood_hover.png")).getImage();
                    }
                    g.drawImage(imgFlood, 0, 0, largeur, hauteur, this);
                    break;
            }
        }
        
        HashMap<String, Position> positions = grille.getPosJoueurs();
        for (String joueur : listeJoueurs) {
            Position posJoueur = positions.get(joueur);
            String role = posJoueur.getAventurier().getRole().getNomRole();
            if (posJoueur.getLigne() == ligne && posJoueur.getColonne() == colonne) {
                switch (role) {
                    case "Explorateur":
                        imgPion = new ImageIcon(getClass().getResource("/plateau_jeu/pionVert.png")).getImage();
                        break;
                    case "Ingénieur":
                        imgPion = new ImageIcon(getClass().getResource("/plateau_jeu/pionRouge.png")).getImage();
                        break;
                    case "Messager":
                        imgPion = new ImageIcon(getClass().getResource("/plateau_jeu/pionOrange.png")).getImage();
                        break;
                    case "Navigateur":
                        imgPion = new ImageIcon(getClass().getResource("/plateau_jeu/pionJaune.png")).getImage();
                        break;
                    case "Pilote":
                        imgPion = new ImageIcon(getClass().getResource("/plateau_jeu/pionBleu.png")).getImage();
                        break;
                    case "Plongeur":
                        imgPion = new ImageIcon(getClass().getResource("/plateau_jeu/pionViolet.png")).getImage();
                        break;
                }
                g.drawImage(imgPion, 0, 0, largeur, hauteur, this);
            }
        }
    }
    
    public void setHovered(boolean b) {
        this.hovered = b;
    }
    
    public Tuile getTuile() {
        return tuile;
    }
}
