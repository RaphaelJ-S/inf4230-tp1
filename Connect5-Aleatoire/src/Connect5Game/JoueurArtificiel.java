package Connect5Game;

public class JoueurArtificiel implements Joueur {

    /**
     * Voici la fonction à modifier.
     * Évidemment, vous pouvez ajouter d'autres fonctions dans JoueurArtificiel.
     * Vous pouvez aussi ajouter d'autres classes, mais elles doivent être
     * ajoutées dans le package planeteH_2.ia.
     * Vous ne pouvez pas modifier les fichiers directement dans planeteH_2., car
     * ils seront écrasés.
     * 
     * @param grille Grille reçu (état courrant). Il faut ajouter le prochain coup.
     * @param delais Délais de rélexion en temps réel.
     * @return Retourne le meilleur coup calculé.
     */

    @Override
    public Position getProchainCoup(Grille grille, int delais) {

        // Dépendant du délais, on pourrait décider d'effectuer un algorithme différent.
        // par exemple, on pourrait limiter la profondeur de la recherche.
        // EvaluationChoix eval = new AleatoireImpl();
        EvaluationChoix eval = new AlphaBetaImpl();

        return eval.evaluer(grille, delais);
    }

}
