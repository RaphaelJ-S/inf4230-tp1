package Connect5Game;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Map.Entry;

/**
 * Algorithme de recherche Alpha-Beta pour le choix d'un prochain coup pour un
 * jeu de Connect5.
 */
public class AlphaBetaImpl implements EvaluationChoix {

    /**
     * Retourne une position représentant un coup dans une partie de Connec5.
     * 
     * @param grille L'état présent à évaluer.
     * @param delais Le délais à respecter pour la prise de décision.
     * @return La décision à prendre.
     */
    @Override
    public Position evaluer(Grille grille, int delais) {

        int utilite = max(grille, Integer.MIN_VALUE, Integer.MAX_VALUE);
        TreeMap<Integer, Position> successeurs = enumererSuccesseurs(grille);

        return successeurs.get(utilite);
    }

    // calcule l'utilité du MAX de l'état dans alpha-beta
    private int max(Grille grille, int alpha, int beta) {
        if (testerEtatFinal(grille))
            return calculerUtilite(grille);
        int utilite = Integer.MIN_VALUE;
        for (Entry<Integer, Position> entry : enumererSuccesseurs(grille).entrySet()) {
            utilite = Math.max(utilite, min(grille, alpha, beta));
            if (utilite >= beta)
                return utilite;
            alpha = Math.max(alpha, utilite);
        }
        return utilite;
    }

    // calcule l'utilité du MIN de l'état dans alpha-beta
    private int min(Grille grille, int alpha, int beta) {
        if (testerEtatFinal(grille))
            return calculerUtilite(grille);
        int utilite = Integer.MIN_VALUE;
        for (Entry<Integer, Position> entry : enumererSuccesseurs(grille).entrySet()) {
            utilite = Math.max(utilite, min(grille, alpha, beta));
            if (utilite >= beta)
                return utilite;
            alpha = Math.max(alpha, utilite);
        }
        return utilite;
        return 0;
    }

    // Evaluation de l'arret de la recherche - pourrait être plein de choses.
    // dernier état, profondeur atteinte, utilité trop petite, etc
    private boolean testerEtatFinal(Grille grille) {

        return true;
    }

    // Je ne suis pas sur que cette fonction devrait être dans cette classe.
    // Version de la fonction sans check de profondeur.
    private TreeMap<Integer, Position> enumererSuccesseurs(Grille grille) {

        return new TreeMap<>();
    }

    // Il me semble aussi que cette fonction devrait être quelque part d'autre.
    private int calculerUtilite(Grille grille) {

        return 0;
    }

}
