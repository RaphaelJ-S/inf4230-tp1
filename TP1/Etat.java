/*
 * INF4230 - Intelligence artificielle
 * UQAM / Département d'informatique
 * 
 * Hiver 2013 / TP1
 * 
 */

import java.util.*;
import java.awt.geom.Point2D;


public class Etat implements Comparable<Etat> {

    // Référence sur la situation du Ramassage
    protected Ramassage ramassage;

    // Noyau de la représentation d'un état. Ici, on met tout ce qui rend l'état unique.
    /* Emplacement du van. */
    protected Emplacement emplacementVan;
    /* Array indicant l'emplacement de chaque colis. */
    protected Emplacement emplacementsColis[];
    /* Array indicant l'état de chargement de chaque colis par le van. */
    protected boolean colisRecuperes[];
    /* Etat de chargement du van */
    protected boolean colisCharge = false;

    // Variables pour l'algorithme A*.
    /* État précédent permettant d'atteindre cet état. */
    protected Etat            parent;
    /* Action à partir de parent permettant d'atteindre cet état. */
    protected String          actionFromParent;
    /* f=g+h. */
    protected double          f;
    /* Meilleur coût trouvé pour atteindre cet état à partir de l'état initial. */    
    protected double          g;
    /* Estimation du coût restant pour atteindre le but. */
    protected double          h;

    
    public Etat(final Ramassage ramassage){
        this.ramassage = ramassage;
    }

    /** Fonction retournant les états successeurs à partir de cet état.
     *  Aussi appelé fonction de transition.
     *  Cela permet d'explorer l'espace d'état (le graphe de recherche).
     */
    // À compléter.
    //
    //   - Pour chaque action possible :
    //   - Instanciez un objet Successeur s;
    //   - Copiez l'état courant (s.etat = clone()).
    //   - Créez une chaîne de caractère représentant l'action (s.action).
    //   - Affectez le coût de cette action (s.cout). 
    //   - Changez la valeur des variables appropriées dans s.etat.
    //   - Ajoutez s dans la liste successeurs.

    public Collection<Successeur> enumererEtatsSuccesseurs()
    {
        final LinkedList<Successeur> successeurs = new LinkedList<Successeur>();
        final ArrayList<Route> deplacementsPossibles = this.emplacementVan.routes;
        final String type = this.emplacementVan.type;
        if(type.equals("C") && !colisEstCharge()) {
            successeurs.add(new Successeur(chargerColis(), "C", this.ramassage.dureeChargement));
        }else if(type.equals("A")){
            int indexColis = peutDecharger();
            if(indexColis != -1)
                successeurs.add(new Successeur(dechargerColis(indexColis), "A", this.ramassage.dureeDechargement));
        }
        for(Route aEffectuer : deplacementsPossibles) {
            successeurs.add(new Successeur(effectuerDeplacement(aEffectuer), aEffectuer.destination.type, aEffectuer.destination.type.equals("-") ? 3 : 2));
        }

		
		// À compléter.
        
		//
        //   - Les actions possibles sont : 
        //   ----> emprunter une route de l'emplacement courant pour aller sur un emplacement voisin,
        //   ----> Ramasser un Colis lorsque le Van se trouve sur un emplacement de Colis pas encore transporté
        //   ----> Decharger un Colis lorsque le Van se trouve une un emplacement d'arrivee  
        
        //   - Pour toute action possible 
        //   --- Instancier un objet Successeur s;
        //   --- Cloner l'état courant dans la variable état du successeur (s.etat = clone()).
        //   --- Créer la chaîne de caractère représentant l'action dans s.action (voir plans fournis).
        //   -----> ex. d'un déplacement à l'ouest  "Ouest = Lieu " +  route.origine.nom + " -> Lieu " + route.destination.nom + ")"       
        //   --- Calculer le coût de cette action dans s.cout.
        //   -----> ex. pour un déplacement, le cout est 1 + le cout de l'emplacement
        //   --- Modifier la valeur des variables appropriées dans s.etat pour refléter l'effet de l'action (qu'est-ce qui change?) 
        //   --- Ajouter s dans la liste successeurs.
		
        return successeurs;
    }


    /* Crée un nouvel État en copiant le contenu pertinent de l'état actuel */
    @Override
    public Etat  clone()
    {
        final Etat etat2 = new Etat(ramassage);
        etat2.colisCharge = colisCharge;
        etat2.emplacementVan = emplacementVan;
        etat2.emplacementsColis = new Emplacement[emplacementsColis.length];
        for(int i = 0; i< emplacementsColis.length; i++)
            etat2.emplacementsColis[i] = emplacementsColis[i];

        etat2.colisRecuperes = new boolean[colisRecuperes.length];
        for(int i = 0; i< colisRecuperes.length; i++)
            etat2.colisRecuperes[i] = colisRecuperes[i];
        return etat2;
    }

    /* Relation d'ordre nécessaire pour TreeSet checkOpen . */
    @Override
    public int compareTo(final Etat o) {
        int c;
        c = this.emplacementVan.compareTo(o.emplacementVan);
        if(c!=0) return c;

        if(colisCharge == o.colisCharge)
            c=0;
        else if(colisCharge == true)
            return 1;
        else
            return -1;
       
        for(int i = 0; i< emplacementsColis.length; i++){
            c = (colisRecuperes[i]?1:0) - (o.colisRecuperes[i]?1:0);
            if(c!=0) return c;
            if(!colisRecuperes[i]){
                c = emplacementsColis[i].compareTo(o.emplacementsColis[i]);
                if(c!=0) return c;
            }
        }
        return 0;
    }

    @Override
    public String toString(){
        String s = "ETAT: f=" + f + "  g=" + g + "\n";
        s += "  Pos=" + emplacementVan.nom + "";
        for(int i = 0; i< emplacementsColis.length; i++){
            s += "\n  PosColis[i]=";
            s += emplacementsColis[i]==null ? "--" : emplacementsColis[i].nom;
        }
        s += "\n";
        return s;
    }

    //Détermine si le colis qui se trouve sur la case présente est déjà chargé ou non.
    private boolean colisEstCharge() {
        boolean colisCharge = true;
        int index = trouverIndexColis();
        if(index != -1) {
            colisCharge = this.colisRecuperes[index];
        }

        return colisCharge;
    }
    //Retourne l'index du premier colis à décharger s'il existe.
    private int peutDecharger() {
        int i = 0;
        int indexColis = -1;
        while(i < this.colisRecuperes.length && indexColis == -1){
            if(this.colisRecuperes[i] && this.emplacementsColis[i] != null) {
                indexColis = i;
            }
            i++;
        }
        return indexColis;
    }
    //Retourne l'index du colis si on se trouve sur une case qui en contient un.
    private int trouverIndexColis() {
        for(int i = 0; i < this.emplacementsColis.length; i++) {
            if(this.emplacementsColis[i].id == this.emplacementVan.id) {
                return i;
            }
        }
        return -1;
    }
    //Retourne la liste des index des colis chargés ou qui restent à charger selon le paramètre chercheCharger
    private List<Integer> trouverTousIndexColis(boolean chercheCharge) {
        ArrayList<Integer> indexColis = new ArrayList<>(this.colisRecuperes.length);
        for(int i = 0; i< colisRecuperes.length; i++) {
            if(colisRecuperes[i] == chercheCharge){
                indexColis.add(i);
            }
        }
        return indexColis;
    }

    //Retourne un Etat ayant effectué un déplacement.
    private Etat effectuerDeplacement(Route deplacement) {
        Etat copie = this.clone();
        copie.emplacementVan = deplacement.destination;
        copie.parent = this;
        for(Integer index : trouverTousIndexColis(true)) {
            copie.emplacementsColis[index] = deplacement.destination;
        }
        copie.actionFromParent = deplacement.destination.type;


        return copie;
    }

    //Retourne un État ayant effectué un chargement de colis.
    private Etat chargerColis() {
        Etat copie = this.clone();
        int index = trouverIndexColis();
        if(index == -1) return null;
        //Le colis ne se trouve plus à cet emplacement alors on le remplace par l'emplacement présent.
        copie.emplacementsColis[index] = copie.emplacementVan;
        //Je ne sais pas si cet attribut a de l'important vu qu'on peut avoir un nombre infini de colis.
        copie.colisRecuperes[index] = true;
        copie.parent = this;
        copie.actionFromParent = "C";
        //manque le calcul des fonctions f(x) = g(x) + h(x)

        return copie;
    }

    //Retourne un État ayant effectué un déchargement de colis.
    private Etat dechargerColis(int indexADecharger) {
        Etat copie = this.clone();
        //On décharge le premier colis dans la van.

        copie.emplacementsColis[indexADecharger] = null;
        copie.parent = this;
        copie.actionFromParent = "A";
        //manque le calcul des fonctions f(x) = g(x) + h(x)


        return copie;
    }

    
    public int calculCout(Emplacement e)
    {
        // int cout = 1;
        // if("-".equals(e.type)) cout = 2;
        return "-".equals(e.type) ? 2 : 1;
    }

}
