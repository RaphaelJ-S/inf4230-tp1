package Connect5Game;

import java.util.TreeMap;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Algorithme de recherche Alpha-Beta pour le choix d'un prochain coup pour un
 * jeu de Connect5.
 */
public class EvaluationChoixAlphaBeta implements EvaluationChoix {
    private class Successeur
    {
        Successeur(Grille agrille, Position positionChoisie)
        {
            grille = agrille;
            position = positionChoisie;
        }

        protected Grille grille = null;
        protected Position position = null;
    }

    int maxProfondeur = 5;
    int profondeur = 0;
    TreeMap<Integer, Successeur> choix = new TreeMap<>();

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
        return choix.get(utilite).position;
    }

    // calcule l'utilité du MAX de l'état dans alpha-beta
    private int max(Grille grille, int alpha, int beta, ParametreRecherche param) {
        int joueur = grille.getJoeurCourant();
        int utilite = Integer.MIN_VALUE;
        if (maxProfondeur == profondeur) {
            return param.calculerUtilite(grille);
        }

        for (Successeur successeur : getSuccesseurs(grille, joueur, param.getFonction())) {
            int minVal = min(successeur.grille, alpha, beta, param);
            choix.put(minVal, successeur);

            utilite = Math.max(utilite, minVal);
            if (utilite >= beta) return utilite;
            alpha = Math.max(alpha, utilite);
        }
        return utilite;
    }

    // calcule l'utilité du MIN de l'état dans alpha-beta
    private int min(Grille grille, int alpha, int beta, ParametreRecherche param) {
        int joueur = grille.getJoeurCourant() == 1 ? 2 : 1;
        int utilite = Integer.MAX_VALUE;

        if (maxProfondeur == profondeur) {
            return param.calculerUtilite(grille);
        }

        for (Successeur successeur : getSuccesseurs(grille, joueur, param.getFonction())) {

            utilite = Math.min(utilite, max(successeur.grille, alpha, beta, param));
            if (utilite <= alpha) return utilite;
            beta = Math.min(beta, utilite);
        }
        return utilite;
    }

    // Retourne les coups possibles associés à la valeurs d'utilité résultante de la
    // grille après ce coup.
    private TreeMap<Grille, Position> enumererSuccesseurs(Grille grille, int joueur, Utilite fonction) {
        // Classe les grille en fonction de leurs utilité en ordre décroissant.
        TreeMap<Grille, Position> branchements = new TreeMap<>(new Comparator<Grille>() {
            @Override
            public int compare(Grille g1, Grille g2) {
                return fonction.evaluerUtilite(g2) - fonction.evaluerUtilite(g1);
            }
        });
        System.out.println(grille);
        System.out.println(joueur);
        System.out.println(grille.getPositionLibres());
        System.out.println();

        for (Position libre : grille.getPositionLibres()) {

            Grille prochain = grille.clone();
            prochain.set(libre, joueur);

            branchements.put(prochain, libre);
        }
        // for (Entry<Grille, Position> entry : branchements.entrySet()) {
        // System.out.println(entry.getValue());
        // }
        // System.out.println();

        return branchements;
    }

    private ArrayList<Successeur> getSuccesseurs(Grille grille, int joueur, Utilite fonction) {
        ArrayList<Successeur> successeurs = new ArrayList<Successeur>();
        for (Position libre : grille.getPositionLibres()){
            Grille successeur = grille.clone();
            successeur.set(libre, joueur);
            successeurs.add(new Successeur(successeur, libre));
        }
        ++profondeur;
        return successeurs;
    }
}
