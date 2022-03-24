package Connect5Game;

import java.util.ArrayList;
import java.util.Random;

/**
 * Algorithme d'évaluation aléatoire pour le choix d'un prochain coup pour un
 * jeu de Connect5.
 */
public class EvaluationChoixAleatoire implements EvaluationChoix {

    /**
     * Retourne une position libre aléatoire
     * 
     * @param grille L'état présent à évaluer.
     * @param delais Le délais pour prendre une décision.
     * @param param  Les paramètres de recherches.
     * @return Une position aléatoire.
     */
    @Override
    public Position evaluer(Grille grille, int delais, AlphaBetaConfig param) {
        Random random = new Random();
        ArrayList<Integer> casesvides = new ArrayList<Integer>();
        int nbcol = grille.getData()[0].length;
        for (int l = 0; l < grille.getData().length; l++)
            for (int c = 0; c < nbcol; c++)
                if (grille.getData()[l][c] == 0)
                    casesvides.add(l * nbcol + c);
        int choix = random.nextInt(casesvides.size());
        choix = casesvides.get(choix);
        return new Position(choix / nbcol, choix % nbcol);
    }

}
