package ter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Le main qui enregistre les resultats dans un fichier et genere aleatoirement
 * la matrice apres avoir entre la taille de la matrice et enregistre les
 * differents resultat dans le fichier
 */

public class TestDung_Matrice_Aleatoire {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Veuillez saisir la taille de la matrice : ");
		int taille_M = scanner.nextInt();
		int[][] attaques = new int[taille_M][taille_M];
		Dung l = new Dung(attaques);
		l.creerMatrice(taille_M);
		// nom du fichier 
		File file = new File("resultat.txt");
		//l.affiche(attaques);
		// creer le fichier s'il n'existe pas if (!file.exists())

		try {
			if (!file.exists())
				file.createNewFile();

			FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
			BufferedWriter bw = new BufferedWriter(fw);
			// ecrire la matrice

			for (int i = 0; i < taille_M; i++) {
				for (int j = 0; j < taille_M; j++) {
					int x = l.attaques[i][j];
					bw.write(String.valueOf(x));
					bw.write(" ");
				}
				bw.write("\n");
			}

			long tempsDebut = System.currentTimeMillis();
			HashMap<Integer, Double> h_categorizer = Utile.triAvecValeur(Utile.addOneMapD(l.h_CategorizerCyclique()));
			double tempsCategorizer = (System.currentTimeMillis() - tempsDebut) / 1000F;
			//bw.write("H_categorizer => " + h_categorizer + "\n");
			ArrayList<ArrayList<Integer>> conflictFree = Utile.tousConflictFree(Utile.combinaison(l.size()),
					l.attaques);
			ArrayList<ArrayList<Integer>> admissibles = Utile.tousAdmissible(Utile.combinaison(l.size()), l.attaques);
			tempsDebut = System.currentTimeMillis();
			ArrayList<ArrayList<Integer>> completes = l.extension_complete();
			double tempsComplete = (System.currentTimeMillis() - tempsDebut) / 1000F;
			ArrayList<Integer> sceptiquement = Utile.sceptiquement(l.extension_complete(), l.attaques);
			ArrayList<Integer> credulement = Utile.credulement(l.extension_complete());

			tempsDebut = System.currentTimeMillis();
			HashMap<Integer, ArrayList<Integer>> classement = l
					.classementLabelingBrut(l.labelling(l.extension_complete()));
			HashMap<Integer, ArrayList<Integer>> labeling = l.classementLabeling(classement);
			double tempsLabelling = (System.currentTimeMillis() + tempsComplete - tempsDebut) / 1000F;

			tempsDebut = System.currentTimeMillis();
			HashMap<Integer, Double> acceptabilite = l.acceptabilite(l.extension_complete());
			double tempsAcceptabilite = (System.currentTimeMillis() - tempsDebut) / 1000F;

			tempsDebut = System.currentTimeMillis();
			HashMap<Integer, Integer> rang = l.rangDesArguments(l.h_CategorizerCyclique());
			double tempsRang = (System.currentTimeMillis() - tempsDebut) / 1000F;

			HashMap<ArrayList<Integer>, Double> avgPourExt = Dung.avgPourUneExtension(l.extension_complete(),
					l.rangDesArguments(l.h_CategorizerCyclique()));

			tempsDebut = System.currentTimeMillis();
			ArrayList<ArrayList<Integer>> semantiqueRBE = l.semantiqueRBE(avgPourExt, l.extension_complete(),
					l.rangDesArguments(l.h_CategorizerCyclique()));
			double tempsRbe = (System.currentTimeMillis() - tempsDebut) / 1000F;

			tempsDebut = System.currentTimeMillis();
			int[][] newAttaques = l.suppresssionAttaques(l.rangDesArguments(l.h_CategorizerCyclique()));
			double tempsnewAttaques = (System.currentTimeMillis() - tempsDebut) / 1000F;

			bw.write("H_categorizer => " + h_categorizer + "\n");
			bw.write("Conflict free => " + Utile.addOneListList(conflictFree) + "\n");
			bw.write("Extensions Admissibles => " + Utile.addOneListList(admissibles) + "\n");
			bw.write("Extensions Complètes => " + Utile.addOneListList(completes) + "\n");
			tempsDebut = System.currentTimeMillis();
			bw.write("Arguments scéptiquements acceptés =>  " + Utile.addOneList(sceptiquement) + "\n");
			bw.write("Arguments crédulements acceptés =>  " + Utile.addOneList(credulement) + "\n");
			bw.write("Le labelling : \n");
			double tempsAcc = (System.currentTimeMillis() - tempsDebut) / 1000F;
			HashMap<Integer, int[]> labelling = l.labelling(completes);
			for (int x = 0; x < attaques.length; x++) {
				bw.write(x + 1 + " est " + TestDung.afficheLabeling(labelling.get(x)) + "\n");
			}
			bw.write("Le classement du labelling => " + labeling + "\n");
			bw.write(
					"Le classement par rapport au statut d'acceptabilité => " + Utile.addOneMapD(acceptabilite) + "\n");
			tempsDebut = System.currentTimeMillis();
			bw.write("La sémantique JRS => " + l.semantiqueJrs(h_categorizer, labeling) + "\n");
			double tempsJrs = ((System.currentTimeMillis() + tempsCategorizer + tempsLabelling) - tempsDebut) / 1000F;

			tempsDebut = System.currentTimeMillis();
			bw.write("La sémantique ARS => "
					+ Utile.addOneMapI(l.semantiqueARS(Utile.triAvecValeur(l.h_CategorizerCyclique()),
							l.acceptabilite(l.extension_complete())))
					+ "\n");
			double tempsArs = ((System.currentTimeMillis() + tempsComplete) - tempsDebut) / 1000F;

			bw.write("Les rangs des arguments => " + Utile.addOneMapI(rang) + "\n");
			bw.write("Les moyennes par rapport au rang des arguments => " + avgPourExt + "\n");
			bw.write("Sémantique RBE => " + semantiqueRBE + "\n");
			bw.write("La matrice après la suppression d'attaques : \n");

			for (int i = 0; i < taille_M; i++) {
				for (int j = 0; j < taille_M; j++) {
					int x = newAttaques[i][j];
					bw.write(String.valueOf(x));
					bw.write(" ");
				}
				bw.write("\n");
			}

			bw.write("\n");

			String s = "Temps d'exécution :\n - Categorizer : " + tempsCategorizer + "\n - Extension complète : "
					+ tempsComplete + "\n - Labeling : " + tempsLabelling + "\n - Rang : " + tempsRang + "\n - RBE : "
					+ tempsRbe + "\n - Suppression d'attaques : " + tempsnewAttaques + "\n - ARS : " + tempsArs
					+ "\n - JRS : " + tempsJrs + "\nCrédulement et sceptiquement : " + tempsAcceptabilite + "\n";
			bw.write(s);

			bw.write(
					"-----------------------------------------------------------------------------------------------------------------");
			bw.write("\n");
			bw.write("\n");
			bw.close();

		} catch (IOException e) { // TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
