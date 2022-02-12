/*
 * INF4230 - Intelligence artificielle
 * UQAM / Département d'informatique
 * 
 * Hiver 2022 / TP1
 * 
 * Membres de l'équipe
 * 
 * ---------------------------
 *   + Code permanent : JACR26038907
 *   + Nom : Jacob-Simard
 *   + Prénom : Raphaël
 * ---------------------------
 *   + Code permanent : 
 *   + Nom : 
 *   + Prénom : 
 * --------------------------- 
 *   + Code permanent : 
 *   + Nom : 
 *   + Prénom : 
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

/**
 * Point d'entrée du programme.
 */
public class TP1 {

	// DECLARATION DU PLAN
	public static void main(String args[]) throws IOException {
		final ParseurH parseur = new ParseurH();
		BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
		String line = "";
		System.out.println("Donne le nom du fichier contenant ton mondeH.");
		System.out.println("Remarque: le fichier doit être à la racine du projet et dois se terminer par .txt");
		System.out.println("Exemple: ramassageH01.txt");

		// Recuperation du fichier map et parseur du fichier
		try {
			line = keyboard.readLine();
		} catch (IOException e) {
			System.err.println("err");
		}
		parseur.parse(line);

		// Création de l'objet évaluateur d'heuristique
		Heuristique h = new HeuristiqueV3(parseur.ramassage);
		System.out.println(
				"distance_heuristique(depart,arrivee) = " + h.estimerCoutRestant(parseur.etatInitial, parseur.but));

		// Tests de positionnement

		// parseur.etatInitial.emplacementVan =
		// parseur.ramassage.emplacements.get("9-6");
		// parseur.etatInitial.colisRecuperes = new boolean[] { true, true };
		// List<Successeur> test = (List<Successeur>)
		// parseur.etatInitial.enumererEtatsSuccesseurs();
		// parseur.etatInitial = test.get(0).etat;
		// test = (List<Successeur>) parseur.etatInitial.enumererEtatsSuccesseurs();

		// for (var e : test) {
		// System.out.println(e);

		// }
		/*
		 * Appel à l'algorithme A* : enregistrement du plan dans la List
		 * 
		 * À la fin de l'appel plan doit intégrer une liste de String donnant
		 * la suite d'instructions pour effectuer tous les deplacements selon
		 * le format donné par la chaine de caratères (cf. classe Route)
		 * "Ouest = Lieu " + route.origine.nom + " -> Lieu " + route.destination.nom +
		 * ")"
		 * 
		 * la strategie à effectuer est :
		 * Partir de l'entrée (emplacement actuel du Van)
		 * aller au premier Colis
		 * ramener le premier Colis au point de Ramassage
		 * aller chercher le second Colis
		 * le ramener à l'arrivee, etc..
		 *
		 */
		final List<String> plan = AEtoile.composerPlan(parseur.etatInitial, parseur.but, h);

		// ramassageH01.txt
		// urgenceH01.txt
		// Écriture du plan dans le fichier de sortie + affichage console
		final File f = new File("planH" + line.substring(10, 12) + ".txt");
		try {
			FileWriter fw = new FileWriter(f);
			System.out.println("Plan {");
			fw.write("Plan  { \n");
			fw.flush();
			for (String action : plan) {
				System.out.println(action + ";");
				fw.write(action + "; \n");
				fw.flush();
			}
			System.out.println("}");
			fw.write("}");
			fw.close();
		} catch (IOException e) {
			System.out.println("err");
		}

	}

}
