package Connect5Game;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

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
     * @param param  Les paramètres de recherches.
     * @return La décision à prendre.
     */
    @Override
    public Position evaluer(Grille grille, int delais, ParametreRecherche param) {
        int joueur = grille.getJoeurCourant();
        int utilite = Integer.MIN_VALUE;

        PriorityQueue<Successeur> succ = enumererSuccesseursTries(grille, joueur, param);
        Successeur proc = null;
        int max = Integer.MIN_VALUE;
        Position choix = null;
        for (int i = 0; i < param.getNbrSuccesseurs(); i++) {
            proc = succ.poll();
            if (proc == null)
                break;
            utilite = max(grille, Integer.MIN_VALUE, Integer.MAX_VALUE, param);
            if (utilite > max) {
                max = utilite > max ? utilite : max;
                choix = proc.positionChoisie;
                System.out.println(choix + " : " + max);

            }

        }

        return choix;
    }

    // calcule l'utilité du MAX de l'état dans alpha-beta
    private int max(Grille grille, int alpha, int beta, ParametreRecherche param) {
        int joueur = grille.getJoeurCourant();
        int utilite = Integer.MIN_VALUE;
        param.getConditions().incrementerProfondeur();

        if (/* profondeurRecherche == profondeurMax */ param.verifierCondition(grille)) {

            return param.calculerUtilite(grille);
        }
        PriorityQueue<Successeur> prochains = enumererSuccesseursTries(grille, joueur, param);

        for (int i = 0; i < param.getNbrSuccesseurs(); i++) {
            Successeur prochain = prochains.poll();
            if (prochain != null) {
                utilite = Math.max(utilite, min(prochain.grille, alpha, beta, param));
                param.getConditions().decrementerProfondeur();

                if (utilite >= beta) {

                    return utilite;
                }
                alpha = Math.max(alpha, utilite);
            }
        }
        /*
         * for (Successeur successeur : enumererSuccesseurs(grille, joueur, param)) {
         * int minVal = min(successeur.grille, alpha, beta, param);
         * if (minVal > utilite) {
         * utilite = minVal;
         * choix = successeur.positionChoisie;
         * }
         * if (utilite >= beta) {
         * param.getConditions().decrementerProfondeur();
         * 
         * return utilite;
         * }
         * alpha = Math.max(alpha, utilite);
         * }
         */

        return utilite;
    }

    // calcule l'utilité du MIN de l'état dans alpha-beta
    private int min(Grille grille, int alpha, int beta, ParametreRecherche param) {
        int joueur = grille.getJoeurCourant() == 1 ? 2 : 1;
        int utilite = Integer.MAX_VALUE;
        param.getConditions().incrementerProfondeur();

        if (param.verifierCondition(grille)) {

            return param.calculerUtilite(grille);
        }
        PriorityQueue<Successeur> prochains = enumererSuccesseursTries(grille, joueur, param);

        for (int i = 0; i < param.getNbrSuccesseurs(); i++) {
            Successeur prochain = prochains.poll();
            if (prochain != null) {
                utilite = Math.min(utilite, max(prochain.grille, alpha, beta, param));
                param.getConditions().decrementerProfondeur();

                if (utilite <= alpha) {
                    return utilite;

                }

                beta = Math.min(beta, utilite);
            }
        }

        return utilite;
    }
    /*
     * for (Successeur successeur : enumererSuccesseurs(grille, joueur, param)) {
     * 
     * utilite = Math.min(utilite, max(successeur.grille, alpha, beta, param));
     * if (utilite <= alpha) {
     * param.getConditions().decrementerProfondeur();
     * return utilite;
     * 
     * }
     * 
     * beta = Math.min(beta, utilite);
     * }
     */

    private ArrayList<Successeur> enumererSuccesseurs(Grille grille, int joueur, ParametreRecherche param) {
        ArrayList<Successeur> successeurs = new ArrayList<Successeur>();
        for (Position libre : grille.getPositionLibres()) {
            Grille successeur = grille.clone();
            successeur.set(libre, joueur);
            successeurs.add(new Successeur(successeur, libre));
        }

        return successeurs;
    }

    private PriorityQueue<Successeur> enumererSuccesseursTries(Grille grille, int joueur, ParametreRecherche param) {
        PriorityQueue<Successeur> successeurs = new PriorityQueue<>(new Comparator<Successeur>() {
            public int compare(Successeur s1, Successeur s2) {
                return param.calculerUtilite(s2.grille) - param.calculerUtilite(s1.grille);
            }
        });
        for (Position libre : grille.getPositionLibres()) {
            Grille successeur = grille.clone();
            successeur.set(libre, joueur);
            successeurs.add(new Successeur(successeur, libre));
        }

        return successeurs;
    }

    // Classe pour représenter le successeur d'une grille incluant la position du
    // coup choisi
    private class Successeur {
        Successeur(Grille agrille, Position aposition) {
            grille = agrille;
            positionChoisie = aposition;
        }

        protected Grille grille = null;
        protected Position positionChoisie = null;

    }
}
