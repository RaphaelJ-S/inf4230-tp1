package Connect5Game;

import java.util.ArrayList;
import java.util.Random;

public class AleatoireImpl implements EvaluationChoix {

    /**
     * Retourne une position libre aléatoire
     * 
     * @param grille L'état présent à évaluer.
     * @param delais Le délais pour prendre une décision.
     * @return Une position aléatoire.
     */
    @Override
    public Position evaluer(Grille grille, int delais) {
        Random random = new Random();
        afficherGrille(grille);
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

    // fonction pour afficher la grille puisque toString the Grille ne fonctionne
    // pas
    // 1 sont les coups du joueur 1
    // 2 sont les coups du joueur 2
    // 0 sont les cases vides et valides
    // tous les autres chiffres sont les cases invalides
    private void afficherGrille(Grille grille) {
        for (byte[] tab1 : grille.data) {
            for (byte elem : tab1) {
                System.out.print(elem + " ");
            }
            System.out.println();
        }
    }

}
