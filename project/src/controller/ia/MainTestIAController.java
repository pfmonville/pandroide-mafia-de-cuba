package controller.ia;

import java.util.ArrayList;
import java.util.List;

import controller.App;
import model.Box;
import model.Player;
import model.Role;

public class MainTestIAController {

	public static void main(String[] args){
		ArrayList<String> stringList = new ArrayList<String>();
		stringList.add(App.rules.getNameDriver());
//		stringList.add(App.rules.getNameLoyalHenchman());
		stringList.add(App.rules.getNameAgent());
		
		
		int nombreDeDiamantsDansBoite = 0;
		Box testBox = new Box(nombreDeDiamantsDansBoite, stringList);
		
		int positionDuJoueur = 2;
		
		// Joueur voleur
//		Player p = new Player(null, positionDuJoueur, false, false);
//		int nombreDeDiamantsVoles = 13;
//		p.takeDiamonds(nombreDeDiamantsVoles);
		
		// Joueur non voleur
		String nomDuRole = "Driver";
		Player p = new Player(new Role(nomDuRole), positionDuJoueur, false, false);
		
		IAController iac = new IAController(p);
		
		ArrayList<ArrayList<String>> result = iac.rolesDistribution(testBox);
		
		for (ArrayList<String> al : result) {
	        String appender = "";
	        for (String i : al) {
	            System.out.print(appender + i);
	            appender = " ";
	        }
	        System.out.println();
	    }
		System.out.println("nb config: "+result.size() );
		
//		ArrayList<String> liste = new ArrayList<String>();
//		liste.add("A");
//		liste.add("B");
//		liste.add("C");
//		liste.add("D");
//		liste.add("E");
//		
//		ArrayList<ArrayList<String>> result = IAController.partialPermutation(liste, 4);
//		for (ArrayList<String> al : result) {
//	        String appender = "";
//	        for (String i : al) {
//	            System.out.print(appender + i);
//	            appender = " ";
//	        }
//	        System.out.println();
//	    }
//		System.out.println("nb config: "+result.size() );
//		System.out.println(liste);
		
	}

}
