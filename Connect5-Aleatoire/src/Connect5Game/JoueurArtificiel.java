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
        EvaluationChoix eval = new EvaluationChoixAlphaBeta();
        if (delais <= 100) {
            eval = new EvaluationChoixAleatoire();
        }

        // Dépendant du délais, on pourrait décider d'effectuer un algorithme différent.
        // par exemple, on pourrait limiter la profondeur de la recherche.
        // EvaluationChoix eval = new AleatoireImpl();

        ParametreRecherche param = determinerBonParametres(delais);

        return eval.evaluer(grille, delais, param);
    }

    private ParametreRecherche determinerBonParametres(int delais) {
        int nbrSuccesseurs = 0;
        int nbrCoupsAVerifier = 0;
        if (delais <= 1000) {
            nbrSuccesseurs = 1;
            nbrCoupsAVerifier = 12;
        } else if (delais <= 3000) {
            nbrSuccesseurs = 3;
            nbrCoupsAVerifier = 6;
        } else {
            nbrSuccesseurs = 2;
            nbrCoupsAVerifier = 14;
        }
        Utilite fonction = new UtiliteCopie();
        ConditionArret arret = new ConditionArretRegleJeu(nbrCoupsAVerifier);
        ParametreRecherche param = new ParametreRecherche(arret, fonction, nbrSuccesseurs);

        return param;
    }

}
