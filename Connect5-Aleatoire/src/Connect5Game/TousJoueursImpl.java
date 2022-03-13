package Connect5Game;

import java.util.ArrayList;
import java.util.HashMap;

public class TousJoueursImpl implements Utilite {

    /**
     * Retourne la valeur d'utilité prenant en compte toutes les possibilités de
     * victoires
     * pour un joueur contrastées avec les possibilités de victoire pour l'autre
     * joueur.
     * 
     * @param grille La grille à évaluer.
     * @return La valeur d'utilité de <code>grille</code>
     */
    @Override
    public int evaluerUtilite(Grille grille) {
        int utilite = 0;
        HashMap<Integer, ArrayList<Position>> cases = trouverCasesSelectionnees(grille);
        ArrayList<Position> j1 = cases.get(1);
        ArrayList<Position> j2 = cases.get(2);
        for (Position evalPositif : j1) {
            utilite += utilitePosition(grille, evalPositif, 1);
        }
        for (Position evalNegatif : j2) {
            utilite -= utilitePosition(grille, evalNegatif, 2);
        }
        return utilite;
    }

    // Identifie les cases sélectionnées et les retournent dans une hashmap où la
    // clé est le nombre du joueur avec ses coups associés.
    private HashMap<Integer, ArrayList<Position>> trouverCasesSelectionnees(Grille grille) {
        HashMap<Integer, ArrayList<Position>> cases = new HashMap<>();

        for (int i = 0; i < grille.getData().length; i++) {
            for (int j = 0; i < grille.getData()[i].length; j++) {
                if (grille.getData()[i][j] == 1) {
                    cases.putIfAbsent(1, new ArrayList<Position>());
                    cases.get(1).add(new Position(i, j));
                } else if (grille.getData()[i][j] == 2) {
                    cases.putIfAbsent(2, new ArrayList<Position>());
                    cases.get(2).add(new Position(i, j));
                }
            }
        }

        return cases;
    }

    // Calcule l'utilité d'une position. vérifie toutes les combinaisons gagnantes
    // possibles
    // diagonales, verticales et horizontales.
    private int utilitePosition(Grille grille, Position aEvaluer, int joueur) {
        int utilite = calculerDiagonaleGD(grille, aEvaluer, joueur);

        return utilite;
    }

    // calcul diagonale haut gauche vers bas droit
    private int calculerDiagonaleGD(Grille grille, Position aEvaluer, int joueur) {
        int limSup = 0;
        int limInf = grille.getData().length - 1;
        int limGauche = 0;
        int limDroite = grille.getData()[0].length - 1;
        int debut = Math.min(
                aEvaluer.ligne - 4 <= limSup ? 0 : aEvaluer.ligne - 4,
                aEvaluer.colonne - 4 <= limGauche ? 0 : aEvaluer.colonne - 4);
        Position curr = new Position(aEvaluer.ligne - debut, aEvaluer.colonne - debut);
        int fin = Math.min(
                aEvaluer.ligne + 4 >= limInf ? limInf : aEvaluer.ligne + 4,
                aEvaluer.colonne + 4 >= limDroite ? limDroite : aEvaluer.colonne + 4);
        boolean continuer = true;
        int utilite = 0;
        while (curr.ligne < Math.min(aEvaluer.ligne + 1, fin) && continuer) {
            for (int i = 0; i < 5; i++) {
                if (curr.ligne + i > fin || curr.colonne + i > fin) {
                    continuer = false;
                    break;
                }
                if (grille.getData()[i][i] != joueur || grille.getData()[i][i] != 0) {
                    continuer = false;
                    break;
                }

                if (i == 4 && (grille.getData()[i][i] == 0 || grille.getData()[i][i] == joueur)) {
                    utilite += 1;
                }

            }
        }

        return utilite;
    }

}
