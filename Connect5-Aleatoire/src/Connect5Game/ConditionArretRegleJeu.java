package Connect5Game;

public class ConditionArretRegleJeu implements ConditionArret {
    private int profondeurMax = Integer.MAX_VALUE;

    @Override
    public boolean conditionArret(Grille grille) {
        boolean arret = grille.nbLibre() == 0 && profondeurMax >= profondeurCourante(grille);
        if (!arret) {
            GrilleVerificateur verif = new GrilleVerificateur();
            arret = verif.determineGagnant(grille) != 0;
        }
        return arret;
    }

    @Override
    public void setProfondeurMax(int profondeurMax) {
        this.profondeurMax = profondeurMax;

    }

}
