package Connect5Game;

public class ConditionArretToutesCases implements ConditionArret {

    @Override
    public boolean conditionArret(Grille grille) {
        return grille.getPositionLibres().size() == 0;
    }

}
