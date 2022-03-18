package Connect5Game;

import java.util.ArrayList;

/**
 * Algorithme de recherche Alpha-Beta pour le choix d'un prochain coup pour un
 * jeu de Connect5.
 */
public class EvaluationChoixAlphaBeta implements EvaluationChoix {

    // ** ATTRIBUTS AJOUTÉS POUR FACILITER LE DEBUGGING **
    Position choix = null;
    int profondeurRecherche = 0;
    int profondeurMax = 5;


    /**
     * Retourne une position représentant un coup dans une partie de Connect5.
     * 
     * @param grille L'état présent à évaluer.
     * @param delais Le délais à respecter pour la prise de décision en ms.
     * @return La décision à prendre.
     */
    @Override
    public Position evaluer(Grille grille, int delais, ParametreRecherche param) {

        int utilite = max(grille, Integer.MIN_VALUE, Integer.MAX_VALUE, param);
        System.out.println(utilite);
        return choix;
    }

    // calcule l'utilité du MAX de l'état dans alpha-beta
    private int max(Grille grille, int alpha, int beta, ParametreRecherche param) {
        int joueur = grille.getJoeurCourant();
        int utilite = Integer.MIN_VALUE;
        if (profondeurRecherche == profondeurMax /* param.verifierCondition(grille) */) {
            return param.calculerUtilite(grille);
        }

        for (Successeur successeur : enumererSuccesseurs(grille, joueur, param.getFonction())) {
            int minVal = min(successeur.grille, alpha, beta, param);
            if(minVal > utilite)
            {
                utilite = minVal;
                choix = successeur.positionChoisie;
            }
            if (utilite >= beta) return utilite;
            alpha = Math.max(alpha, utilite);
        }
        return utilite;
    }

    // calcule l'utilité du MIN de l'état dans alpha-beta
    private int min(Grille grille, int alpha, int beta, ParametreRecherche param) {
        int joueur = grille.getJoeurCourant() == 1 ? 2 : 1;
        int utilite = Integer.MAX_VALUE;

        if (profondeurRecherche == profondeurMax /* param.verifierCondition(grille) */) {
            return param.calculerUtilite(grille);
        }

        for (Successeur successeur : enumererSuccesseurs(grille, joueur, param.getFonction())) {

            utilite = Math.min(utilite, max(successeur.grille, alpha, beta, param));
            if (utilite <= alpha) return utilite;
            beta = Math.min(beta, utilite);
        }
        return utilite;
    }

    private ArrayList<Successeur> enumererSuccesseurs(Grille grille, int joueur, Utilite fonction) {
        ArrayList<Successeur> successeurs = new ArrayList<Successeur>();
        for (Position libre : grille.getPositionLibres()){
            Grille successeur = grille.clone();
            successeur.set(libre, joueur);
            successeurs.add(new Successeur(successeur, libre));
        }
        ++profondeurRecherche;
        return successeurs;
    }

    // Classe pour représenter le successeur d'une grille incluant la position du coup choisi
    private class Successeur {
        Successeur(Grille agrille, Position aposition)
        {
            grille = agrille;
            positionChoisie = aposition;
        }

        protected Grille grille = null;
        protected Position positionChoisie = null;
    }
}
