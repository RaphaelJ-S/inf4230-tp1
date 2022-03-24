package Connect5Game;

/**
 * Condition d'arrêt prenant en compte les règle du jeu et la profondeur de
 * l'algorithme.
 */
public class ConditionArretRegleJeu implements ConditionArret {
    private int profondeurMax;
    private int profondeurCourante;

    public ConditionArretRegleJeu() {
        profondeurCourante = 0;
        profondeurMax = Integer.MAX_VALUE;
    }

    public ConditionArretRegleJeu(int profondeurMax) {
        profondeurCourante = 0;
        this.profondeurMax = profondeurMax;
    }

    @Override
    public boolean conditionArret(Grille grille) {
        boolean arret = grille.nbLibre() == 0 || profondeurMax < profondeurCourante;
        if (!arret) {
            GrilleVerificateur verif = new GrilleVerificateur();
            arret = verif.determineGagnant(grille) != 0;

        }
        return arret;
    }

    @Override
    public void incrementerProfondeur() {
        profondeurCourante++;

    }

    @Override
    public void decrementerProfondeur() {
        profondeurCourante--;

    }

}
