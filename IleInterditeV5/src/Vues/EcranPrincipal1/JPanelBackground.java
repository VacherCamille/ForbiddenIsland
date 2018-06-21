/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vues.EcranPrincipal1;

//import PlateauJeu.PlateauJeu;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author Aymerick_PC
 */
public class JPanelBackground extends JPanel {
    
    private final int type;
    
    public static final int MAIN_SCREEN = 0;
    public static final int PLATEAU_JEU = 1;
    public static final int CURRENT_PLAYER = 2;
    public static final int VIOLET = 3;
    public static final int JAUNE = 4;
    public static final int VERT = 5;
    public static final int ROUGE = 6;
    public static final int BLEU = 7;
    public static final int ORANGE = 8;
    public static final int PANEL_LEFT_RIGHT = 9;
    
    public JPanelBackground(int type) {
        this.type = type;
        this.setOpaque(false);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Dimension dim = this.getSize();
        Image img = null;
        
        switch (type) {
            case MAIN_SCREEN:
                img = new ImageIcon(getClass().getResource("/ecran_principal/island.jpg")).getImage();
                break;
            case PLATEAU_JEU:
                img = new ImageIcon(getClass().getResource("/plateau_jeu/background.jpg")).getImage();
                break;
            case CURRENT_PLAYER:
                img = new ImageIcon(getClass().getResource("/plateau_jeu/footer_background.png")).getImage();
                break;
            case VIOLET:
                img = new ImageIcon(getClass().getResource("/plateau_jeu/violet.png")).getImage();
                break;
            case JAUNE:
                img = new ImageIcon(getClass().getResource("/plateau_jeu/jaune.png")).getImage();
                break;
            case VERT:
                img = new ImageIcon(getClass().getResource("/plateau_jeu/vert.png")).getImage();
                break;
            case ROUGE:
                img = new ImageIcon(getClass().getResource("/plateau_jeu/rouge.png")).getImage();
                break;
            case BLEU:
                img = new ImageIcon(getClass().getResource("/plateau_jeu/bleu.png")).getImage();
                break;
            case ORANGE:
                img = new ImageIcon(getClass().getResource("/plateau_jeu/orange.png")).getImage();
                break;
            case PANEL_LEFT_RIGHT:
                img = new ImageIcon(getClass().getResource("/plateau_jeu/panelJJC.png")).getImage();
                break;
        }
        
        if (img != null) g.drawImage(img, 0, 0, dim.width, dim.height, this);
    }
}
