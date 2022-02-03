/*
 * INF4230 - Intelligence artificielle
 * UQAM / Département d'informatique
 * 
 * Hiver 2017 / TP1
 * 
 */


import java.util.*;

public class AEtoile {

	// Démarche suggérée.
    // -- Commencez avec une heuristique minimaliste (toujours 0) et testez avec un problème TRÈS simple
    // -- Tracez les itérations sur la liste open sur la console avec System.out.println(..).
    // -- Pour chaque itération :
    // ---- Affichez le numéro d'itération.
    // ---- Affichez l'état e sélectionné par l'intération (les e.f affichés devraient croître);
	// -- Vérifiez le bon fonctionnement de la génération des états.
    // -- Vérifiez que e.f soit non-décroissant (>=) tout au long.
    //  Lorsque l'implémentation est terminée et validée, évaluez la pertinence d'un PriorityQueue.
	//  Élaborez des heuristiques plus réalistes


    public static List<String> composerPlan(final Etat etatInitial, final But but, final Heuristique heuristique){
  
		// Déclaration des variables pour les listes des états à examiner (open) et déjà examinés (closed)
        final TreeSet<Etat> open = new TreeSet<>(new AEtoile.FComparator());
		// Une liste auxiliaitre qui reflète fidèlement le contenu de open
        // nécessaire car la recherche dans open doit se faire d'abord par valeur de f et ensuite sur les variables définissant un état 
        // cela ne permet pas de repérer la présence du même état avec une valeur f différente
        final TreeSet<Etat> checkOpen = new TreeSet<>();
        final TreeSet<Etat> closed = new TreeSet<>();
		// la sortie est une liste d'actions, chacune étant une chaîne de caractères
        final LinkedList<String> plan =  new LinkedList<String>(); //variable à retourner
        
		 //  Shéma de l'algorithme A*    
		
        //initialisation des valeurs de distances (exacte et heuristique)
        etatInitial.g = 0;
        etatInitial.h = heuristique.estimerCoutRestant(etatInitial, but);
        etatInitial.f=etatInitial.g+etatInitial.h;

         //  Suite de l'algorithme A* - A COMPLÉTER
        
        //  -- Ajouter etatInitial dans open.
        //  -- Dans une boucle qui itère sur la liste open tant que celle-ci n'est pas vide.
        //  ---- Sortir d'open l'état e avec e.f minimal. 
        //  ---- Vérifier si l'état e satisfait le but. 
        //  ------ Si oui, sortir de la boucle et composer le plan optimal
        //  ---- Ajouter e dans closed.
        //  ---- Générer les successeurs de e.
        //  ---- Pour tout état-successeur s.etat :
        //  ------ Calculer s.etat.g puis s.etat.f
        //  ------ Vérifier que s.etat n'a pas d'état équivalent dans closed. (si oui, alors l'ignorer car l'heuristique admissible nous dit que s.etat aura un f supérieur ou égal)
        //  ------   Vérifier si s.etat a un état équivalent dans open.
        //  --------   Si un tel état existe dans open, comparer les valeurs de f et retenir la moindre des deux
        //  ------   Ajoutez s.etat dans open si aucun des cas précédents n'est présent.

        return plan;
    }
	//  Tout à la fin, n'oubliez pas de commenter les affichages de traçage

	// Comparatrice pour les états qui se base sur les valeurs de f
    static class FComparator implements Comparator<Etat> {
        @Override
        public int compare(Etat e1, Etat e2) {
            if(e1.f<e2.f)
                return -1;
            if(e1.f>e2.f)
                return +1;

            return e1.compareTo(e2);
        }
    }
}
