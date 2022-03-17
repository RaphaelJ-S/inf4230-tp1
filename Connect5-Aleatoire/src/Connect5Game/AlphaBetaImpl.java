package Connect5Game;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Algorithme de recherche Alpha-Beta pour le choix d'un prochain coup pour un
 * jeu de Connect5.
 */
public class AlphaBetaImpl implements EvaluationChoix {

    private HashMap<Position, Integer> successeurs = new HashMap<>();

    /**
     * Retourne une position représentant un coup dans une partie de Connect5.
     * 
     * @param grille L'état présent à évaluer.
     * @param delais Le délais à respecter pour la prise de décision en ms.
     * @return La décision à prendre.
     */
    @Override
    public Position evaluer(Grille grille, int delais) {
        for (Position libre : grille.getPositionLibres()) {
            successeurs.put(libre, Integer.MIN_VALUE);
        }
        System.out.println(successeurs);
        int utilite = max(grille, Integer.MIN_VALUE, Integer.MAX_VALUE, null);
        for (Entry<Position, Integer> entry : successeurs.entrySet()) {
            if (entry.getValue() == utilite)
                return entry.getKey();

        }
        return null;
    }

    // calcule l'utilité du MAX de l'état dans alpha-beta
    private int max(Grille grille, int alpha, int beta, Position premiere) {
        Utilite fonction = new TousJoueursImpl();
        int joueur = grille.getJoeurCourant();
        int utilite = Integer.MIN_VALUE;
        if (testerEtatFinal(grille)) {
            utilite = fonction.evaluerUtilite(grille);
            successeurs.put(premiere, utilite);
            return utilite;
        }

        for (Entry<Grille, Position> entry : enumererSuccesseurs(grille, joueur, fonction, null).entrySet()) {

            utilite = Math.max(utilite, min(entry.getKey(), alpha, beta, entry.getValue()));
            if (utilite >= beta)
                return utilite;
            alpha = Math.max(alpha, utilite);
        }
        return utilite;
    }

    // calcule l'utilité du MIN de l'état dans alpha-beta
    private int min(Grille grille, int alpha, int beta, Position premiere) {
        Utilite fonction = new TousJoueursImpl();
        int joueur = grille.getJoeurCourant() == 1 ? 2 : 1;
        int utilite = Integer.MAX_VALUE;

        if (testerEtatFinal(grille)) {
            utilite = fonction.evaluerUtilite(grille);
            successeurs.put(premiere, utilite);
            return utilite;
        }
        for (Entry<Grille, Position> entry : enumererSuccesseurs(grille, joueur, fonction, premiere).entrySet()) {

            utilite = Math.min(utilite, max(entry.getKey(), alpha, beta, entry.getValue()));
            if (utilite <= alpha)
                return utilite;
            beta = Math.min(beta, utilite);
        }
        return utilite;
    }

    // Evaluation de l'arret de la recherche - pourrait être plein de choses.
    // dernier état, profondeur atteinte, utilité trop petite, etc
    private boolean testerEtatFinal(Grille grille) {
        return grille.nbLibre() == 0;
    }

    // Retourne les coups possibles associés à la valeurs d'utilité résultante de la
    // grille après ce coup.
    private TreeMap<Grille, Position> enumererSuccesseurs(Grille grille, int joueur, Utilite fonction,
            Position originale) {
        // Classe les grille en fonction de leurs utilité en ordre décroissant.
        TreeMap<Grille, Position> branchements = new TreeMap<>(new Comparator<Grille>() {
            @Override
            public int compare(Grille g1, Grille g2) {
                return fonction.evaluerUtilite(g2) - fonction.evaluerUtilite(g1);
            }
        });

        for (Position libre : grille.getPositionLibres()) {
            Grille prochain = grille.clone();
            prochain.set(libre, joueur);
            if (originale == null) {
                originale = libre;
            }
            branchements.put(prochain, originale);
        }
        return branchements;
    }

}
