package Connect5Game;

import java.util.ArrayList;
import java.util.HashMap;

public class TousJoueursImpl implements Utilite {

    enum Directions {
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
     * pour un joueurCourant contrastées avec les possibilités de victoire pour
     * l'autre
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

        ArrayList<Position> courant = cases.get(joueurCourant);
        ArrayList<Position> enemy = cases.get(adversaire);

        if (courant != null) {
            for (Position evalPositif : courant) {
                utilite += evaluerPosition(grille, evalPositif, joueurCourant);
            }
        }
        if (enemy != null) {
            for (Position evalNegatif : enemy) {
                utilite -= evaluerPosition(grille, evalNegatif, adversaire);
            }
        }
        return utilite;
    }

    // Identifie les cases sélectionnées et les retournent dans une hashmap où la
    // clé est le nombre du joueur avec ses coups associés.
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

    // Pour evaluer l'utilité d'une position, on regarde chacune des directions à
    // partir de cette position
    // On part de la valeur 1, si une direction offre une possibilité de victoire,
    // on fait 1 * 10^nb jetons du joueurCourant courant dans cette direction
    // Si une direction n'a pas de possibilité de victoire, à cause d'un jeton de
    // l'adversaire ou une case non-accessible, cette direction vaut 0.
    private int evaluerPosition(Grille grille, Position aEvaluer, int joueurCourant) {
        int utilite = 0;

        for (Directions direction : Directions.values()) {
            int x = aEvaluer.ligne;
            int y = aEvaluer.colonne;
            int distance = 0;
            int nbJetonsDirection = 0;
            boolean caseValide = false;

            do {
                switch (direction) {
                    // On commence par ajuster notre curseur sur la prochaine case à valider
                    case hautGauche:
                        --x;
                        ++y;
                        break;
                    case haut:
                        ++y;
                        break;
                    case hautDroite:
                        ++x;
                        ++y;
                        break;
                    case basGauche:
                        --x;
                        --y;
                        break;
                    case bas:
                        --y;
                        break;
                    case basDroite:
                        ++x;
                        --y;
                        break;
                    default:
                        break;
                }
                ++distance;
                Position positionAValider = new Position(x, y);
                caseValide = validerCase(grille, positionAValider, joueurCourant);

                if (caseValide) {
                    if (grille.get(positionAValider) == joueurCourant) {
                        ++nbJetonsDirection;
                    }
                    if (distance == 4) // Direction où il est possible de gagner
                    {
                        utilite += 1 * Math.pow(10, nbJetonsDirection);
                    }
                }
            } while (caseValide);
        }
        return utilite;
    }

    private boolean validerCase(Grille grille, Position aEvaluer, int joueurCourant) {
        int limHaut = 0;
        int limBas = grille.getData().length - 1;
        int limGauche = 0;
        int limDroite = grille.getData()[0].length - 1;
        System.out.println("ligne : " + aEvaluer.ligne);
        System.out.println("colonne : " + aEvaluer.colonne);

        return aEvaluer.colonne >= limGauche && aEvaluer.colonne < limDroite && aEvaluer.ligne >= limHaut
                && aEvaluer.ligne < limBas && (grille.get(aEvaluer) == 0 || grille.get(aEvaluer) == joueurCourant);
    }

}
