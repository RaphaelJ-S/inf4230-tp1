 * INF4230 - Intelligence artificielle
 * UQAM / DÃ©partement d'informatique
 * 
 * Hiver 2017 / TP1
 * 
 */

/** Contient les Ã©lÃ©ments pertinent Ã  propos d'un successeur (une transition
 *  dans le graphe) : l'Ã©tat suivant, le cout de l'action et l'action.
 */

public class Successeur {

    protected Etat     etat;
    protected String   action;
    protected double   cout;
    
    public Successeur(Etat etat, String action, double cout) {
        this.etat = etat;
        this.action = action;
        this.cout = cout;
        this.etat.actionFromParent = action;
    }
    
    @Override
    public String toString() {
        return action + "|" + cout + "|" + etat.toString();
    }
}
