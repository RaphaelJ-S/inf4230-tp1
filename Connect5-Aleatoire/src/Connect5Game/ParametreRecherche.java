package Connect5Game;

public class ParametreRecherche {
    private ConditionArret conditions;
    private Utilite fonction;

    public ParametreRecherche(ConditionArret cond, Utilite fonction) {
        conditions = cond;
        this.fonction = fonction;

    }

    public boolean verifierCondition(Grille grille) {
        return conditions.conditionArret(grille);
    }

    public int calculerUtilite(Grille grille) {
        return fonction.evaluerUtilite(grille);
    }

    public ConditionArret getConditions() {
        return conditions;
    }

    public Utilite getFonction() {
        return fonction;
    }

}
