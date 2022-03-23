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
        if (delais <= 100)
            eval = new EvaluationChoixAleatoire();
        AlphaBetaConfig param = determinerBonParametres(delais);

        return eval.evaluer(grille, delais, param);
    }

    private AlphaBetaConfig determinerBonParametres(int delais) {
        int nbrSuccesseurs = 0;
        int nbrCoupsAVerifier = 0;

        if (delais <= 1000) {
            nbrSuccesseurs = 3;
            nbrCoupsAVerifier = 4;
        } else if (delais <= 3000) {
            nbrSuccesseurs = 5;
            nbrCoupsAVerifier = 5;
        } else {
            nbrSuccesseurs = 5;
            nbrCoupsAVerifier = 6;
        }

        Utilite fonction = new UtiliteTousJoueurs();
        ConditionArret arret = new ConditionArretRegleJeu(nbrCoupsAVerifier);
        AlphaBetaConfig param = new AlphaBetaConfig(arret, fonction, nbrSuccesseurs);

        return param;
    }

}
