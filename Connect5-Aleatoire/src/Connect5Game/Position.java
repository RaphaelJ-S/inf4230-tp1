/*
 * INF4230 - Intelligence artificielle.
 * UQAM - Département d'informatique
 */

package Connect5Game;

/**
 *
 */
public class Position {
    public Position() {

    }

    public Position(int l, int c) {
        ligne = l;
        colonne = c;
    }

    @Override
    public String toString() {
        return ligne + " " + colonne;
    }

    public int ligne, colonne;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + colonne;
        result = prime * result + ligne;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Position other = (Position) obj;
        if (colonne != other.colonne)
            return false;
        if (ligne != other.ligne)
            return false;
        return true;
    }

}
