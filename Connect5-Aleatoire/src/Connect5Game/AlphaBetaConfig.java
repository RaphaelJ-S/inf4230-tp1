package Connect5Game;

/**
 * Paramètres de recherche pour un algorithme de recherche de coup dans une
 * grille.
 */
public class AlphaBetaConfig {
    private ConditionArret conditions; // Les conditions d'arrêt de l'algorithme
    private Utilite fonction; // La fonction d'utilité pour l'évaluation d'une grille.
    private int nbrSuccesseurs; // Le nombre de successeurs à générer à chaque pas.

    public AlphaBetaConfig(ConditionArret cond, Utilite fonction, int nbrSuccesseurs) {
        conditions = cond;
        this.fonction = fonction;
        this.nbrSuccesseurs = nbrSuccesseurs;
    }

    /**
     * Vérification des condition de la grille.
     * 
     * @param grille La grille à vérifier.
     * @return Si la grille est conforme.
     */
    public boolean verifierCondition(Grille grille) {
        return conditions.conditionArret(grille);
    }

    /**
     * Calcul d'utilité de la grille.
     * 
     * @param grille La grille à évaluer.
     * @return La valeur d'utilité de la grille.
     */
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
