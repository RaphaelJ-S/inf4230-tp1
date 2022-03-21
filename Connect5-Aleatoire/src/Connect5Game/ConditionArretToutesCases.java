package Connect5Game;

public class ConditionArretToutesCases implements ConditionArret {

    @Override
    public boolean conditionArret(Grille grille) {
        return grille.getPositionLibres().size() == 0;
    }

    @Override
    public void incrementerProfondeur() {
        return;

    }

    @Override
    public void decrementerProfondeur() {
        return;
    }

}
