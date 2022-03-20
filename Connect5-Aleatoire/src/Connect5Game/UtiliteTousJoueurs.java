package Connect5Game;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.HashMap;

public class UtiliteTousJoueurs implements Utilite {

    enum Directions {
        gauche,
        hautGauche,
        haut,
        basGauche,
        droite,
        basDroite,
        bas,
        hautDroite
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
    // on fait 10^nb jetons du joueurCourant courant dans cette direction
    // Si une direction n'a pas de possibilité de victoire, à cause d'un jeton de
    // l'adversaire ou une case non-accessible, cette direction vaut 0.
    private int evaluerPosition(Grille grille, Position aEvaluer, int joueurCourant) {
        final int distanceMaximale = 4; // Distance maximale par rapport à la position à evaluer dans le but d'atteindre une combinaison de 5 jetons et gagner la partie

        LinkedList<Directions> directionsOpposees = new LinkedList<Directions>(EnumSet.range(Directions.droite, Directions.hautDroite));
        int utilite = 0;
    
        for (Directions direction : EnumSet.range(Directions.gauche, Directions.basGauche)) {
            Position positionDirection = aEvaluer;
            Position positionDirectionOpposee = aEvaluer;
            LinkedList<Position> positionsOpposees = new LinkedList<Position>();
            int distance = 0;
            int distanceOpposee = 0;
            int nbJetons = 0;
            boolean caseValide = false;

            // On commence par valider les 4 positions dans la direction opposée ainsi que le nombre de jetons alliés s'y trouvant
            Directions directionOpposee = directionsOpposees.poll(); // On synchronise la nouvelle direction avec sa direction opposée
            do {
                positionDirectionOpposee = trouverProchainePositionAValider(directionOpposee, positionDirectionOpposee);
                caseValide = validerCase(grille, positionDirectionOpposee, joueurCourant);
                if (caseValide) {
                    positionsOpposees.addFirst(positionDirectionOpposee);
                    if (grille.get(positionDirectionOpposee) == joueurCourant) ++nbJetons;
                    ++distanceOpposee;
                }
            } while (caseValide && distanceOpposee < distanceMaximale);
            // On vérifie ensuite toutes les combinaisons gagnante entre une direction et sa direction opposée
            do {
                if (distance + distanceOpposee == distanceMaximale){
                    // On ajoute une valeur d'utilité uniquement quand il est possible de gagner pour une combinaison (pas de case inaccessible ou utilisée par l'adversaire)
                    utilite += Math.pow(10, nbJetons);
                    if (distance == distanceMaximale) break; // Uniquement besoin de calculer l'utilité pour le dernier cas possible, on peut donc sortir maintenant
                    if (grille.get(positionsOpposees.poll()) == joueurCourant) --nbJetons;
                    --distanceOpposee;
                }
                positionDirection = trouverProchainePositionAValider(direction, positionDirection);
                caseValide = validerCase(grille, positionDirection, joueurCourant);
                if (caseValide) {
                    if (grille.get(positionDirection) == joueurCourant) ++nbJetons;
                    ++distance;
                }
            } while (caseValide && distance <= distanceMaximale);
        }
        return utilite;
    }

    private Position trouverProchainePositionAValider(Directions direction, Position positionPrecedente)
    {
        int x = positionPrecedente.colonne;
        int y = positionPrecedente.ligne;

        switch (direction) {
            // On ajuste notre curseur sur la prochaine case à valider
            case gauche:
                --x;
                break;
            case hautGauche:
                --x;
                --y;
                break;
            case haut:
                --y;
                break;
            case hautDroite:
                ++x;
                --y;
                break;
            case droite:
                ++x;
                break;
            case basGauche:
                --x;
                ++y;
                break;
            case bas:
                ++y;
                break;
            case basDroite:
                ++x;
                ++y;
                break;
            default:
                break;
        }

        return new Position(y, x);
    }

    private boolean validerCase(Grille grille, Position aEvaluer, int joueurCourant) {
        int limHaut = 0;
        int limBas = grille.getData().length - 1;
        int limGauche = 0;
        int limDroite = grille.getData()[0].length - 1;

        return aEvaluer.colonne >= limGauche && aEvaluer.colonne < limDroite && aEvaluer.ligne >= limHaut
                && aEvaluer.ligne < limBas && (grille.get(aEvaluer) == 0 || grille.get(aEvaluer) == joueurCourant);
    }

}
