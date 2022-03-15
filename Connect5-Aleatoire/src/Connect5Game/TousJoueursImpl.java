package Connect5Game;

import java.util.ArrayList;
import java.util.HashMap;

public class TousJoueursImpl implements Utilite {

    enum Directions
    {
        hautGauche,
        haut,
        hautDroite,
        basGauche,
        bas,
        basDroite
    }

    /**
     * Retourne la valeur d'utilité prenant en compte toutes les possibilités de
     * victoires
     * pour un joueurCourant contrastées avec les possibilités de victoire pour l'autre
     * joueurCourant.
     * 
     * @param grille La grille à évaluer.
     * @return La valeur d'utilité de <code>grille</code>
     */
    @Override
    public int evaluerUtilite(Grille grille) {
        int joueurCourant = grille.getJoeurCourant();
        int adversaire = joueurCourant == 1 ? 2 : 1;

        int utilite = 0;
        HashMap<Integer, ArrayList<Position>> cases = trouverCasesSelectionnees(grille);

        ArrayList<Position> j1 = cases.get(1);
        ArrayList<Position> j2 = cases.get(2);

        if(j1 != null)
        {
            for (Position evalPositif : j1) {
                utilite += evaluerPosition(grille, evalPositif, joueurCourant);
            }
        }
        if(j2 != null)
        {
            for (Position evalNegatif : j2) {
                utilite -= evaluerPosition(grille, evalNegatif, adversaire);
            }
        }
        return utilite;
    }

    // Identifie les cases sélectionnées et les retournent dans une hashmap où la
    // clé est le nombre du joueurCourant avec ses coups associés.
    private HashMap<Integer, ArrayList<Position>> trouverCasesSelectionnees(Grille grille) {
        HashMap<Integer, ArrayList<Position>> cases = new HashMap<>();

        for (int i = 0; i < grille.getData().length; ++i) {
            for (int j = 0; j < grille.getData()[i].length; ++j) {
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
    private int utilitePosition(Grille grille, Position aEvaluer, int joueurCourant) {
        return calculerDiagonaleGD(grille, aEvaluer, joueurCourant) +
                calculerDiagonaleDG(grille, aEvaluer, joueurCourant) +
                calculerHorizontale(grille, aEvaluer, joueurCourant) +
                calculerVerticale(grille, aEvaluer, joueurCourant);

    }

    // Pour evaluer l'utilité d'une position, on regarde chacune des directions à partir de cette position
    // On part de la valeur 1, si une direction offre une possibilité de victoire, on fait 1 * 10^nb jetons du joueurCourant courant dans cette direction
    // Si une direction n'a pas de possibilité de victoire, à cause d'un jeton de l'adversaire ou une case non-accessible, cette direction vaut 0.
    private int evaluerPosition(Grille grille, Position aEvaluer, int joueurCourant)
    {
        int utilite = 0;

        for(Directions direction : Directions.values())
        {
            int x = aEvaluer.ligne;
            int y = aEvaluer.colonne;
            int distance = 0;
            int nbJetonsDirection = 0;
            boolean caseValide = false;

            do
            {
                switch(direction)
                {
                    // On commence par ajuster notre curseur sur la prochaine case à valider
                    case hautGauche: --x; ++y; break;
                    case haut: ++y; break;
                    case hautDroite: ++x; ++y; break;
                    case basGauche: --x; --y; break;
                    case bas: --y; break;
                    case basDroite: ++x; --y; break;
                    case default: break;
                }
                ++distance;
                Position positionAValider = new Position(x,y);
                caseValide = validerCase(grille, positionAValider, joueurCourant);

                if(caseValide)
                {
                    if(grille.get(positionAValider) == joueurCourant)
                    {
                        ++nbJetonsDirection;
                    }
                    if(distance == 4) // Direction où il est possible de gagner
                    {
                        utilite += 1 * Math.pow(10, nbJetonsDirection);
                    }
                }
            }
            while(caseValide);
        }
        return utilite;
    }

    private boolean validerCase(Grille grille, Position aEvaluer, int joueurCourant)
    {
        int limHaut = 0;
        int limBas = grille.getData().length - 1;
        int limGauche = 0;
        int limDroite = grille.getData()[0].length - 1;

        return aEvaluer.ligne >= limGauche && aEvaluer.ligne < limDroite && aEvaluer.colonne >= limHaut && aEvaluer.colonne < limBas && (grille.get(aEvaluer) == 0 || grille.get(aEvaluer) == joueurCourant);
    }

    // calcul diagonale haut gauche vers bas droit
    private int calculerDiagonaleGD(Grille grille, Position aEvaluer, int joueurCourant) {
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
                if (grille.getData()[iter_x][iter_y] != joueurCourant && grille.getData()[iter_x][iter_y] != 0) {
                    curr.ligne = iter_x;
                    curr.colonne = iter_y;
                    break;
                }

                if ((i == 4) && (grille.getData()[iter_x][iter_y] == 0 || grille.getData()[iter_x][iter_y] == joueurCourant)) {
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
    private int calculerDiagonaleDG(Grille grille, Position aEvaluer, int joueurCourant) {
        int utilite = 0;

        return utilite;
    }

    // calcul diagonale haut doit vers bas gauche
    private int calculerHorizontale(Grille grille, Position aEvaluer, int joueurCourant) {
        int utilite = 0;

        return utilite;
    }

    // calcul diagonale haut doit vers bas gauche
    private int calculerVerticale(Grille grille, Position aEvaluer, int joueurCourant) {
        int utilite = 0;

        return utilite;
    }
}
