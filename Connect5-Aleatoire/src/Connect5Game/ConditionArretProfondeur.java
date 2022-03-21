package Connect5Game;

public class ConditionArretProfondeur implements ConditionArret {
    private int profondeurMax;
    private int profondeurCourante;

    public ConditionArretProfondeur() {
        profondeurMax = 5;
        profondeurCourante = 0;
    }

    public ConditionArretProfondeur(int profondeurMax) {
        this.profondeurMax = profondeurMax;
        profondeurCourante = 0;
    }

    @Override
    public boolean conditionArret(Grille grille) {

        return grille.nbLibre() == 0 || profondeurMax >= profondeurCourante;
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
