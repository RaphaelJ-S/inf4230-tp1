import java.util.List;
import java.awt.geom.Point2D;

public class HeuristiqueV3 extends Heuristique {

    public HeuristiqueV3(Ramassage ramassage) {
        super(ramassage);
    }

    @Override
    public double estimerCoutRestant(final Etat etat, final But but) {
        double min = 100000000.0;
        Emplacement cible = null;
        // index de tous les colis qui restent à charger
        List<Integer> restant = etat.trouverTousIndexColis(false);
        List<Integer> aDeposer = etat.trouverTousIndexColis(true);

        // calcul du min de la distance euclidienne pour tous les colis restants
        for (Integer i : restant) {
            Point2D posColis = etat.emplacementsColis[i].positionGeographique;
            Point2D posVan = etat.emplacementVan.positionGeographique;
            double distance = posVan.distance(posColis);
            if (distance < min) {
                min = distance;
                cible = etat.emplacementsColis[i];
            }
        }
        if (cible == null) {
            cible = etat.ramassage.destination;
            min = etat.emplacementVan.positionGeographique.distance(cible.positionGeographique);
        }
        for (Integer i : aDeposer) {
            if (!etat.emplacementsColis[i].equals(etat.ramassage.destination))
                min += 30;
        }

        return min + 30 * restant.size();
    }

}
