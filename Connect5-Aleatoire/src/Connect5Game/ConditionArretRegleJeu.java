package Connect5Game;

public class ConditionArretRegleJeu implements ConditionArret {

    @Override
    public boolean conditionArret(Grille grille) {
        boolean arret = grille.nbLibre() == 0;
        if (!arret) {
            GrilleVerificateur verif = new GrilleVerificateur();
            arret = verif.determineGagnant(grille) != 0;
        }
        return arret;
    }

}
