package Connect5Game;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Algorithme de recherche Alpha-Beta pour le choix d'un prochain coup pour un
 * jeu de Connect5.
 */
public class EvaluationChoixAlphaBeta implements EvaluationChoix {

    /**
     * Retourne une position représentant un coup dans une partie de Connect5.
     * 
     * @param grille L'état présent à évaluer.
     * @param delais Le délais à respecter pour la prise de décision en ms.
     * @param config Les paramètres de recherches.
     * @return La décision à prendre.
     */
    @Override
    public Position evaluer(Grille grille, int delais, AlphaBetaConfig config) {
        int joueur = grille.getJoeurCourant();
        int utilite = Integer.MIN_VALUE;

        PriorityQueue<Successeur> successeurs = enumererSuccesseursTries(grille, joueur, config);
        Successeur successeur = null;
        int max = Integer.MIN_VALUE;
        Position choix = null;

        for (int i = 0; i < config.getNbrSuccesseurs(); ++i) {
            successeur = successeurs.poll();
            if (successeur == null)
                break;
            utilite = max(grille, Integer.MIN_VALUE, Integer.MAX_VALUE, config);
            if (utilite > max) {
                max = utilite;
                choix = successeur.positionChoisie;
            }
        }
        return choix;
    }

    /**
     * Calcul d'utilité pour le joueur MAX.
     * 
     * @param grille La grille à évaluer.
     * @param alpha  La borne inférieure.
     * @param beta   La borne supérieure.
     * @param config Les paramètres de recherche.
     * @return L'utilité maximale calculée.
     */
    private int max(Grille grille, int alpha, int beta, AlphaBetaConfig config) {
        int joueur = grille.getJoeurCourant();
        int utilite = Integer.MIN_VALUE;
        config.getConditions().incrementerProfondeur();

        if (config.verifierCondition(grille))
            return config.calculerUtilite(grille);

        PriorityQueue<Successeur> successeurs = enumererSuccesseursTries(grille, joueur, config);

        for (int i = 0; i < config.getNbrSuccesseurs(); ++i) {
            Successeur successeur = successeurs.poll();
            if (successeur != null) {
                utilite = Math.max(utilite, min(successeur.grille, alpha, beta, config));
                config.getConditions().decrementerProfondeur();
                if (utilite >= beta)
                    return utilite;
                alpha = Math.max(alpha, utilite);
            }
        }
        return utilite;
    }

    /**
     * Calcul d'utilité pour le joueur MIN.
     * 
     * @param grille La grille à évaluer.
     * @param alpha  La borne inférieure.
     * @param beta   La borne supérieure.
     * @param config Les paramètres de recherche.
     * @return L'utilité minimale calculée.
     */
    private int min(Grille grille, int alpha, int beta, AlphaBetaConfig config) {
        int joueur = grille.getJoeurCourant() == 1 ? 2 : 1;
        int utilite = Integer.MAX_VALUE;
        config.getConditions().incrementerProfondeur();

        if (config.verifierCondition(grille))
            return config.calculerUtilite(grille);

        PriorityQueue<Successeur> prochains = enumererSuccesseursTries(grille, joueur, config);

        for (int i = 0; i < config.getNbrSuccesseurs(); ++i) {
            Successeur prochain = prochains.poll();
            if (prochain != null) {
                utilite = Math.min(utilite, max(prochain.grille, alpha, beta, config));
                config.getConditions().decrementerProfondeur();
                if (utilite <= alpha)
                    return utilite;
                beta = Math.min(beta, utilite);
            }
        }
        return utilite;
    }

    /**
     * Retourne les successeur d'une grille en ordre décroissant en fonction de la
     * valeur d'utilité du successeurs.
     * 
     * @param grille La grille pour laquelle on génère les successeurs.
     * @param joueur La valeur représentant le joueur courant.
     * @param config Les paramètres de recherche.
     * @return Les successeurs de la grille triés.
     */
    private PriorityQueue<Successeur> enumererSuccesseursTries(Grille grille, int joueur, AlphaBetaConfig config) {
        PriorityQueue<Successeur> successeurs = new PriorityQueue<>(new Comparator<Successeur>() {
            public int compare(Successeur s1, Successeur s2) {
                return config.calculerUtilite(s2.grille) - config.calculerUtilite(s1.grille);
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
