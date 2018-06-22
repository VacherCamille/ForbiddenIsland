/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele.Aventurier;

import Modele.Plateau.Position;
import Modele.Plateau.Tuile;
import Util.Utils;
import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author dieuaida
 */
public class Plongeur extends CarteAventurier {
    
    public Plongeur() {
        super("Plongeur", Utils.Pion.VIOLET);
    }
    
    @Override
    public ArrayList<Tuile> getTuilesDeplacement() {
        int x = this.getJoueur().getPosition().getLigne();
        int y = this.getJoueur().getPosition().getColonne();
        int l1;
        int l2;
        HashSet<Position> aa = new HashSet<>();
        HashSet<Position> ab = new HashSet<>();
        ArrayList<Tuile> at = new ArrayList<>();
        //toutes les cases coulées et inondees atteignables
        aa = getCaseAdjacenteInCl(x, y);
        aa.add(new Position(x,y));
        ab.addAll(aa);
        do {
            l1 = aa.size();
            for (Position pos : aa) {
                ab.addAll(getCaseAdjacenteInCl(pos.getLigne(),pos.getColonne()));
            }
            aa.addAll(ab);
            l2 = aa.size();
        } while (l1 != l2);
        //toutes les cases asséchée et inondée atteignables
        ab.clear();
        for (Position pos : aa) {
            ab.addAll(getCaseAdjacenteAsIn(pos.getLigne(), pos.getColonne()));
        }
        //transformation en tuile (on transforme l'eau en vin)
        at.clear();
        for (Position pos : ab) {
            at.add(getJoueur().getEnvironnement().getTuile(pos.getLigne(), pos.getColonne()));
        }
        if(at.contains(this.getJoueur().getTuile())){
            at.remove(this.getJoueur().getTuile());
        }
        return at;

    }

    public HashSet<Position> getCaseAdjacenteInCl(int x, int y) {
        HashSet<Position> ap = new HashSet<>();
        if (getJoueur().getEnvironnement().getTuile(x + 1, y) != null) {
            if (getJoueur().getEnvironnement().getTuile(x + 1, y).getEtat() == Utils.EtatTuile.INONDEE || getJoueur().getEnvironnement().getTuile(x + 1, y).getEtat() == Utils.EtatTuile.COULEE) {
                Position a = new Position(x + 1, y);
                ap.add(a);
            }
        }
        if (getJoueur().getEnvironnement().getTuile(x - 1, y) != null) {
            if (getJoueur().getEnvironnement().getTuile(x - 1, y).getEtat() == Utils.EtatTuile.INONDEE || getJoueur().getEnvironnement().getTuile(x - 1, y).getEtat() == Utils.EtatTuile.COULEE) {
                Position a = new Position(x - 1, y);
                ap.add(a);

            }
        }
        if (getJoueur().getEnvironnement().getTuile(x, y + 1) != null) {
            if (getJoueur().getEnvironnement().getTuile(x, y + 1).getEtat() == Utils.EtatTuile.INONDEE || getJoueur().getEnvironnement().getTuile(x, y + 1).getEtat() == Utils.EtatTuile.COULEE) {
                Position a = new Position(x, y + 1);
                ap.add(a);

            }
        }
        if (getJoueur().getEnvironnement().getTuile(x, y - 1) != null) {
            if (getJoueur().getEnvironnement().getTuile(x, y - 1).getEtat() == Utils.EtatTuile.INONDEE || getJoueur().getEnvironnement().getTuile(x, y - 1).getEtat() == Utils.EtatTuile.COULEE) {
                Position a = new Position(x, y - 1);
                ap.add(a);

            }
        }
        return ap;
    }

    public HashSet<Position> getCaseAdjacenteAsIn(int x, int y) {
        HashSet<Position> ap = new HashSet<>();
        if (getJoueur().getEnvironnement().getTuile(x, y) != null) {
            if (getJoueur().getEnvironnement().getTuile(x, y).getEtat() == Utils.EtatTuile.ASSECHEE || getJoueur().getEnvironnement().getTuile(x , y).getEtat() == Utils.EtatTuile.INONDEE) {
                Position a = new Position(x, y);
                ap.add(a);
            }
        }
        if (getJoueur().getEnvironnement().getTuile(x + 1, y) != null) {
            if (getJoueur().getEnvironnement().getTuile(x + 1, y).getEtat() == Utils.EtatTuile.ASSECHEE || getJoueur().getEnvironnement().getTuile(x + 1, y).getEtat() == Utils.EtatTuile.INONDEE) {
                Position a = new Position(x + 1, y);
                ap.add(a);
            }
        }
        if (getJoueur().getEnvironnement().getTuile(x - 1, y) != null) {
            if (getJoueur().getEnvironnement().getTuile(x - 1, y).getEtat() == Utils.EtatTuile.ASSECHEE || getJoueur().getEnvironnement().getTuile(x - 1, y).getEtat() == Utils.EtatTuile.INONDEE) {
                Position a = new Position(x - 1, y);
                ap.add(a);
            }
        }
        if (getJoueur().getEnvironnement().getTuile(x, y + 1) != null) {
            if (getJoueur().getEnvironnement().getTuile(x, y + 1).getEtat() == Utils.EtatTuile.ASSECHEE || getJoueur().getEnvironnement().getTuile(x, y+1).getEtat() == Utils.EtatTuile.INONDEE) {
                Position a = new Position(x, y + 1);
                ap.add(a);
            }
        }
        if (getJoueur().getEnvironnement().getTuile(x, y - 1) != null) {
            if (getJoueur().getEnvironnement().getTuile(x, y - 1).getEtat() == Utils.EtatTuile.ASSECHEE || getJoueur().getEnvironnement().getTuile(x , y-1).getEtat() == Utils.EtatTuile.INONDEE) {
                Position a = new Position(x, y - 1);
                ap.add(a);
            }
        }
        return ap;
    }
}
