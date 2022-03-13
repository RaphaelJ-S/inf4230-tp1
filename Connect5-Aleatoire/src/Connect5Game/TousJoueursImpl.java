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
        return calculerDiagonaleGD(grille, aEvaluer, joueur) +
                calculerDiagonaleDG(grille, aEvaluer, joueur) +
                calculerHorizontale(grille, aEvaluer, joueur) +
                calculerVerticale(grille, aEvaluer, joueur);

    }

    // calcul diagonale haut gauche vers bas droit
    private int calculerDiagonaleGD(Grille grille, Position aEvaluer, int joueur) {
        int limSup = 0;
        int limInf = grille.getData().length - 1;
        int limGauche = 0;
        int limDroite = grille.getData()[0].length - 1;
        int x = aEvaluer.ligne;
        int y = aEvaluer.colonne;
        int utilite = 0;

        // Trouver le début à vérifier
        while (x > limSup && y > limGauche && x > aEvaluer.ligne - 4 && y > aEvaluer.colonne - 4) {
            x--;
            y--;
        }
        Position curr = new Position(x, y);
        // Trouver la fin à vérifier
        while (x < limInf && y < limDroite && x < aEvaluer.ligne + 4 && y < aEvaluer.colonne + 4) {
            x++;
            y++;
        }
        while (curr.ligne <= aEvaluer.ligne && curr.colonne <= aEvaluer.colonne) {
            for (int i = 0; i < 5; i++) {
                int iter_x = curr.ligne + i;
                int iter_y = curr.colonne + i;
                if (iter_x > x || iter_y > y) {
                    break;
                }
                if (grille.getData()[iter_x][iter_y] != joueur && grille.getData()[iter_x][iter_y] != 0) {
                    curr.ligne = iter_x;
                    curr.colonne = iter_y;
                    break;
                }

                if ((i == 4) && (grille.getData()[iter_x][iter_y] == 0 || grille.getData()[iter_x][iter_y] == joueur)) {
                    utilite += 1;
                }

            }
            curr.ligne += 1;
            curr.colonne += 1;
            if (x - curr.ligne < 4 || y - curr.colonne < 4)
                break;
        }

        return utilite;
    }

    // calcul diagonale haut doit vers bas gauche
    private int calculerDiagonaleDG(Grille grille, Position aEvaluer, int joueur) {
        int utilite = 0;

        return utilite;
    }

    // calcul diagonale haut doit vers bas gauche
    private int calculerHorizontale(Grille grille, Position aEvaluer, int joueur) {
        int utilite = 0;

        return utilite;
    }

    // calcul diagonale haut doit vers bas gauche
    private int calculerVerticale(Grille grille, Position aEvaluer, int joueur) {
        int utilite = 0;

        return utilite;
    }
}
