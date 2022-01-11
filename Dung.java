package ter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.lang.*;

public class Dung {
	int[][] attaques;

	public Dung(int[][] attaques) {
		this.attaques = attaques;

	}

	/**
	 * permt d'afficher la matrice d'attaques
	 * 
	 * @param attaques
	 */
	public void affiche(int[][] attaques) {
		// Affiche la matrice
		System.out.println();
		for (int[] t : attaques) {

			for (int i = 0; i < t.length; i++) {
				System.out.print(t[i] + " ");
			}
			System.out.println();
		}
	}

	/**
	 * 
	 * @return la taille de la matrice d'attaques
	 */
	public int size() {
		return attaques.length;
	}

	/**
	 * Permet de generer une matrice aleatoire en entrant le pourcentage d'attaques
	 * 
	 * @param taille
	 */
	public void creerMatrice(int taille) {
		Random r = new Random();
		double x = 0.0;
		// int[][] tableau = new int [taille][taille];
		for (int i = 0; i < taille; i++) {
			for (int j = 0; j < taille; j++) {
				// O.80 est le pourcentage d'attaques
				if ((x / (taille * taille))<0.80) {
					this.attaques[i][j] = r.nextInt(2);
					// System.out.println(x/(taille*taille));
					if (this.attaques[i][j] == 1) {
						x += 1;
					}
				} else {
					this.attaques[i][j] = 0;
				}

			}
		}

	}

	/**
	 * Verfie que l'element i est dans le tableau tab return true si il est dedans
	 * sinon retourne faux
	 * 
	 * @param tab
	 * @param i
	 * @param taille
	 * @return
	 */
	public boolean estDansTableau(int[] tab, int i, int taille) {
		for (int j = 0; j < taille; j++) {
			if (tab[j] == i)
				return true;
		}
		return false;
	}

	// Fonction qui va trier le tableau tab en fonction du h_categoriser
	public int[] trieAvecCategoriser(HashMap<Integer, Double> categoriser, int[] tab, int taille) {
		int[] trie = new int[taille];
		int index = 0;
		for (Integer i : categoriser.keySet()) {
			if (estDansTableau(tab, i, taille)) {
				trie[index] = i;
				index++;
			}
		}

		return trie;
	}

	/**
	 * retourne la liste des extensions completes
	 * 
	 * @return
	 */
	public ArrayList<ArrayList<Integer>> extension_complete() {
		ArrayList<ArrayList<Integer>> complete = new ArrayList<ArrayList<Integer>>();
		// toutes les combinaisons possibles
		List<List<Integer>> combinaisons = Utile.combinaison(attaques.length);
		for (List<Integer> combi : combinaisons) {
			// vérifie si une des combinaisons et admissible(donc conflit free aussi)
			if ((Utile.admissible(combi, attaques) == true)) {
				if (Utile.complete(combi, attaques) == true) {
					// si c'est le cas on vérifie si elle est complète on rajote dans la liste si
					// oui
					ArrayList<Integer> arrayListComplete = Utile.listToArrayList(combi);
					complete.add(arrayListComplete);
				}

			}
		}
		return complete;
	}

	public HashMap<Integer, Double> h_CategorizerCyclique() {
		Double[] categorizerDebut = new Double[attaques.length];
		// on met tous les scores à 1
		for (int i = 0; i < attaques.length; i++) {
			categorizerDebut[i] = 1.0;
		}
		// point fixe est le nombre d'argument ayant atteint le point fixe
		
		int pointFixe = 0;
		Double[] categorizerStep = new Double[attaques.length];
		// tant que le point fixe n'est pas egale à la taille des arguments
		while (pointFixe != attaques.length) {
			pointFixe = 0;
			
			for (int i = 0; i < attaques.length; i++) {
				// on récupère la liste de sattaquants de i

				ArrayList<Integer> attaquantsDeI = Utile.attaquants(i, attaques);
				if (attaquantsDeI.size() == 0) {
					// si la liste est vide alors le score = 0
					categorizerStep[i] = 1.0;

				} else {
					// si non on calcule le score grace au score de l'etape précédente
					// avec la formule score = 1/(1+sum)
					// sum = cest la somme de score des attaquants de i
					double sum = 0.0;
					for (Integer j : attaquantsDeI) {

						sum += categorizerDebut[j];
					}
					// permet de prendre 2 chiffre après la virgule
					double d = (double) Math.round(sum * 10000000) / 10000000; // 4.248 --> 4.25
					categorizerStep[i] = 1.0 / (1.0 + d);
					categorizerStep[i] = (double) Math.round(categorizerStep[i] * 10000000) / 10000000;
					
				
				}

			}
			// on verifie le point fixe
			for (int x = 0; x < attaques.length; x++) {
				if (Math.abs(categorizerStep[x] -categorizerDebut[x] ) < 0.00001) {
					pointFixe = pointFixe + 1;
					
				}

			}
			//System.out.println(pointFixe);
			// recopie steo dans debut
			for (int x = 0; x < attaques.length; x++) {
				categorizerDebut[x] = categorizerStep[x];

			}
		}

		HashMap<Integer, Double> categ = new HashMap<Integer, Double>();
		for (int x = 0; x < attaques.length; x++) {
			categ.put(x, categorizerDebut[x]);
		}
		return categ;
	}

	/**
	 * retourne une hashMap classe par statut d'acceptabilité
	 * 
	 * @param extension
	 * @return si l'argument est sceptiquement accepté alors il a une valeur de 2 si
	 *         l'argument est credulement accepte alors il a une valeur de 1 sinon
	 *         il a une valeur de 0
	 */
	public HashMap<Integer, Double> acceptabilite(ArrayList<ArrayList<Integer>> extension) {
		HashMap<Integer, Double> classementAccept = new HashMap<Integer, Double>();
		ArrayList<Integer> argsCredul = Utile.credulement(extension);
		ArrayList<Integer> argsScep = Utile.sceptiquement(extension, attaques);
		for (int x = 0; x < attaques.length; x++) {
			if (Utile.verifNombre(argsScep, x) == true) {
				classementAccept.put(x, 2.0);
			} else if (Utile.verifNombre(argsCredul, x) == true) {
				classementAccept.put(x, 1.0);
			} else {
				classementAccept.put(x, 0.0);
			}
		}
		HashMap<Integer, Double> classementAccepter = Utile.triAvecValeur(classementAccept);
		return classementAccepter;

	}

	/**
	 * recupere le labelling de chaque argument de la matrice et retourne une
	 * hashmap ou la cle est l'argument et la valeur est le tableau labelling
	 * 
	 * @param extension
	 * @return
	 */
	public HashMap<Integer, int[]> labelling(ArrayList<ArrayList<Integer>> extension) {
		HashMap<Integer, int[]> labelling = new HashMap<Integer, int[]>();
		for (int x = 0; x < attaques.length; x++) {
			int[] labelingArg = Utile.labellingPourUnArg(x, extension, attaques);
			labelling.put(x, labelingArg);
		}
		return labelling;
	}

	/**
	 * classe le labelling en fonction du classement de leur statut de justification
	 * en ordre décroissant mais avec les liste vide
	 * 
	 * @param labelling
	 * @return mais avec le liste qui peuvent etre vide (une position sans liste)
	 */
	public HashMap<Integer, ArrayList<Integer>> classementLabelingBrut(HashMap<Integer, int[]> labelling) {
		HashMap<Integer, ArrayList<Integer>> classement = new HashMap<Integer, ArrayList<Integer>>();
		ArrayList<Integer> args1 = new ArrayList<Integer>();
		for (int i = 0; i < attaques.length; i++) {
			if (Utile.verifclassement(labelling.get(i)) == 1) {
				args1.add(i);
			}

		}
		classement.put(1, args1);

		ArrayList<Integer> args2 = new ArrayList<Integer>();
		for (int i = 0; i < attaques.length; i++) {
			if (Utile.verifclassement(labelling.get(i)) == 2) {
				args2.add(i);
			}

		}
		classement.put(2, args2);

		ArrayList<Integer> args3 = new ArrayList<Integer>();
		for (int i = 0; i < attaques.length; i++) {
			if (Utile.verifclassement(labelling.get(i)) == 3) {
				args3.add(i);
			}

		}
		classement.put(3, args3);

		ArrayList<Integer> args4 = new ArrayList<Integer>();
		for (int i = 0; i < attaques.length; i++) {
			if (Utile.verifclassement(labelling.get(i)) == 4) {
				args4.add(i);
			}

		}
		classement.put(4, args4);

		ArrayList<Integer> args5 = new ArrayList<Integer>();
		for (int i = 0; i < attaques.length; i++) {
			if (Utile.verifclassement(labelling.get(i)) == 5) {
				args5.add(i);
			}

		}

		classement.put(5, args5);

		return classement;

	}

	/**
	 * classemnt en enlevant les positions qui ont la liste vide
	 * 
	 * @param classement
	 * @return
	 */
	public HashMap<Integer, ArrayList<Integer>> classementLabeling(HashMap<Integer, ArrayList<Integer>> classement) {
		HashMap<Integer, ArrayList<Integer>> classe = new HashMap<Integer, ArrayList<Integer>>();
		int x = 1;
		for (int i = 1; i < 6; i++) {

			if (classement.get(i).size() != 0) {

				classe.put(x, Utile.addOneList(classement.get(i)));
				x++;

			}

		}
		return classe;
	}

	/**
	 * Retourne le classement de la semantique jrs
	 * 
	 * @param categorizer
	 * @param classementLabeling
	 * @return
	 */
	public HashMap<Integer, Integer> semantiqueJrs(HashMap<Integer, Double> categorizer,
			HashMap<Integer, ArrayList<Integer>> classementLabeling) {
		HashMap<Integer, Double> classementCategorizer = Utile.triAvecValeur(categorizer);
		HashMap<Integer, Integer> semantiqueJRS = new HashMap<Integer, Integer>();

		int s = 0;

		for (int i = 1; i < classementLabeling.size() + 1; i++) {
			// System.out.println(classementLabeling.get(i));
			// classListeCat : classe une liste de classement lbelling en ordre decroissant
			// par rapport au categorizer
			ArrayList<Integer> l = Utile.classListeCat(classementLabeling.get(i), categorizer);

			semantiqueJRS.put(l.get(0), s);
			// System.out.println(l);
			for (int j = 1; j < l.size(); j++) {
				if (categorizer.get(l.get(j)) > categorizer.get(l.get(j - 1))) {
					s += 1;
					semantiqueJRS.put(l.get(j), s);
				} else if (categorizer.get(l.get(j)) < categorizer.get(l.get(j - 1))) {
					s += 1;
					semantiqueJRS.put(l.get(j), s);
				} else {
					semantiqueJRS.put(l.get(j), s);
				}

			}
			s += 1;

		}
		return semantiqueJRS;
	}

	// Fonction qui va trier le tableau tab en fonction du h_categoriser
	public ArrayList<Integer> trieAvecCategoriser(HashMap<Integer, Double> categoriser, ArrayList<Integer> liste) {
		ArrayList<Integer> trie = new ArrayList<Integer>();
		for (Integer i : categoriser.keySet()) {
			if (Utile.verifNombre(liste, i)) {
				trie.add(i);
			}
		}

		return trie;
	}

	/**
	 * Fonction qui rertourne la semantique ARS prend en argument le classement de
	 * h_categoriser et le statut d'acceptabilite
	 * 
	 * @param categoriser
	 * @param accep
	 * @return
	 */
	public HashMap<Integer, Integer> semantiqueARS(HashMap<Integer, Double> categoriser,
			HashMap<Integer, Double> accep) {

		// la liste sep va contenir les elements septiquement acceptes
		// la liste cred va contenir les elements credulement acceptes
		// la liste autre va contenir le reste
		ArrayList<Integer> sep = new ArrayList<Integer>();
		ArrayList<Integer> cred = new ArrayList<Integer>();
		ArrayList<Integer> autre = new ArrayList<Integer>();

		for (int i = 0; i < attaques.length; i++) {
			if (accep.get(i) == 2.0) {
				sep.add(i);

			} else if (accep.get(i) == 1.0) {
				cred.add(i);
			} else {
				autre.add(i);
			}

		}
		// la liste tab_sep va contenir les elements septiquement acceptes trie selon le
		// h_categoriser
		// la liste tab_cred va contenir les elements credulement acceptes trie selon le
		// h_categoriser
		// la liste tab_autre va contenir le reste trie selon le h_categoriser
		ArrayList<Integer> tab_sep = new ArrayList<Integer>();
		ArrayList<Integer> tab_cred = new ArrayList<Integer>();
		ArrayList<Integer> tab_autre = new ArrayList<Integer>();
		tab_sep = trieAvecCategoriser(categoriser, sep);
		tab_cred = trieAvecCategoriser(categoriser, cred);
		tab_autre = trieAvecCategoriser(categoriser, autre);

		// On copier les listes dans le HashMap resultat dans l'ordre decroissant
		// Dans cette hashMap la cle est l'argument et la valeur est la postion
		int index = 0;
		HashMap<Integer, Integer> resultat = new LinkedHashMap<Integer, Integer>();
		if (tab_sep.size() != 0) {
			double x = 0.0;
			for (Integer i : tab_sep) {
				if (x != categoriser.get(i)) {
					resultat.put(i, index);
					index++;
				} else {
					resultat.put(i, index - 1);
				}
				x = categoriser.get(i);
			}

		}

		if (tab_cred.size() != 0) {
			double x = 0.0;
			for (Integer i : tab_cred) {
				if (x != categoriser.get(i)) {
					resultat.put(i, index);
					index++;
				}

				else {

					resultat.put(i, index - 1);
				}
				x = categoriser.get(i);
			}

		}

		if (tab_autre.size() != 0) {
			double x = 0.0;
			for (Integer i : tab_autre) {
				if (x != categoriser.get(i)) {
					resultat.put(i, index);
					index++;
				} else {
					resultat.put(i, index - 1);
				}
				x = categoriser.get(i);
			}

		}

		return resultat;
	}

	/**
	 * retourne le rang des arguments en fonction de categorizer
	 *
	 * 
	 * @param categorizer
	 * @return
	 */

	public HashMap<Integer, Integer> rangDesArguments(HashMap<Integer, Double> categorizer) {
		HashMap<Integer, Double> categorizerClasse = Utile.triAvecValeur(categorizer);
		HashMap<Integer, Integer> classementFinal = new HashMap<Integer, Integer>();
		int x = 0;
		double y = 0.0;
		for (Map.Entry m : categorizerClasse.entrySet()) {
			// m.getKey() retourne la cle et m.getValue() retourne la valeur
			if (y == 0.0) {
				classementFinal.put((Integer) m.getKey(), x);
				y = (double) m.getValue();
			}

			else if (y > (double) m.getValue()) {
				x++;
				classementFinal.put((Integer) m.getKey(), x);
				y = (double) m.getValue();
			} else {
				classementFinal.put((Integer) m.getKey(), x);
				y = (double) m.getValue();
			}
			// System.out.println("ID: "+m.getKey()+", Nom: "+m.getValue());
		}
		return classementFinal;
	}

	/**
	 * retourne une HashMap qui a pour cle un ensemble de l'extension passée en
	 * paramètre (complete ou preferee) et en valeur la moyenne des rangs
	 * 
	 * @param extension
	 * @param rang
	 * @return
	 */
	public static HashMap<ArrayList<Integer>, Double> avgPourUneExtension(ArrayList<ArrayList<Integer>> extension,
			HashMap<Integer, Integer> rang) {
		HashMap<ArrayList<Integer>, Double> avgEnsemble = new HashMap<ArrayList<Integer>, Double>();
		for (ArrayList<Integer> ext : extension) {
			double avg = Utile.avgPourUnEns(ext, rang);
			avgEnsemble.put(Utile.addOneList(ext), avg);
		}
		return avgEnsemble;
	}

	/**
	 * retourne les ensembles de la semantique RBE
	 * 
	 * @param avgPourext
	 * @param extension
	 * @param rang
	 * @return
	 */

	public ArrayList<ArrayList<Integer>> semantiqueRBE(HashMap<ArrayList<Integer>, Double> avgPourext,
			ArrayList<ArrayList<Integer>> extension, HashMap<Integer, Integer> rang) {
		// retourne la moyenne pour pour chaque ensemble de l'extension passee en
		// parmaètre
		avgPourext = avgPourUneExtension(extension, rang);
		ArrayList<ArrayList<Integer>> rbe = new ArrayList<ArrayList<Integer>>();
		double x = 100;
		// trouver la plus petite valeur de avg
		for (Map.Entry m : avgPourext.entrySet()) {
			ArrayList<Integer> s =(ArrayList<Integer>) m.getKey();
			if(s.size()!=0) {
				if ((double) m.getValue() < x) {
					x = (double) m.getValue();
				}

			}
						}
		// on ajoute uniquement celle(s) qui a (ont) la plus petite valeur
		for (Map.Entry m : avgPourext.entrySet()) {
			
			if ((double) m.getValue() == x) {
				rbe.add((ArrayList<Integer>) m.getKey());
			}
		}
		return rbe;
	}

	/**
	 * retourne la nouvelle matrice apres la suppression d'attaques
	 * 
	 * @param rang
	 * @return
	 */
	public int[][] suppresssionAttaques(HashMap<Integer, Integer> rang) {
		int[][] newAttaques = new int[attaques.length][attaques.length];
		for (int i = 0; i < attaques.length; i++) {
			for (int j = 0; j < attaques.length; j++) {
				newAttaques[i][j] = attaques[i][j];
			}
		}
		for (int i = 0; i < attaques.length; i++) {
			for (int j = 0; j < attaques.length; j++) {
				if (attaques[i][j] == 1) {
					// si le rang est superieur cela veut dire que la valeur du categorizer
					// est inferieur donc l'attaque est supprimee
					if (rang.get(i) > rang.get(j)) {
						newAttaques[i][j] = 0;
					}
				}
			}
		}
		return newAttaques;
	}

	/**
	 * renvoie la liste des ensemble de l'extension preferee
	 * 
	 * @param complete , prend en entree l'extension complete
	 * @return
	 */
	public ArrayList<ArrayList<Integer>> preferee(ArrayList<ArrayList<Integer>> complete) {
		
		ArrayList<ArrayList<Integer>> prefere = new ArrayList<ArrayList<Integer>>();

		for (int j = 0; j < complete.size(); j++) {
			//System.out.println(j);
			int x = 0;

			for (int m = j+1; m < complete.size(); m++) {

				boolean a = Utile.verifPrefere(complete.get(j), complete.get(m));
				//System.out.println("j" + pref.get(j) + " m " + pref.get(m) + " a " + a);
				if (a) {
					x += 1;
				}
				

			}
			if (x == 0 && (complete.get(j).size() !=0)) {
				
				prefere.add(complete.get(j));
			}
		}

		return prefere;
	}

}
