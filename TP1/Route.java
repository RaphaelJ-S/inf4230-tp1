/*
 * INF4230 - Intelligence artificielle
 * UQAM / Département d'informatique
 * 
 * Hiver 2019 / TP1
 * 
 */

public class Route {

    protected Emplacement  origine;
    protected Emplacement  destination;
	
	
    public Route(final Emplacement origine, final Emplacement destination){
        this.origine = origine;
        this.destination = destination;
    }

}
