/*
 * INF4230 - Intelligence artificielle
 * UQAM / Département d'informatique
 * 
 * Hiver 2013 / TP1
 * 
 */

import java.awt.geom.Point2D;
import java.util.ArrayList;

/* Un emplacement représente un endroit sur la carte dans la planete. */
public class Emplacement implements Comparable<Emplacement> {

    protected  Point2D positionGeographique;
    protected  String  nom;
    protected  String  type;

    /* Routes attachées à cet emplacement. */
    protected  ArrayList<Route>  routes;
    
    /* Pour numérotation interne. */
    protected int        id;
    protected static int nextId = 0;
	
	
    public Emplacement(final String nom, final double x, final double y, final String type){
        this.nom = nom;
        this.type = type;
        this.positionGeographique = new Point2D.Double(x, y);
        this.routes = new ArrayList<Route>();
        this.id = nextId++;
    }

    @Override
    public int compareTo(final Emplacement o) {
        return id - o.id;
    }

    @Override
    public String toString(){
        //return nom //Ligne originale
        return "("+id+"(" + nom + ", " + type + ", " + positionGeographique.toString() + ")";
    }

}
