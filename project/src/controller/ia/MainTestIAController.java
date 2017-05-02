package controller.ia;

import java.util.ArrayList;
import java.util.List;

import controller.App;
import model.Box;
import model.Player;
import model.Role;

public class MainTestIAController {

	public static void main(String[] args){
		// Initialisation de la boite que recoit le joueur courant
		ArrayList<String> stringList = new ArrayList<String>();
		stringList.add(App.rules.getNameLoyalHenchman());
//		stringList.add(App.rules.getNameLoyalHenchman());
//		stringList.add(App.rules.getNameLoyalHenchman());
//		stringList.add(App.rules.getNameLoyalHenchman());
//		stringList.add(App.rules.getNameLoyalHenchman());
//		stringList.add(App.rules.getNameAgent());
//		stringList.add(App.rules.getNameAgent());
//		stringList.add(App.rules.getNameDriver());
//		stringList.add(App.rules.getNameDriver());
		int nombreDeDiamantsDansBoite = 15;
		Box testBox = new Box(nombreDeDiamantsDansBoite, stringList);
		
		// Initialisation du joueur courant
		int positionDuJoueur = 6; // inclus dans [1 ; n-1], le parrain est le joueur 0
		Player p = new Player(null, positionDuJoueur, false, false);
		
		/*
		 *  Il fallait metre en du dans la classe GameController,
		 *  une valeur pour la variable numberOfPlayers
		 */
		IAController iac = new IAController(p);
		
		// Le joueur recoit la boite. Maj des configBefore
		long start = System.currentTimeMillis();
		iac.rolesDistributionBefore(testBox);
		long endConfigBefore = System.currentTimeMillis() - start;
		
		// Le joueur choisi ce qu'il prend
		// Joueur voleur
//		int nombreDeDiamantsVoles = 15;
//		iac.getPlayer().takeDiamonds(nombreDeDiamantsVoles);
		
		// Joueur non voleur
		String nomDuRole = App.rules.getNameLoyalHenchman();
		iac.getPlayer().setRole(new Role(nomDuRole));
		
		/*
		 *  TODO: MAJ de l'etat de la boite (N'EST PAS SENSE ETRE FAIT DANS LA METHODE rolesDistributionBefore)
		 *  + Maj des configAfter
		 */
		start = System.currentTimeMillis();
		ArrayList<ArrayList<Integer>> resultConfigAfter = iac.rolesDistributionAfter(testBox);
		long endConfigAfter = System.currentTimeMillis() - start;
		
		// Affichage des configBefore
		System.out.println("*** ConfigBefore ***");
		for (ArrayList<Integer> al : iac.getConfigBefore()) {
	        String appender = "";
	        for (Integer i : al) {
	            System.out.print(appender + App.rules.convertNumberIntoRoleName(i));
	            appender = " ";
	        }
	        System.out.println();
	    }
		System.out.println("nb configBefore: "+iac.getConfigBefore().size() );
		System.out.println("temps d'execution : "+endConfigBefore);
		System.out.println();
		
		// Affichage des configAfter
		System.out.println("*** ConfigAfter ***");
		for (ArrayList<Integer> al : resultConfigAfter) {
	        String appender = "";
	        for (Integer i : al) {
	            System.out.print(appender + App.rules.convertNumberIntoRoleName(i));
	            appender = " ";
	        }
	        System.out.println();
	    }
		System.out.println("nb configAfter: "+resultConfigAfter.size() );
		System.out.println("temps d'execution : "+endConfigAfter);
		System.out.println();
		
	}

}


