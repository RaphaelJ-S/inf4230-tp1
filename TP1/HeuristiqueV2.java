/*
 * INF4230 - Intelligence artificielle
 * UQAM / Département d'informatique
 *
 * Hiver 2013 / TP1
 *
 */

import java.util.*;
import java.lang.Math;
import java.awt.geom.Point2D;

public class HeuristiqueV2 extends Heuristique {

    HeuristiqueV2(final Ramassage monde) {
        super(monde);
    }
	
	/** Estime et retourne le coût restant pour atteindre le but à partir de état.
     *  Attention : pour être admissible, cette fonction heuristique ne doit pas
     *  surestimer le coût restant.
     */


    @Override
    public double estimerCoutRestant(final Etat etat, final But but) {
        double coutChargerColis = 30;
        double coutLivrerColis = 30;
        double coutDeplacementRegulier = 2;
        double sommeDuree = 0;

    	// À Compléter.
    	// -- Vous devez réflechir à deux façons d'estimer la distance entre l'état courant et l'état but
    	// -- Pensez en termes de cout des déplacements du Van (vers le prochain Colis et vers le point d'Arrivee) 
    	// ainsi que de Ramassge/Depot des Colis
    	// -- Par exemple, pour calculer une distance, vous pouvez utiliser les positions géographiques des emplacements
    	// -- Commencez avec une version plus simple, puis raffinez vos estimations tout en veillant de ne pas surestimer le cout

        ArrayList<Point2D> colisRestants = new ArrayList<Point2D>();

        for(int i = 0; i < etat.colisRecuperes.length ; ++i)
        {
            if(!etat.colisRecuperes[i])
            {
                colisRestants.add(etat.emplacementsColis[i].positionGeographique);
            }
            else
            {
                if (!etat.emplacementsColis[i].equals(etat.ramassage.destination))
                {
                    sommeDuree += coutLivrerColis;
                }
            }
        }

        boolean PointDeLivraisonAjoute = false;
        sommeDuree += (coutChargerColis * colisRestants.size()) + (coutLivrerColis * colisRestants.size());
        Point2D emplacementCourant = etat.emplacementVan.positionGeographique;

        // Chaque tour dans le while correspond à la visite du prochain emplacement
        do
        {
            // On ajoute le point de livraison une fois tous les paquets récupérés
            if(colisRestants.size() == 0 && !PointDeLivraisonAjoute)
            {
                colisRestants.add(etat.ramassage.destination.positionGeographique);
                PointDeLivraisonAjoute = true;
            }

            int indexMinDistance = 0;
            double minDistance = Double.MAX_VALUE;
            Point2D emplacementSuivant = new Point2D.Float(0,0);

            for(int i = 0 ; i < colisRestants.size(); ++i)
            {
                Point2D emplacement = colisRestants.get(i);
                double px = (emplacement.getX() - emplacementCourant.getX()) * coutDeplacementRegulier;
                double py = (emplacement.getY() - emplacementCourant.getY()) * coutDeplacementRegulier;
                double distance = Math.sqrt((px * px) + (py * py));
                
                if(distance < minDistance)
                {
                    emplacementSuivant = emplacement;
                    minDistance = distance;
                    indexMinDistance = i;
                }
            }
            emplacementCourant = emplacementSuivant;
            colisRestants.remove(indexMinDistance);
            sommeDuree += minDistance;
        }
        while(!PointDeLivraisonAjoute);

        return sommeDuree;
    }

}
