package Connect5Game;

public class UtiliteTousJoueurs implements Utilite {

    @Override
    public int evaluerUtilite(Grille grille) {
        int joueurCourant = grille.getJoeurCourant();
        int adversaire = joueurCourant == 1 ? 2 : 1;
        utilitesJoueurs[1] = 0;
        utilitesJoueurs[2] = 0;
        calculerUtilite(grille);

        return utilitesJoueurs[joueurCourant] - utilitesJoueurs[adversaire];
    }

    private int[] calculerUtilite(Grille grille) {
        lastValue = 0; // reset status

        // Horizontale
        for (int l = 0; l < grille.data.length; ++l) {
            for (int c = 0; c < grille.data[0].length; ++c)
                verifierCase(grille.data[l][c]);
            verifierCase(20);
        }

        // Verticale
        for (int c = 0; c < grille.data[0].length; ++c) {
            for (int l = 0; l < grille.data.length; ++l)
                verifierCase(grille.data[l][c]);
            verifierCase(20);
        }

        // Diagonale du haut vers le bas
        for (int c = -grille.data.length; c < grille.data[0].length; ++c) {
            int c2 = c;
            int l = 0;
            if (c2 < 0) {
                l = -c2;
                c2 = 0;
            }
            for (; c2 < grille.data[0].length && l < grille.data.length; ++c2, ++l) {
                verifierCase(grille.data[l][c2]);
            }
            verifierCase(20);
        }

        // Diagonale du bas vers le haut
        for (int c = -grille.data.length; c < grille.data[0].length; ++c) {
            int c2 = c;
            int l = grille.data.length - 1;
            if (c2 < 0) {
                l += c2;
                c2 = 0;
            }
            for (; c2 < grille.data[0].length && l >= 0; ++c2, --l)
                verifierCase(grille.data[l][c2]);
            verifierCase(20);
        }

        return utilitesJoueurs;
    }

    private void verifierCase(int value) {
        if (value != lastValue) {

            if (estJoueur(lastValue)) {
                finiParZero = estZero(value);

                if (finiParZero || suiteZero + count >= 5) {
                    // int augmentation = (finiParZero || suiteZero > 0) && count > 1 ? 2 : 1;
                    utilitesJoueurs[lastValue] += (10 << count);
                }
                suiteZero = finiParZero ? 1 : 0;

            } else if (estBloc(lastValue)) {

                suiteZero = estZero(value) ? 1 : 0;
            } else {
                suiteZero = 0;
            }
            count = estJoueur(value) ? 1 : 0;
            lastValue = value;

        } else {
            if (estJoueur(value))
                ++count;
            else if (value == 0)
                ++suiteZero;
            lastValue = value;
        }
    }

    private boolean estJoueur(int valeur) {
        return valeur == 1 || valeur == 2;
    }

    private boolean estZero(int valeur) {
        return valeur == 0;
    }

    private boolean estBloc(int valeur) {
        return valeur > 2;
    }

    protected boolean finiParZero = false;
    protected int suiteZero = 0;
    protected int[] utilitesJoueurs = new int[3];
    protected int lastValue = 0;
    protected int count = 0;

}