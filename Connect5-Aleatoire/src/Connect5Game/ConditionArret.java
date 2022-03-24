package Connect5Game;

/**
 * Conditions d'arrêt pour un algorithme de recherche du jeu Connect5.
 */
public interface ConditionArret {

    /**
     * Évaluation des conditions d'arrêt.
     * 
     * @param grille La grille à évaluer.
     * @return Si la grille respecte les condition.
     */
    public boolean conditionArret(Grille grille);

    /**
     * Incrémentation de la profondeur si l'algorithme doit le considérer.
     */
    public void incrementerProfondeur();

    /**
     * Décrémentation de la profondeur si l'algorithme doit le considérer.
     */
    public void decrementerProfondeur();

}
