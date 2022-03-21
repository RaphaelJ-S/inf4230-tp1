package Connect5Game;

public class ParametreRecherche {
    private ConditionArret conditions;
    private Utilite fonction;
    private int nbrSuccesseurs;

    public ParametreRecherche(ConditionArret cond, Utilite fonction, int nbrSuccesseurs) {
        conditions = cond;
        this.fonction = fonction;
        this.nbrSuccesseurs = nbrSuccesseurs;
    }

    public boolean verifierCondition(Grille grille) {
        return conditions.conditionArret(grille);
    }

    public int calculerUtilite(Grille grille) {
        return fonction.evaluerUtilite(grille);
    }

    public int getNbrSuccesseurs() {
        return nbrSuccesseurs;
    }

    public ConditionArret getConditions() {
        return conditions;
    }

    public Utilite getFonction() {
        return fonction;
    }

}
