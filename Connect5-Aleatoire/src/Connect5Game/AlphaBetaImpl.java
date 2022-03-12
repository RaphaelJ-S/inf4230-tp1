package Connect5Game;

import java.util.ArrayList;

/**
 * Algorithme de recherche Alpha-Beta pour le choix d'un prochain coup pour un
 * jeu de Connect5.
 */
public class AlphaBetaImpl implements EvaluationChoix {

    @Override
    public Position evaluer(Grille grille, int delais) {
        // TODO Auto-generated method stub
        return null;
    }

    private int max(Grille grille, int alpha, int beta) {

        return 0;
    }

    private int min(Grille grille, int alpha, int beta) {

        return 0;
    }

    // Je ne suis pas sur que cette méthode devrait être ici. Il semblerait qu'elle
    // devrait être dans Grille.
    private ArrayList<Grille> enumererSuccesseurs(Grille grille, int profondeur) {

        return new ArrayList<>();
    }

}
