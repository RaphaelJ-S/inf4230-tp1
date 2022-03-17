package Connect5Game;

public interface ConditionArret {
    public boolean conditionArret(Grille grille);

    public void setProfondeurMax(int profondeurMax);

    default int profondeurCourante(Grille grille) {
        int profondeur = 0;
        for (int i = 0; i < grille.getData().length; i++) {
            for (int j = 0; j < grille.getData()[i].length; j++) {
                if (grille.getData()[i][j] == 1 || grille.getData()[i][j] == 2) {
                    profondeur++;
                }
            }
        }
        return profondeur;
    }

}
