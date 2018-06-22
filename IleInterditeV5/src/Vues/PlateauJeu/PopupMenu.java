/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vues.PlateauJeu;
import MVC.Message;
import MVC.TypesMessages;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.BevelBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

public class PopupMenu extends JPanel {

  private JPopupMenu popup;
  private String nomCarte;
  private PlateauJeu plateau;

  public PopupMenu(String nomCarte,PlateauJeu plateau) {
    this.plateau = plateau;
    this.nomCarte = nomCarte;
    popup = new JPopupMenu();
    
    ActionListener menuListener = new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent event) {
        Message m = new Message();
        m.type = TypesMessages.JETER_CARTE;
        m.nomCarteT = nomCarte;
        plateau.notifierObservateur(m);
      }
    };
    JMenuItem item;
    popup.add(item = new JMenuItem("Jeter Carte"));
    item.setHorizontalTextPosition(JMenuItem.RIGHT);
    item.addActionListener(menuListener);

    popup.setLabel("Justification");
    popup.setBorder(new BevelBorder(BevelBorder.RAISED));
    popup.addPopupMenuListener(new PopupPrintListener());

  }

  public void afficher(Component c,int x,int y){
      popup.show(c,x,y);
  }

  // An inner class to show when popup events occur
  class PopupPrintListener implements PopupMenuListener {
    @Override
    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
      System.out.println("Popup menu will be visible!");
    }

    @Override
    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
      System.out.println("Popup menu will be invisible!");
    }

    @Override
    public void popupMenuCanceled(PopupMenuEvent e) {
      System.out.println("Popup menu is hidden!");
    }
  }

}