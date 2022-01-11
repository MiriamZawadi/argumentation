package ter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MM {
	
	/*public int[] semantiqueJrs(HashMap<Integer, ArrayList<Integer>> classementLabelling,
			HashMap<Integer, Double> categorizer) {
		int[] jrs = new int[attaques.length];
		int j=0;
		for (int i = 1; i < classementLabelling.size()+1; i++) {
			// argument qui a la valeur max de categorizer
			ArrayList<Integer> classement1 = classementLabelling.get(i);
			
			while (classement1.size() != 0) {
				int max = Utile.maxValeurCat(classement1, categorizer);
				jrs[j] = max;
				Iterator success = classement1.iterator();
				while(success.hasNext()) {
					int elem = (int) success.next();
					if (elem==max) {
							success.remove();
					}
				}
				j++;
			}

		}
		return jrs;
	}*/
	
	/********* DANS DUNG ********
	
	/**
	 * retourne le rang des arguments en fonction  de categorizer
	 *
	 * 
	 * @param categorizer
	 * @return
	 */
	/*
		public HashMap<Integer, Integer> rangDesArguments(HashMap<Integer, Double> categorizer) {
			HashMap<Integer, Double> categorizerClassé = Utile.triAvecValeur(categorizer);
			HashMap<Integer, Integer> classementFinal = new HashMap<Integer, Integer>();
			int x = 0;
			double y = 0.0;
			for (Map.Entry m : categorizerClassé.entrySet()) {

				if (y == 0) {
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
		} */
	/**
	 * retourne une HashMap qui a pour clé un ensemble de l'extension passée en paramètre 
	 * (complète) et en valeur la moyenne des rangs 
	 * @param extension
	 * @param rang
	 * @return
	 */
		/* public static HashMap<ArrayList<Integer>,Double>  avgPourUneExtension(ArrayList<ArrayList<Integer>> extension,HashMap<Integer, Integer>rang){
			HashMap<ArrayList<Integer>,Double> avgEnsemble = new HashMap<ArrayList<Integer>,Double> ();
			for(ArrayList<Integer> ext : extension) {
				double avg = Utile.avgPourUnEns(ext, rang);
				avgEnsemble.put(ext, avg);
			}
			return avgEnsemble ; 
		}
		/**
		 * retourne les extensions de RBE
		 * @param avgPourext
		 * @param extension
		 * @param rang
		 * @return
		 */
	/*
		public  ArrayList<ArrayList<Integer>> semantiqueRBE(HashMap<ArrayList<Integer>,Double> avgPourext,ArrayList<ArrayList<Integer>> extension,HashMap<Integer, Integer>rang){
			avgPourext = avgPourUneExtension(extension,rang);
			ArrayList<ArrayList<Integer>> rbe = new ArrayList<ArrayList<Integer>>();
			double x = 100;
			for (Map.Entry m : avgPourext.entrySet()) {
				if((double)m.getValue()<x) {
					x = (double)m.getValue() ; 
				}
			}
			
			for (Map.Entry m : avgPourext.entrySet()) {
				if((double)m.getValue()==x) {
					rbe.add((ArrayList<Integer>)m.getKey());
				}
			}
			return rbe;
		}
		
		public int [][] suppresssionAttaques(HashMap<Integer, Integer>  rang ){
			int [][] newAttaques =  new int [attaques.length][attaques.length]  ; 
			for(int i =0;i<attaques.length ; i++) {
				for(int j =0;j<attaques.length ; j++) {
					newAttaques[i][j] =attaques[i][j];
				}
			}
			for(int i =0;i<attaques.length ; i++) {
				for(int j =0;j<attaques.length ; j++) {
					if(attaques[i][j]==1) {
						if(rang.get(i)>rang.get(j)) {
							newAttaques[i][j]=0 ;
						}
					}
				}
			}
			return newAttaques;
		}*/
	
	
	
	
	


}
