/*
 * INF4230 - Intelligence artificielle
 * UQAM / Département d'informatique
 *
 * Hiver 2013 / TP1
 *
 */

/**
 *
 */

public class HeuristiqueV1 extends Heuristique {

    HeuristiqueV1(final Ramassage monde) {
        super(monde);
    }
	
	/** Estime et retourne le coût restant pour atteindre le but à partir de état.
     *  Attention : pour être admissible, cette fonction heuristique ne doit pas
     *  surestimer le coût restant.
     */


    @Override
    public double estimerCoutRestant(final Etat etat, final But but) {
        double sommeDuree = 0;
    	// À Compléter.
    	// -- Vous devez réflechir à deux façons d'estimer la distance entre l'état courant et l'état but
    	// -- Pensez en termes de cout des déplacements du Van (vers le prochain Colis et vers le point d'Arrivee) 
    	// ainsi que de Ramassge/Depot des Colis
    	// -- Par exemple, pour calculer une distance, vous pouvez utiliser les positions géographiques des emplacements
    	// -- Commencez avec une version plus simple, puis raffinez vos estimations tout en veillant de ne pas surestimer le cout
        return sommeDuree;
    }

}
