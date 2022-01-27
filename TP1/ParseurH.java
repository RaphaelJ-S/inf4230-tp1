
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

public class ParseurH {
	protected Ramassage ramassage;
	protected Etat etatInitial;
	protected But but;

	/*******************************************************
	 * Cette fonction parse le fichier de map determinant 
	 * - la position initiale du Van (V)
	 * - la position du point d'arrivee (A)
	 * - la position des Colis (C)
	 *******************************************************/

	public void parse(final String nomFichier) throws IOException {
		
		//Ouverture du fichier
		FileInputStream in = null;
		try {
			File inputFile = new File(nomFichier);
			in = new FileInputStream(inputFile);
		} catch (Exception e) {
			return;
		}
		BufferedReader bin = new BufferedReader(new InputStreamReader(in));
		
		/*Lecture du fichier*/
		
		//recupération des deux premieres lignes contenant
		//1) le nombre de lignes de la map
		//2) le nombre de colonnes de la map
		String input;
		input = bin.readLine();
		int nbLignes = Integer.parseInt(input);
		input = bin.readLine();
		int nbColonnes = Integer.parseInt(input);
		
		//initialisation des variables
		String nomCol = "";
		ramassage = new Ramassage();
		etatInitial = new Etat(ramassage);
		Vector<String> positionsColis = new Vector<>();
		Map<String, Emplacement> destinations = new TreeMap<>();
		
		// création des emplacements
		for (int i = 0; i < nbLignes; i++) {
			input = bin.readLine();
			for (int j = 0; j < nbColonnes; j++) {
				
				/*input.charAt(j) est le caractere lu 
				* si ' ' ne rien faire (ca sera une route bloquée)
				* si 'V' c'est la position initiale du Van
				* si 'A' c'est la position de l'arrivee
				* si 'C' c'est la position d'un Colis
				* si '#' c'est une zone à traffic normal
				* si '-' c'est une zone d'embouteillage
				* aucun autre caractere n'est accepté
				*/
				
				if(input.charAt(j)!=' '
						&& input.charAt(j)!='V'
						&& input.charAt(j)!='C'
						&& input.charAt(j)!='A'
						&& input.charAt(j)!='#'
						&& input.charAt(j)!='-') {
					System.err.println("FICHIER MAL FORMÉ");
					System.exit(-1);
				}
					
				if (input.charAt(j) != ' ') {
					//emplacement créé
					String name = i + "-" + j;
					Emplacement location = new Emplacement(name, i, j, "" + input.charAt(j));
					ramassage.emplacements.put(name, location);
					
					//les cas particuliers 
					if (input.charAt(j) == 'V')
						etatInitial.emplacementVan = ramassage.emplacements.get(name);
					if (input.charAt(j) == 'C') {
						positionsColis.add(name);
					}
					if (input.charAt(j) == 'A') {
						Emplacement e = ramassage.emplacements.get(name);
						destinations.put(name, e);
						nomCol = name;
					}
				}
			}
		}
		
        //Pour chaque emplacement on teste l'existence d'emplacements adjacents
		// si un adjacent existe, on rajoute une route qui va de l'emplacement
		// vers l'adjacent.
		for (int i = 0; i < nbLignes; i++) {
			for (int j = 0; j < nbColonnes; j++) {
				//On teste l'existence de l'emplacement position "i-j"
				if (ramassage.emplacements.get(i + "-" + j) != null) {
					Emplacement l1 = ramassage.emplacements.get(i + "-" + j);
					
					//on crée les routes existantes (4 directions a tester)
					if (ramassage.emplacements.get(i + "-" + (j - 1)) != null) {
						Emplacement l2 = ramassage.emplacements.get(i + "-" + (j - 1));
						l1.routes.add(new Route(l1, l2));
					}
					if (ramassage.emplacements.get((i - 1) + "-" + j) != null) {
						Emplacement l2 = ramassage.emplacements.get((i - 1) + "-" + j);
						l1.routes.add(new Route(l1, l2));
					}
					if (ramassage.emplacements.get(i + "-" + (j + 1)) != null) {
						Emplacement l2 = ramassage.emplacements.get(i + "-" + (j + 1));
						l1.routes.add(new Route(l1, l2));
					}
					if (ramassage.emplacements.get((i + 1) + "-" + j) != null) {
						Emplacement l2 = ramassage.emplacements.get((i + 1) + "-" + j);
						l1.routes.add(new Route(l1, l2));
					}
				}
			}
		}
		int nbColis = positionsColis.size();
		
		//creation d'un masque qui indique si les Colis ont été récupérés
		//et on transfert les emplacements des Colis à l'etat initial
		etatInitial.colisRecuperes = new boolean[nbColis];
		etatInitial.emplacementsColis = new Emplacement[nbColis];
		for (int i = 0; i < nbColis; i++) {
			etatInitial.emplacementsColis[i] =
					ramassage.emplacements.get(positionsColis.get(i));
		}
			

		//generation des buts, but identiques tous egaux a l'arrivee
		but = new But();
		but.destinationsPatients = new Emplacement[nbColis];
		for (int i = 0; i < nbColis; i++)
			but.destinationsPatients[i] = destinations.get(nomCol);
		ramassage.destination = destinations.get(nomCol);
	}

}
