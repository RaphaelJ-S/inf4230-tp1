package Connect5Game;

/**
 * Algorithme d'évaluation de choix pour un jeu de Connect5.
 */
public interface EvaluationChoix {

    /**
     * Évalue la position optimale pour une grille données.
     * 
     * @param grille La grille à évaluer.
     * @param delais Le délais à respecter.
     * @param param  Les paramètres de recherche.
     * @return La position choisie.
     */
    public Position evaluer(Grille grille, int delais, AlphaBetaConfig param);
}
