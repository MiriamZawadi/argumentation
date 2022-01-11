package ter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Main qui lit la matrice d'attaques dans un fichier qui ne contient que la
 * matrice d'attaques et affiche les differents resultats sur la console
 */

public class TestDung {
	// Nous permet d'avoir la taille de la matrice afin de l'initialiser
	public static int tailleMatrice(String nomFichier) {
		int m = 0;
		try {

			// lire le fichier
			BufferedReader file = new BufferedReader(new InputStreamReader(new FileInputStream(new File(nomFichier))));
			String line = file.readLine();
			String[] t = line.split(" ");
			m += t.length;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return m;
	}

	public static String afficheLabeling(int[] tab) {

		if (tab[0] == 1 & tab[1] == 0 & tab[2] == 0) {
			return "{in}";
		}

		if (tab[0] == 1 & tab[1] == 1 & tab[2] == 0) {
			return "{in,out}";
		}
		if (tab[0] == 1 & tab[1] == 0 & tab[2] == 1) {
			return "{in,undec}";
		}
		if (tab[0] == 1 & tab[1] == 1 & tab[2] == 1) {
			return "{in,out,undec}";
		}
		if (tab[0] == 0 & tab[1] == 1 & tab[2] == 1) {
			return "{out,undec}";
		}
		if (tab[0] == 0 & tab[1] == 0 & tab[2] == 1) {
			return "{undec}";
		}

		return "{out}";

	}

	public static void main(String[] args) {
		// nom du fichier
		String nomFichier = "/Users/miramzawadi/eclipse-workspace/Ter/matrice.txt";
		int taille = tailleMatrice(nomFichier);
		int[][] attaques = new int[taille][taille];

		try {

			// lire le fichier
			BufferedReader file = new BufferedReader(new InputStreamReader(new FileInputStream(new File(nomFichier))));
			String line = file.readLine();
			int i = 0;
			// lis chaque ligne du fichier SO1N001 et l'ajoute dans la liste de double
			while (line != null) {
				// transforme la ligne lu en un tableau de String
				String[] t = line.split(" ");

				// rajout des elements du tableau t dans la matrice
				for (int j = 0; j < attaques[i].length; j++) {
					attaques[i][j] = Integer.parseInt(t[j]);

				}

				i += 1;
				// lis la prochaine ligne
				line = file.readLine();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		// INSTANCIER DUNG avec Matrice dans un fichier
		Dung l = new Dung(attaques);
		l.affiche(attaques);

		// System.out.println("Les attaquants de 3 sont "+Utile.attaquants(3,
		// attaques));

		long tempsDebut = System.currentTimeMillis();
		HashMap<Integer, Double> h_categorizer = Utile.triAvecValeur(Utile.addOneMapD(l.h_CategorizerCyclique()));
		System.out.println("Le classement par rapport à h_categorizer" + h_categorizer);
		long tempsFin = System.currentTimeMillis();
		double seconds = (tempsFin - tempsDebut) / 1000F;
		// System.out.println(seconds);

		// System.out.println (Utile.combinaison(l.size()));
		ArrayList<ArrayList<Integer>> conflictFree = Utile.tousConflictFree(Utile.combinaison(l.size()), l.attaques);
		System.out.println("Les arguments conflict free sont : " + Utile.addOneListList(conflictFree));

		ArrayList<ArrayList<Integer>> admissibles = Utile.tousAdmissible(Utile.combinaison(l.size()), l.attaques);
		System.out.println("Les arguments admissibles sont : " + Utile.addOneListList(admissibles));

		tempsDebut = System.currentTimeMillis();
		ArrayList<ArrayList<Integer>> completes = l.extension_complete();
		seconds = (System.currentTimeMillis() - tempsDebut) / 1000F;
		System.out.println("Les complètes sont : " + Utile.addOneListList(completes));
		// System.out.println(seconds);

		ArrayList<ArrayList<Integer>> prefere = l.preferee(completes);
		System.out.println("Les préférées sont : " + Utile.addOneListList(prefere));

		// System.out.println("Le retour de la fonction
		// h_categorizer"+l.h_CategorizerCyclique().toString());

		ArrayList<Integer> sceptiquement = Utile.sceptiquement(completes, l.attaques);
		System.out.println("La grounded est : " + Utile.addOneList(sceptiquement));
		System.out.println("Les arguments scéptiquement accepté pour la sémantique complète sont : "
				+ Utile.addOneList(sceptiquement));

		ArrayList<Integer> credulement = Utile.credulement(completes);
		System.out.println("Les arguments crédulement accepté pour la sémantique complète sont : "
				+ Utile.addOneList(credulement));

		System.out.println("Le statut de justification pour chaque argument : ");
		HashMap<Integer, int[]> labelling = l.labelling(completes);
		for (int x = 0; x < attaques.length; x++) {
			System.out.println(x + 1 + " est " + afficheLabeling(labelling.get(x)));
		}
		HashMap<Integer, ArrayList<Integer>> classement = l.classementLabelingBrut(l.labelling(completes));
		HashMap<Integer, ArrayList<Integer>> labeling = l.classementLabeling(classement);
		System.out.println("Le classement labelling : " + labeling);

		HashMap<Integer, Double> acceptabilite = l.acceptabilite(l.extension_complete());
		System.out.println(
				"Le classement statut d'accceptabilité : " + Utile.triAvecValeur(Utile.addOneMapD(acceptabilite)));

		System.out.println(
				"Classement JRS est : " + Utile.triAvecValeurCroissant(l.semantiqueJrs(h_categorizer, labeling)));
		// Utile.affaicheArsJrs(l.semantiqueJrs(l.h_CategorizerCyclique(),
		// l.classementLabeling(classement)), attaques);
		System.out.println("Classement ARS est : " + Utile
				.triAvecValeurCroissant(Utile.addOneMapI(l.semantiqueARS(Utile.triAvecValeur(l.h_CategorizerCyclique()),
						l.acceptabilite(l.extension_complete())))));
		tempsDebut = System.currentTimeMillis();
		HashMap<Integer, Integer> rang = l.rangDesArguments(l.h_CategorizerCyclique());
		seconds = (System.currentTimeMillis() - tempsDebut) / 1000F;
		System.out.println("Le rang est   : " + Utile.addOneMapI(rang));
		// System.out.println(seconds);

		// System.out.println(Utile.avgPourUnEns(m,l.rangDesArguments(l.h_CategorizerCyclique())));
		HashMap<ArrayList<Integer>, Double> avgPourExt = Dung.avgPourUneExtension(l.extension_complete(),
				l.rangDesArguments(l.h_CategorizerCyclique()));
		System.out.println("Les moyennes pour l'extension complète : " + avgPourExt);
		ArrayList<ArrayList<Integer>> semantiqueRBE = l.semantiqueRBE(avgPourExt, l.extension_complete(),
				l.rangDesArguments(l.h_CategorizerCyclique()));
		System.out.println("La sémantique RBE est : " + semantiqueRBE);
		int[][] newAttaques = l.suppresssionAttaques(l.rangDesArguments(l.h_CategorizerCyclique()));
		System.out.print("La nouvelle matrice après la Suppression d'attaques ");
		l.affiche(newAttaques);
		// System.out.println("Classement JRS est :
		// "+Arrays.toString(l.semantiqueJrs(l.classementLabeling(classement),
		// Utile.triAvecValeur(l.h_CategorizerCyclique()))));

	}

}
