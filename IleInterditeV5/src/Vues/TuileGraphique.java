/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vues;

import MVC.Message;
import MVC.TypesMessages;
import Modele.Plateau.Grille;
import Modele.Plateau.Position;
import Modele.Plateau.Tuile;
import Util.Utils.EtatTuile;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 *
 * @author Aymerick
 */
public class TuileGraphique extends JPanel{
    private final Tuile tuile;
    private final Grille grille;
    private final String[] listeJoueurs;
    private final int ligne;
    private final int colonne;
    private boolean hlight = false;
    private PlateauJeu plateau;
    
    private JLabel labelNom;
    
    private static final Color COULEUR_ASSECHEE = new Color(108, 181, 217);
    private static final Color COULEUR_INONDEE = new Color(10, 147, 216);
    private static final Color COULEUR_COULEE = new Color(3, 53, 79);
    
    public TuileGraphique(Tuile tuile, Grille grille, String[] listeJoueurs, int ligne, int colonne) {
        this.tuile = tuile;
        this.grille = grille;
        this.listeJoueurs = listeJoueurs;
        this.ligne = ligne;
        this.colonne = colonne;
        this.config();
    }
    
    private void config() {
        if (tuile != null) {
            labelNom = new JLabel(tuile.getNomTuile());
            this.add(labelNom);
            
            this.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent me) {}

                @Override
                public void mousePressed(MouseEvent me) {
                    if(hlight){
                    plateau.notifier(colonne,ligne);
                    }
                }
                
                @Override
                public void mouseReleased(MouseEvent me) {}

                @Override
                public void mouseEntered(MouseEvent me) {
                    setBorder(new LineBorder(Color.WHITE, 3, false));
                }

                @Override
                public void mouseExited(MouseEvent me) {
                    setBorder(new EmptyBorder(3, 3, 3, 3));
                }
            });
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        int largeur = this.getWidth();
        int hauteur = this.getHeight();
        
        if (tuile != null) {
            if (tuile.getEtat() == EtatTuile.ASSECHEE) g.setColor(COULEUR_ASSECHEE);
            if (tuile.getEtat() == EtatTuile.INONDEE) g.setColor(COULEUR_INONDEE);
            if (tuile.getEtat() == EtatTuile.COULEE) g.setColor(COULEUR_COULEE);
            g.fillRect(0, 0, largeur, hauteur);
            
            if (tuile.getSpawnPion() != null) {
                g.setColor(tuile.getSpawnPion().getCouleur());
                g.fillOval(5, 25, (int) (largeur * 0.15), (int) (hauteur * 0.15));
                g.setColor(Color.BLACK);
                g.drawOval(5, 25, (int) (largeur * 0.15), (int) (hauteur * 0.15));
            }
            
            if (tuile.getSpawnTresor() != null) {
                Image img = null;
                switch (tuile.getSpawnTresor()) {
                    case CALICE:
                        img = getToolkit().getImage("ressources/tresor/calice.png");
                        break;
                    case CRISTAL:
                        img = getToolkit().getImage("ressources/tresor/cristal.png");
                        break;
                    case PIERRE:
                        img = getToolkit().getImage("ressources/tresor/pierre.png");
                        break;
                    case STATUE:
                        img = getToolkit().getImage("ressources/tresor/statue.png");
                        break;
                }
                g.drawImage(img, 5, 50, (int) (largeur * 0.3), (int) (hauteur * 0.3), this);
            }
            
            HashMap<String, Position> posJoueurs = grille.getPosJoueurs();
            for (String joueur : listeJoueurs) {
                Position posJoueur = posJoueurs.get(joueur);
                if (posJoueur.getLigne() == ligne && posJoueur.getColonne() == colonne) {
                    g.setColor(posJoueur.getAventurier().getPion().getCouleur());
                    if (posJoueur.getAventurier().getRole().getNomRole().equals("Explorateur")) {
                        g.fillRect(5, 60, 10, 10);
                        g.setColor(Color.BLACK); g.drawRect(5, 60, 10, 10);
                    }
                    if (posJoueur.getAventurier().getRole().getNomRole().equals("Ingénieur")) {
                        g.fillRect(20, 60, 10, 10);
                        g.setColor(Color.BLACK); g.drawRect(20, 60, 10, 10);
                    }
                    if (posJoueur.getAventurier().getRole().getNomRole().equals("Messager")) {
                        g.fillRect(35, 60, 10, 10);
                        g.setColor(Color.BLACK); g.drawRect(35, 60, 10, 10);
                    }
                    if (posJoueur.getAventurier().getRole().getNomRole().equals("Navigateur")) {
                        g.fillRect(50, 60, 10, 10);
                        g.setColor(Color.BLACK); g.drawRect(50, 60, 10, 10);
                    }
                    if (posJoueur.getAventurier().getRole().getNomRole().equals("Pilote")) {
                        g.fillRect(65, 60, 10, 10);
                        g.setColor(Color.BLACK); g.drawRect(65, 60, 10, 10);
                    }
                    if (posJoueur.getAventurier().getRole().getNomRole().equals("Plongeur")) {
                        g.fillRect(80, 60, 10, 10);
                        g.setColor(Color.BLACK); g.drawRect(80, 60, 10, 10);
                    }
                }
            }
            if(hlight){
                g.setColor(Color.YELLOW);
                g.fillOval(25, 25, 50, 50);
            }
            
            
        } else {
            g.setColor(COULEUR_COULEE);
            g.fillRect(0, 0, largeur, hauteur);
            Image img = getToolkit().getImage("ressources/tuile/coulee.png");
            g.drawImage(img, 0, 0, largeur, hauteur, this);
        }
        
        
    }

    public void setHlight(boolean hlight) {
        this.hlight = hlight;
        this.repaint();
    }
    
    public void setPlateau(PlateauJeu p){
        this.plateau = p;
    }
    
}
