package ter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;


public class Utile {
	/**
	 * 
	 * @param arg        un argument
	 * @param attaquesla matirce d'attaques
	 * @return liste contenant tous les attaquant de arg
	 */
	// renvoie une liste qui contient la liste des arguments qui attaquent m
	public static ArrayList<Integer> attaquants(int arg, int[][] attaques) {
		ArrayList<Integer> l = new ArrayList<Integer>();
		for (int i = 0; i < attaques.length; i++) {
			if (attaques[i][arg] == 1) {
				l.add(i);
			}
		}

		return l;
	}
	// renvoie la liste des arguments attaqués par arg
	public static ArrayList<Integer> attaques(int arg, int[][] attaques) {
		ArrayList<Integer> l = new ArrayList<Integer>();
		for (int i = 0; i < attaques.length; i++) {
			if (attaques[arg][i] == 1) {
				l.add(i);
			}
		}

		return l;
	}

	/**
	 * trie une map HashMap<Integer, Double>(celle de categorizer) dans l'ordre
	 * decroissante par rapport aux valeur
	 * 
	 * @param map la map
	 * @return map trier en ordre decroissante pa rapport aux valeurs
	 */
	public static HashMap<Integer, Double> triAvecValeur(HashMap<Integer, Double> map) {
		List<Map.Entry<Integer, Double>> list = new LinkedList<Map.Entry<Integer, Double>>(map.entrySet());
		// implementation de comparator
		Collections.sort(list, new Comparator<Map.Entry<Integer, Double>>() {
			public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		HashMap<Integer, Double> map_apres = new LinkedHashMap<Integer, Double>();
		for (Map.Entry<Integer, Double> entry : list)
			map_apres.put(entry.getKey(), entry.getValue());
		return map_apres;
	}

	/**
	 * prend en entree le labelling (argument labelled) et retourne la liste des
	 * argumetns qui on pour label "in" Cette fonction va servir pour la semantique
	 * JRS
	 * 
	 * @param labelling     liste des arguments in
	 * @param tailleMatrice
	 * @return
	 */
	public static ArrayList<Integer> argumentsIn(HashMap<Integer, String> labelling, int tailleMatrice) {
		ArrayList<Integer> argumentsIn = new ArrayList<Integer>();
		for (int i = 0; i < tailleMatrice; i++) {
			if (labelling.get(i) == "in") {
				argumentsIn.add(i);
			}
		}
		return argumentsIn;
	}

	/**
	 * prend en entree le labelling (argument labelled) et retourne la liste des
	 * argumetns qui on pour label "out" Cette fonction va servir pour la semantique
	 * JRS
	 * 
	 * @param labelling liste des arguments out
	 * 
	 * @return
	 */
	public static ArrayList<Integer> argumentsOut(HashMap<Integer, String> labelling, int tailleMatrice) {
		ArrayList<Integer> argumentsOut = new ArrayList<Integer>();
		// ajoute l'argument(cle= a la liste si son label(valeur) est out
		for (int i = 0; i < tailleMatrice; i++) {
			if (labelling.get(i) == "out") {
				argumentsOut.add(i);
			}
		}
		return argumentsOut;
	}

	/**
	 * prend en entree une list d'arguments qui peut etre argumentIn ou argumentOut
	 * et retourne l'argument qui a la valeur max de categorizer
	 * 
	 * Cette fonction va aider pour la semantique JRS
	 * 
	 * @param arguments:  liste qui contient un certains nombres d'argments qui sont
	 *                    des cles de categorizer
	 * @param categorizer : resultat de la methode categorizer
	 * @return l'argument dans arguments qui a un categorizer superieur aux autres
	 */
	public static int maxValeurCat(ArrayList<Integer> arguments, HashMap<Integer, Double> categorizer) {
		// m est le premier element de la liste arguments
		int m = arguments.get(0);
		double argmax = categorizer.get(m);
		int r = m;
		// par defaut l'argmax est la valeur de m dans categorizer(la valeur de
		// la fonction categorizer pour l'argument m
		for (Integer j : arguments) {

			if (categorizer.get(j) > argmax) {
				argmax = categorizer.get(j);
				r = j;
			}
		}
		/*
		 * double argmax = categorizer.get(m); for (int i = 1; i < arguments.size();
		 * i++) {
		 * 
		 * if (categorizer.get(arguments.get(i)) > argmax) { m = arguments.get(i); } }
		 */
		return r;
	}

	public static ArrayList<Integer> classListeCat(ArrayList<Integer> args, HashMap<Integer, Double> categorizer) {
		ArrayList<Integer> liste = new ArrayList<Integer>();
		while (args.size() != 0) {
			int max = Utile.maxValeurCat(args, categorizer);
			liste.add(max);
			Iterator success = args.iterator();
			while (success.hasNext()) {
				int elem = (int) success.next();
				if (elem == max) {
					success.remove();
				}
			}
		}
		return liste;
	}

	/**
	 * etabli la combinaison 
	 * 
	 * @param nombres
	 * @param nb
	 * @return
	 */
	public static List<List<Integer>> etablirCombinaison(ArrayList<Integer> nombres, int nb) {
		List<List<Integer>> combinaisons = new LinkedList<List<Integer>>();
		if (nb > 0) {
			ListIterator<Integer> listIterator = nombres.listIterator();

			while (listIterator.hasNext()) {
				Integer integer = (Integer) listIterator.next();
				listIterator.remove();

				List<List<Integer>> etablirCombinaison = etablirCombinaison(new ArrayList<Integer>(nombres), nb - 1);

				for (List<Integer> combinaison : etablirCombinaison) {
					combinaison.add(0, integer);
					combinaisons.add(combinaison);
				}
			}
		} else {
			combinaisons.add(new LinkedList<Integer>());
		}

		return combinaisons;
	}

	/**
	 * renvoie la liste de toutes les combinaisons possibles
	 * 
	 * @param nb taille de la matrice
	 * @return
	 */
	public static List<List<Integer>> combinaison(int nb) {
		ArrayList<Integer> nombres = new ArrayList<Integer>();

		for (int j = 0; j < nb; j++) {
			nombres.add(j);
		}
		List<List<Integer>> combinaisons = etablirCombinaison(nombres, 1);

		for (int i = 1; i < nb; i++) {
			ArrayList<Integer> nombres2 = new ArrayList<Integer>();

			for (int j = 0; j < nb; j++) {
				nombres2.add(j);
			}
			List<List<Integer>> etablirCombinaison = etablirCombinaison(nombres2, i + 1);
			for (List<Integer> combi : etablirCombinaison) {
				combinaisons.add(combi);
			}

		}
		ArrayList<Integer> vide = new ArrayList<Integer>();
		combinaisons.add(vide);
		return combinaisons;
	}

	/**
	 * Verifie si le nombre est dans une liste
	 * 
	 * @param list
	 * @param i
	 * @return vraie si i est dans la liste
	 */
	public static boolean verifNombre(List<Integer> list, int i) {

		for (int j = 0; j < list.size(); j++) {
			if (list.get(j) == i)
				return true;
		}
		return false;

	}

	/**
	 * 
	 * @param combinaison : un ensemble d'une des combinaisons possibles des
	 *                    arguments (un des ensembles de la fonction
	 *                    etablirCombinaison)
	 * @param attaques    : la matrice d'attaques
	 * @return Vraie si l'ensemble est conflict_free
	 */
	public static boolean conflict_free(List<Integer> combinaison, int[][] attaques) {

		for (int i = 0; i < attaques.length; i++) {
			if (verifNombre(combinaison, i)) {
				for (int j = 0; j < attaques.length; j++) {
					if (verifNombre(combinaison, j)) {
						if (attaques[i][j] == 1) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	/**
	 * 
	 * @param combinaison : un ensemble d'une des combinaisons possibles des
	 *                    arguments (un des ensembles de la fonction
	 *                    etablirCombinaison)
	 * @param attaques    : la matrice d'attaques
	 * @return Vraie si l'ensemble est admissible
	 */
	public static boolean admissible(List<Integer> combinaison, int[][] attaques) {
		
		if (Utile.conflict_free(combinaison, attaques)) {
			// on verifie pour chaque element de la matrice si il est dans la combinaison
			for (int i = 0; i < attaques.length; i++) {

				if (verifNombre(combinaison, i)) {
					// on recupere les attaquantd de i
					ArrayList<Integer> attaquantsDeI = Utile.attaquants(i, attaques);
					// on regarde si un element de combinaison attaque les attaquants de I (si un
					// element de combinaison defends I)
					if (attaquantsDeI.size() != 0) {
						for (Integer z : attaquantsDeI) {
							if ((attaqueAttaquant(combinaison, z, attaques)) == false) {
								return false;
							}
						}
					}

				}
			}
		} else
			return false;
		return true;
	}

	/**
	 * prend en entree une combinaison(ensemble s'arguments ) verifie si un des
	 * arguments de combinaison attaque l'argument j (qui est un attaquant)
	 * 
	 * @param combinaison
	 * @param i
	 * @param attaques
	 * @return
	 */
	public static boolean attaqueAttaquant(List<Integer> combinaison, int i, int[][] attaques) {
		boolean b = false;
		for (int j : combinaison) {
			if (attaques[j][i] == 1) {
				return true;
			}
		}

		return b;
	}

	/**
	 * 
	 * @param combinaison : un ensemble d'une des combinaisons possibles des
	 *                    arguments (un des ensembles de la fonction
	 *                    etablirCombinaison)
	 * @param attaques    : la matrice d'attaques
	 * @return Vraie si l'ensemble est complete
	 */
	public static boolean complete(List<Integer> combinaison, int[][] attaques) {
		// probleme de l'ensemble vide
		if (combinaison.size() == 0) {
			// si un arguments dans attaques n'a pas d'attaquants alors la
			// liste vide n'est pas complète
			for (int i = 0; i < attaques.length; i++) {
				if (attaquants(i, attaques).size() == 0) {
					return false;
				}
			}
		}
		// pour un element i dans la combinaison
		for (Integer i : combinaison) {
			// on recupere la liste des arguments defendus par i
			ArrayList<Integer> defendusParI = argDefendus(i, attaques);
			if (defendusParI.size() != 0) {

				for (Integer j : defendusParI) {
					// si un argument j defendus par i n'est pas dans combinaison
					// on retourne false
					if (verifNombre(combinaison, j) == false) {
						return false;
					} else
						continue;
				}

			}
		}

		return true;
	}

	/**
	 * convertie une list en ArrayList
	 * 
	 * @param <T>
	 * @param list
	 * @return
	 */
	public static <T> ArrayList<T> listToArrayList(List<T> list) {
		ArrayList<T> arrayList = new ArrayList<T>();
		for (T i : list) {
			arrayList.add(i);
		}
		return arrayList;
	}

	/**
	 * renvoie tous les ensembles conflict free d'un systeme d'argumentation
	 * 
	 * @param toutesLescombinaisons
	 * @param attaques
	 * @return
	 */
	public static ArrayList<ArrayList<Integer>> tousConflictFree(List<List<Integer>> toutesLescombinaisons,
			int[][] attaques) {
		ArrayList<ArrayList<Integer>> liste = new ArrayList<ArrayList<Integer>>();
		for (List<Integer> cf : toutesLescombinaisons) {
			if (conflict_free(cf, attaques) == true) {
				ArrayList<Integer> arrayListCf = listToArrayList(cf);
				liste.add(arrayListCf);
			}
		}
		return liste;
	}

	/**
	 * renvoie tous les arguments admissibles
	 * 
	 * @param toutesLescombinaisons
	 * @param attaques
	 * @return la liste des arguments admissibles
	 */
	public static ArrayList<ArrayList<Integer>> tousAdmissible(List<List<Integer>> toutesLescombinaisons,
			int[][] attaques) {
		ArrayList<ArrayList<Integer>> liste = new ArrayList<ArrayList<Integer>>();
		for (List<Integer> cf : toutesLescombinaisons) {
			if (admissible(cf, attaques) == true) {
				ArrayList<Integer> arrayListCf = listToArrayList(cf);
				liste.add(arrayListCf);
			}
		}
		return liste;
	}

	/**
	 * 
	 * @param arg
	 * @param j
	 * @param attaques : matrice d'attaques
	 * @return vrai si l'argument arg defends l'argument j
	 */
	public static boolean defend(int arg, int j, int[][] attaques) {
		ArrayList<Integer> attaquantJ = attaquants(j, attaques);
		ArrayList<Integer> attaquesArg = attaques(arg, attaques);
		for (Integer i : attaquantJ) {
			if (verifNombre(attaquesArg, i) == false) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * @param arg
	 * @param attaques
	 * @return la liste des arguments defendus par arg
	 */
	public static ArrayList<Integer> argDefendus(int arg, int[][] attaques) {
		ArrayList<Integer> defendus = new ArrayList<Integer>();
		for (int i = 0; i < attaques.length; i++) {
			if (defend(arg, i, attaques)) {
				defendus.add(i);
			}
		}
		return defendus;
	}

	/**
	 * prends en entree un argument et une extension et vérifie si l'argument est
	 * scéptiquement accépté
	 * 
	 * @param arg
	 * @param extension
	 * @return
	 */
	public static boolean verifSceptique(int arg, ArrayList<ArrayList<Integer>> extension) {
		// on recupère chaque ensemble de l'extension
		for (ArrayList<Integer> l : extension) {
			// on vérifie que l'argument arg appartient à l'ensemble
			if (verifNombre(l, arg) == false) {
				// si l'argument n'appartient pas à un des ensembles on retourne false
				return false;
			}
		}
		return true;
	}

	/**
	 * renvoie la liste de tous les arguments sceptiquement acceptes
	 * 
	 * @param extension
	 * @param attaques
	 * @return
	 */
	public static ArrayList<Integer> sceptiquement(ArrayList<ArrayList<Integer>> extension, int[][] attaques) {
		ArrayList<Integer> argSceptiquementAccepte = new ArrayList<Integer>();
		// pour chaque argument on verifie si il est sceptiquement accepte
		for (int i = 0; i < attaques.length; i++) {
			if (Utile.verifSceptique(i, extension) == true) {
				// si oui on rajoute dans la liste
				argSceptiquementAccepte.add(i);
			}
		}
		return argSceptiquementAccepte;
	}

	/**
	 * renvoie la liste des argument credulement accepte pour une extension passée
	 * en parametre
	 * 
	 * @param extension
	 * @return
	 */
	public static ArrayList<Integer> credulement(ArrayList<ArrayList<Integer>> extension) {
		ArrayList<Integer> argCredulementAccepte = new ArrayList<Integer>();
		// pour chaque ensemble de l'extension
		for (ArrayList<Integer> l : extension) {
			// on recupère ses arguments
			for (Integer i : l) {
				// on verifie si ils ne sont oas dejà dans la liste
				// si ils ne sont pas on rajopute dans la liste (eviter les doublons)
				if (Utile.verifNombre(argCredulementAccepte, i) == false) {
					argCredulementAccepte.add(i);
				}
			}

		}
		return argCredulementAccepte;
	}
	
	

	/**
	 * prend en entree un ensemble d'une extension et et la matrice d'attaques et
	 * retourne le reinstatement labelling
	 * 
	 * retourn pour tous les elements d'une extension elle retourne le labelling si
	 * on suppose l'ensemble "ensemble in" exemple : 0 0 0 1 0 0 1 0 0 1 0 0 0 1 1 0
	 * les completes sont : [[0], [0, 1], [0, 2]] si l'ensemble est {0,1} la fontion
	 * retourne 0:2 1:2 2:0 3: 0 ça veut dire 0 et 1 sont in 2 et 3 sont out
	 * 
	 * @param ensemble
	 * @param attaques
	 * @return
	 */
	public static HashMap<Integer, Integer> reinstatement(ArrayList<Integer> ensemble, int[][] attaques) {
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		for (Integer j : ensemble) {
			ArrayList<Integer> attaquesParEltEns = attaques(j, attaques);
			for (Integer x : attaquesParEltEns) {
				if (!(map.containsKey(x))) {
					map.put(x, 0);
				}

			}
		}
		for (int i = 0; i < attaques.length; i++) {
			if (verifNombre(ensemble, i) == true) {
				map.put(i, 2);
			} else {
				if (!(map.containsKey(i))) {
					map.put(i, 1);
				}
			}

		}
		return map;
	}

	/**
	 * prend argument un argument, une extension et la matrice d'attaques et renvoie
	 * un tableau qui determine le labelling de l'argument pour l'extension passee
	 * en parametre il y a un tableau de 3 cases si. Chaque represente un labelling
	 * 
	 * ex : tab[0]=1 tab[1]=0 tab[2]=0 ça veut dire que l'argument est toujours in
	 * tab[0]=1 tab[1]=0 tab[2]=1 ça veut dire que l'argument est {in, undec}
	 * 
	 * @param arg
	 * @param extension
	 * @param attaques
	 * @return le tableau de taille 3
	 */
	// in out undec
	public static int[] labellingPourUnArg(int arg, ArrayList<ArrayList<Integer>> extension, int[][] attaques) {
		int[] labelingArg = new int[3];
		for (ArrayList<Integer> l : extension) {
			HashMap<Integer, Integer> map = reinstatement(l, attaques);
			if (map.get(arg) == 2) {// si l'argument est in
				labelingArg[0] = 1;
			}
			if (map.get(arg) == 1) {// si l'argument est undec
				labelingArg[2] = 1;
			}
			if (map.get(arg) == 0) {// si l'argument est out
				labelingArg[1] = 1;
			}
		}
		return labelingArg;
	}

	/**
	 * retourne un entier en foction du classement deu statut de justification
	 * 
	 * @param tab
	 * @return
	 */
	public static int verifclassement(int[] tab) {
		int[] arrays2 = { 1, 0, 0 };
		// renvoie 1 parce que c'est le statut {in}
		if (Arrays.equals(tab, arrays2)) {
			return 1;
		}

		int[] arrays2_3 = { 1, 0, 1 };
		if (Arrays.equals(tab, arrays2_3)) {
			return 2;
		}
		int[] arrays2_4 = { 0, 0, 1 };

		if (Arrays.equals(tab, arrays2_4)) {
			return 3;
		}
		int[] arrays2_5 = { 1, 1, 1 };
		if (Arrays.equals(tab, arrays2_5)) {
			return 3;
		}
		int[] arrays2_6 = { 1, 1, 0 };
		if (Arrays.equals(tab, arrays2_6)) {
			return 3;
		}
		int[] arrays2_7 = { 0, 1, 1 };
		if (Arrays.equals(tab, arrays2_7)) {
			return 4;
		}

		return 5;

	}

	/**
	 * trie une map HashMap<Integer, Integer>(celle de ARS et JRS) dans l'ordre
	 * croissant par rapport aux valeur
	 * 
	 * @param map la map
	 * @return
	 * @return map trier en ordre decroissante pa rapport aux valeurs
	 */
	public static HashMap<Integer, Integer> triAvecValeurCroissant(HashMap<Integer, Integer> map) {
		List<Map.Entry<Integer, Integer>> list = new LinkedList<Map.Entry<Integer, Integer>>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<Integer, Integer>>() {
			public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});

		HashMap<Integer, Integer> map_apres = new LinkedHashMap<Integer, Integer>();
		for (Map.Entry<Integer, Integer> entry : list)
			map_apres.put(entry.getKey(), entry.getValue());
		return map_apres;
	}

	/**
	 * renvoie la moyenne de rang de l'ensemble passé en paramètre
	 * 
	 * @param ensemble
	 * @param rang
	 * @return
	 */
	public static double avgPourUnEns(ArrayList<Integer> ensemble, HashMap<Integer, Integer> rang) {
		double sum = 0.0;
		if (ensemble.size() == 0) {
			return 0.0;
		} else {
			for (Integer arg : ensemble) {
				sum += rang.get(arg);

			}
		}

		double avg = sum / ensemble.size();
		double d = (double) Math.round(avg * 100) / 100;
		return d;
	}

	/**
	 * verifie si l'ensemble m est compris dans l'ensemble s
	 * 
	 * @param m
	 * @param s
	 * @return
	 */
	public static boolean verifPrefere(ArrayList<Integer> m, ArrayList<Integer> s) {
		boolean b = true;
		// verifie que tous les elements de l'ensemble m sont dans s
		for (Integer i : m) {
			if (!verifNombre(s, i)) {
				return false;
			}
		}
		// verifie que le nombre d'element de l'ensemble s est superieur au nb elements
		// de l'ensemble m
		if (s.size() > m.size()) {
			return true;
		}
		return false;
	}

	// pour l'affichage
	public static ArrayList<Integer> addOneList(ArrayList<Integer> l) {
		ArrayList<Integer> m = new ArrayList<Integer>();
		for (Integer s : l) {
			m.add(s + 1);
		}
		return m;
	}

	// pour l'affichage
	public static ArrayList<ArrayList<Integer>> addOneListList(ArrayList<ArrayList<Integer>> l) {
		ArrayList<ArrayList<Integer>> m = new ArrayList<ArrayList<Integer>>();
		for (ArrayList<Integer> s : l) {
			ArrayList<Integer> r = addOneList(s);
			m.add(r);

		}
		return m;
	}

	// pour l'affichage
	public static HashMap<Integer, Double> addOneMapD(HashMap<Integer, Double> l) {
		HashMap<Integer, Double> m = new HashMap<Integer, Double>();
		for (int i = 0; i < l.size(); i++) {
			double x = l.get(i);
			m.put(i + 1, x);
		}
		return m;
	}

	// pour l'affichage
	public static HashMap<Integer, Integer> addOneMapI(HashMap<Integer, Integer> l) {
		HashMap<Integer, Integer> m = new HashMap<Integer, Integer>();
		for (int i = 0; i < l.size(); i++) {
			int x = l.get(i);
			m.put(i + 1, x);
		}
		return m;
	}
	
	

}
