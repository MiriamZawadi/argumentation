package ter;

import java.util.ArrayList;

public class verif {
	public static void main(String[] args) {
		
	ArrayList<ArrayList<Integer>> complet = new ArrayList<ArrayList<Integer>>();
	ArrayList<Integer> a = new ArrayList<Integer>();
	ArrayList<Integer> b = new ArrayList<Integer>();
	ArrayList<Integer> c = new ArrayList<Integer>();
	ArrayList<Integer> d= new ArrayList<Integer>();
	a.add(3);
	b.add(1);
	b.add(3);
	c.add(3);
	c.add(4);
	d.add(1);
	d.add(2);
	d.add(3);
	complet.add(a);
	complet.add(b);
	complet.add(c);
	
	int [][] m= {{1,2,3}};
	Dung l = new Dung(m);
	System.out.println(l.preferee(complet));
	}
	
}
