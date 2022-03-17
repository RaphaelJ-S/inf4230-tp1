package Connect5Game;

public class ConditionArretToutesCases implements ConditionArret {
    private int profondeurMax = Integer.MAX_VALUE;

    @Override
    public boolean conditionArret(Grille grille) {
        return grille.getPositionLibres().size() == 0;
    }

    @Override
    public void setProfondeurMax(int profondeurMax) {
        this.profondeurMax = profondeurMax;

    }

}
