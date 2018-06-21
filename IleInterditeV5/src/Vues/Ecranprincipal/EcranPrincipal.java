/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vues.Ecranprincipal;

import MVC.Message;
import MVC.Observe;
import MVC.TypesMessages;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Aymerick_PC
 */
public class EcranPrincipal extends Observe {
    
    private static final String MAIN_SCREEN = "mainScreen";
    private static final String OPTIONS = "options";
    private ArrayList<String> nomsJ;
    
    private JPanelBackground cards;
    
    // composants MAIN_SCREEN:
    private JFrame window;
    
    private JLabel labelTitle;
    private JLabel labelNbJoueurs;
    private JLabel labelDifficulte;
    private JLabel labelVersion;
    private JLabel labelCopyright;
    
    private JComboBox nbJoueurs;
    private JComboBox difficulte;
    
    private JButton demarrer;
    private JButton options;
    private JButton quitter;
    
    // composants OPTIONS:
    private JLabel labelOptions;
    private JButton retour;
    
    // composants JDialog
    private JDialog dialog;
    
    private JLabel labelNomPerso;
    private JLabel labelJ1;
    private JLabel labelJ2;
    private JLabel labelJ3;
    private JLabel labelJ4;
    
    private JTextField tfJ1;
    private JTextField tfJ2;
    private JTextField tfJ3;
    private JTextField tfJ4;
    
    private JButton lancer;
    
    public EcranPrincipal() {
        window = new JFrame("L'Île Interdite");
        
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(1080, 720);
        window.setMinimumSize(new Dimension(480, 360));
        window.setLocationRelativeTo(null);
        
        Font fontTitle = new Font("Impact", Font.PLAIN, (int) (Math.min(window.getWidth(), window.getHeight()) * 0.1));
        Font fontItem = new Font("Impact", Font.PLAIN, (int) (Math.min(window.getWidth(), window.getHeight()) * 0.04));
        
        dialog = new JDialog(window, "Nom des personnages", true);
        dialog.setSize(360, 480);
        dialog.setLocationRelativeTo(null);
        dialog.setResizable(false);
                
        Font fontTitleD = new Font("Impact", Font.PLAIN, (int) (dialog.getWidth() * 0.1));
        Font fontItemD = new Font("Impact", Font.PLAIN, (int) (dialog.getWidth() * 0.05));
        
        // composants MAIN_SCREEN:
        labelTitle = new JLabel("L'Île Interdite");
            labelTitle.setFont(fontTitle);
            labelTitle.setBorder(new EmptyBorder(50, 0, (int) (window.getHeight() * 0.1), 0));
        labelNbJoueurs = new JLabel("Nombres de joueurs :   ");
            labelNbJoueurs.setFont(fontItem);
        labelDifficulte = new JLabel("Difficulté :");
            labelDifficulte.setFont(fontItem);
            labelDifficulte.setHorizontalAlignment(JLabel.CENTER);
        labelVersion = new JLabel("Ile Interdite 0.6.0");
            labelVersion.setFont(fontItem);
            labelVersion.setBorder(new EmptyBorder(5, 5, 5, 5));
        labelCopyright = new JLabel("Copyright : Aymerick D., Manuel S., Camille V., Sullivan C., Penda D.");
            labelCopyright.setFont(new Font("Impact", Font.PLAIN, (int) (Math.min(window.getWidth(), window.getHeight()) * 0.03)));
            labelCopyright.setBorder(new EmptyBorder(5, 5, 5, 5));
        nbJoueurs = new JComboBox(new String[] { "2 Joueurs", "3 Joueurs", "4 Joueurs" });
            nbJoueurs.setFont(fontItem);
        difficulte = new JComboBox(new String[] { "Novice", "Normal", "Elite", "Légendaire" });
            difficulte.setFont(fontItem);
        demarrer = new JButton("Démarrer la partie");
            demarrer.setFont(fontItem);
        options = new JButton("Options");
            options.setFont(fontItem);
        quitter = new JButton("Quitter le Jeu");
            quitter.setFont(fontItem);
            
        // composants OPTIONS:
        labelOptions = new JLabel("Soon...");
            labelOptions.setFont(fontItem);
        retour = new JButton("Retour");
            retour.setFont(fontItem);
        
        // composants JDialog:
        labelNomPerso = new JLabel("Nom des personnages");
            labelNomPerso.setFont(fontTitleD);
            labelNomPerso.setBorder(new EmptyBorder(10, 0, 20, 0));
        labelJ1 = new JLabel("Joueur 1 : ");
            labelJ1.setFont(fontItemD);
            labelJ1.setHorizontalAlignment(JLabel.CENTER);
        labelJ2 = new JLabel("Joueur 2 : ");
            labelJ2.setFont(fontItemD);
            labelJ2.setHorizontalAlignment(JLabel.CENTER);
        labelJ3 = new JLabel("Joueur 3 : ");
            labelJ3.setFont(fontItemD);
            labelJ3.setHorizontalAlignment(JLabel.CENTER);
        labelJ4 = new JLabel("Joueur 4 : ");
            labelJ4.setFont(fontItemD);
            labelJ4.setHorizontalAlignment(JLabel.CENTER);
                
        tfJ1 = new JTextField();
            tfJ1.setFont(fontItemD);
        tfJ2 = new JTextField();
            tfJ2.setFont(fontItemD);
        tfJ3 = new JTextField();
            tfJ3.setFont(fontItemD);
        tfJ4 = new JTextField();
            tfJ4.setFont(fontItemD);
                
        lancer = new JButton("C'EST PARTI !");
            lancer.setFont(fontItem);
            
        // =====================================================================
        // JFrame
        cards = new JPanelBackground(JPanelBackground.MAIN_SCREEN);
        cards.setLayout(new CardLayout());
        cards.setBorder(new EmptyBorder(5, 5, 5, 5));
        
        JPanel panelMain = new JPanel(new BorderLayout(3, 3));
        panelMain.setOpaque(false);
            JPanel panelTitle = new JPanel();
            panelTitle.setOpaque(false);
                panelTitle.add(labelTitle);
            panelMain.add(panelTitle, BorderLayout.NORTH);
            
            JPanel panelMenu = new JPanel();
            panelMenu.setOpaque(false);
                JPanel gridMenu = new JPanel(new GridLayout(4, 1, 10, 10));
                gridMenu.setOpaque(false);
                    JPanel panelNbJoueurs = new JPanel(new GridLayout(1, 2, 1, 1));
                    panelNbJoueurs.setOpaque(false);
                        panelNbJoueurs.add(labelNbJoueurs);
                        panelNbJoueurs.add(nbJoueurs);
                    gridMenu.add(panelNbJoueurs);
                    JPanel panelDifficulte = new JPanel(new GridLayout(1, 2, 1, 1));
                    panelDifficulte.setOpaque(false);
                        panelDifficulte.add(labelDifficulte);
                        panelDifficulte.add(difficulte);
                    gridMenu.add(panelDifficulte);
                    gridMenu.add(demarrer);
                    JPanel panelOptQuit = new JPanel(new GridLayout(1, 2, 3, 3));
                    panelOptQuit.setOpaque(false);
                        panelOptQuit.add(options);
                        panelOptQuit.add(quitter);
                    gridMenu.add(panelOptQuit);
                panelMenu.add(gridMenu);
            panelMain.add(panelMenu, BorderLayout.CENTER);
            
            JPanel panelSouth = new JPanel(new BorderLayout());
            panelSouth.setOpaque(false);
                panelSouth.add(labelVersion, BorderLayout.WEST);
                panelSouth.add(labelCopyright, BorderLayout.EAST);
            panelMain.add(panelSouth, BorderLayout.SOUTH);
        cards.add(panelMain, MAIN_SCREEN);
        
        JPanel panelOptions = new JPanel(new BorderLayout());
        panelOptions.setOpaque(false);
            JPanel panelOptNorth = new JPanel();
                panelOptNorth.add(labelOptions);
            panelOptions.add(panelOptNorth, BorderLayout.NORTH);
            JPanel panelOptSouth = new JPanel(new BorderLayout());
                panelOptSouth.add(retour, BorderLayout.CENTER);
            panelOptions.add(panelOptSouth, BorderLayout.SOUTH);
        cards.add(panelOptions, OPTIONS);
        
        window.add(cards);
        
        // JDialog
        JPanel panelDialog = new JPanel(new BorderLayout(5, 5));
        panelDialog.setBorder(new EmptyBorder(10, 10, 10, 10));
            JPanel panelHeader = new JPanel();
                panelHeader.add(labelNomPerso);
            panelDialog.add(panelHeader, BorderLayout.NORTH);
            JPanel panelNomPerso = new JPanel(new GridLayout(4, 1, 50, 50));
            panelNomPerso.setBorder(new EmptyBorder(0, 0, 20, 0));
                JPanel panelJ1 = new JPanel(new GridLayout(1, 2));
                    panelJ1.add(labelJ1);
                    panelJ1.add(tfJ1);
                panelNomPerso.add(panelJ1);
                JPanel panelJ2 = new JPanel(new GridLayout(1, 2));
                    panelJ2.add(labelJ2);
                    panelJ2.add(tfJ2);
                panelNomPerso.add(panelJ2);
                JPanel panelJ3 = new JPanel(new GridLayout(1, 2));
                    panelJ3.add(labelJ3);
                    panelJ3.add(tfJ3);
                panelNomPerso.add(panelJ3);
                JPanel panelJ4 = new JPanel(new GridLayout(1, 2));
                    panelJ4.add(labelJ4);
                    panelJ4.add(tfJ4);
                panelNomPerso.add(panelJ4);
            panelDialog.add(panelNomPerso, BorderLayout.CENTER);
            panelDialog.add(lancer, BorderLayout.SOUTH);
        dialog.add(panelDialog);
        
        // =====================================================================
        
        
        
        this.initActionListener();
    }
    
    private void initActionListener() {
        // listener fenêtre :
        window.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent ce) {
                Font fontTitle = new Font("Impact", Font.PLAIN, (int) (Math.min(window.getWidth(), window.getHeight()) * 0.1));
                Font fontItem = new Font("Impact", Font.PLAIN, (int) (Math.min(window.getWidth(), window.getHeight()) * 0.04));
                
                // MAIN_SCREEN:
                labelTitle.setFont(fontTitle);
                labelTitle.setBorder(new EmptyBorder(20, 0, (int) (window.getHeight() * 0.1), 0));
                labelNbJoueurs.setFont(fontItem);
                labelDifficulte.setFont(fontItem);
                labelVersion.setFont(fontItem);
                labelCopyright.setFont(new Font("Impact", Font.PLAIN, (int) (Math.min(window.getWidth(), window.getHeight()) * 0.03)));
                nbJoueurs.setFont(fontItem);
                difficulte.setFont(fontItem);
                demarrer.setFont(fontItem);
                options.setFont(fontItem);
                quitter.setFont(fontItem);
                
                // OPTIONS:
                labelOptions.setFont(fontItem);
                retour.setFont(fontItem);
            }

            @Override
            public void componentMoved(ComponentEvent ce) {}

            @Override
            public void componentShown(ComponentEvent ce) {}

            @Override
            public void componentHidden(ComponentEvent ce) {}
        });
        
        quitter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                System.exit(0);
            }
        });
        
        demarrer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                tfJ1.setText("");
                tfJ2.setText("");
                tfJ3.setText(""); tfJ3.setEnabled(true);
                tfJ4.setText(""); tfJ4.setEnabled(true);
                
                if (nbJoueurs.getSelectedItem().equals("2 Joueurs")) {
                    tfJ3.setEnabled(false);
                    tfJ4.setEnabled(false);
                } else if (nbJoueurs.getSelectedItem().equals("3 Joueurs")) {
                    tfJ4.setEnabled(false);
                }
                
                dialog.setVisible(true);
            }
        });
        
        options.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                CardLayout c = (CardLayout) cards.getLayout();
                c.show(cards, OPTIONS);
            }
        });
        
        retour.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                CardLayout c = (CardLayout) cards.getLayout();
                c.show(cards, MAIN_SCREEN);
            }
        });
        
        lancer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                nomsJ = new ArrayList<>();
                    ajouterProprement(tfJ1.getText());
                    ajouterProprement(tfJ2.getText());
                if (nbJoueurs.getSelectedItem().equals("3 Joueurs")) {
                    ajouterProprement(tfJ3.getText());
                } else if (nbJoueurs.getSelectedItem().equals("4 Joueurs")) {
                    ajouterProprement(tfJ3.getText());
                    ajouterProprement(tfJ4.getText());
                }
                
                Message m = new Message();
                m.type = TypesMessages.DEMARRER_PARTIE;
                m.listeJoueurs = nomsJ;
                m.difficulte = (String) difficulte.getSelectedItem();
                notifierObservateur(m);
                
                dialog.setVisible(false);
            }
        });
    }
    
    private void ajouterProprement(String nomJoueur) {
        int i = 1;
        String nouveauNomJoueur = nomJoueur;
        while (nomsJ.contains(nouveauNomJoueur)) {
            nouveauNomJoueur = nomJoueur + "(" + i + ")";
            i += 1;
        }
        nomsJ.add(nouveauNomJoueur);
    }
    
    public void afficher() {
        window.setVisible(true);
    }
    
    public void fermer() {
        window.setVisible(false);
    }
}
