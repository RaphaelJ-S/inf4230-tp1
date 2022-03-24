package Connect5Game;

/**
 * Fonction d'utilité d'une grille.
 */
public interface Utilite {

    /**
     * Calcule l'utilité d'une grille.
     * 
     * @param grille La grille à évaluer.
     * @return La valeur d'utilité calculée.
     */
    public int evaluerUtilite(Grille grille);
}
