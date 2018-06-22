/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vues.PlateauJeu;

import MVC.Message;
import MVC.Observe;
import MVC.TypesMessages;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author sulmontm
 */
public class FinPartie extends Observe {

    private JFrame window;
    private boolean etat;
    private JButton recommencer;
    private JButton quitter;
    private JLabel etatPartie;

    public FinPartie(boolean etat) {
        initWindow();
        window.setLayout(new GridLayout(3, 1));
        recommencer = new JButton("RECOMMENCER");
        quitter = new JButton("QUITTER");
        etatPartie = new JLabel();
        setEtat(etat);
        window.add(etatPartie);
        window.add(recommencer);
        window.add(quitter);
        recommencer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Message m = new Message();
                m.type = TypesMessages.ABANDONNER;
                notifierObservateur(m);
                window.setVisible(false);
            }
        });

        quitter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

    }

    public void initWindow() {
        window = new JFrame("Partie Terminée");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(600, 200);
        window.setLocationRelativeTo(null);
    }

    public void setEtat(boolean etat) {
        if (etat) {
            etatPartie.setText("Bravo ! Vous Avez Gagné!");
        } else {
            etatPartie.setText("Dommage ! Vous Avez perdu!");
        }
    }
    
    public void afficher(boolean b){
            window.setVisible(b);
    }

}
