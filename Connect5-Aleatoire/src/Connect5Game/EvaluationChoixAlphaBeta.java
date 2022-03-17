package Connect5Game;

import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.ArrayList;
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
     * @return La décision à prendre.
     */
    @Override
    public Position evaluer(Grille grille, int delais, ParametreRecherche param) {
        Position choix = null;
        int utilite = Integer.MIN_VALUE;
        for (Paire libres : enumererSuccesseurs(grille, grille.getJoeurCourant(), param.getFonction())) {

            int nv_utilite = max(libres.grille, Integer.MIN_VALUE, Integer.MAX_VALUE, param);
            choix = nv_utilite >= utilite ? libres.pos : choix;
            utilite = nv_utilite;
        }

        return choix;
    }

    // calcule l'utilité du MAX de l'état dans alpha-beta
    private int max(Grille grille, int alpha, int beta, ParametreRecherche param) {
        int joueur = grille.getJoeurCourant();
        int utilite = Integer.MIN_VALUE;
        if (param.verifierCondition(grille)) {
            return param.calculerUtilite(grille);
        }

        for (Paire entry : enumererSuccesseurs(grille, joueur, param.getFonction())) {

            utilite = Math.max(utilite, min(entry.grille, alpha, beta, param));
            if (utilite >= beta)
                return utilite;
            alpha = Math.max(alpha, utilite);
        }
        return utilite;
    }

    // calcule l'utilité du MIN de l'état dans alpha-beta
    private int min(Grille grille, int alpha, int beta, ParametreRecherche param) {
        int joueur = grille.getJoeurCourant() == 1 ? 2 : 1;
        int utilite = Integer.MAX_VALUE;

        if (param.verifierCondition(grille)) {

            return param.calculerUtilite(grille);
        }

        for (Paire entry : enumererSuccesseurs(grille, joueur, param.getFonction())) {

            utilite = Math.min(utilite, max(entry.grille, alpha, beta, param));
            if (utilite <= alpha)
                return utilite;
            beta = Math.min(beta, utilite);
        }
        return utilite;
    }

    // Retourne les coups possibles associés à la valeurs d'utilité résultante de la
    // grille après ce coup.
    private ArrayList<Paire> enumererSuccesseurs(Grille grille, int joueur, Utilite fonction) {
        // Classe les grille en fonction de leurs utilité en ordre décroissant.
        ArrayList<Paire> branchements = new ArrayList<>();

        for (Position libre : grille.getPositionLibres()) {

            Grille prochain = grille.clone();
            prochain.set(libre, joueur);
            branchements.add(new Paire(prochain, libre));
        }

        return branchements;
    }

    // Classe wrapper pour retourner deux valeurs.
    private class Paire {
        public Grille grille;
        public Position pos;

        public Paire(Grille grille, Position pos) {
            this.grille = grille;
            this.pos = pos;
        }

    }

}
