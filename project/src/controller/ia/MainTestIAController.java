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
		stringList.add(App.rules.getNameLoyalHenchman());
		stringList.add(App.rules.getNameAgent());
		
		
		int nombreDeDiamantsDansBoite = 3;
		Box testBox = new Box(nombreDeDiamantsDansBoite, stringList);
		
		int positionDuJoueur = 2;
		
		// Joueur voleur
		Player p = new Player(null, positionDuJoueur, false, false);
		p.takeDiamonds(3);
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
		
	}

}