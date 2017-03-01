package controller.ia;

import java.util.ArrayList;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import model.Box;
import model.Player;
import model.Rules;

public class IAController implements Runnable{
	private Player player;
	private Rules rules;
	private int diamondsLeftWhenBoxReceived;
	private ArrayList<String> tokensLeftWhenBoxReceived;
	private Graph myKnowledge;
	private int numberOfPlayers;
	
	
	public IAController(Player player, Box box, Rules rules, int numberOfPlayers) {
		super();
		this.player = player;
		this.rules = rules;
		this.numberOfPlayers = numberOfPlayers;
		Object content[] = box.open();
		diamondsLeftWhenBoxReceived = (int) content[0];
		tokensLeftWhenBoxReceived = (ArrayList<String>) content[1];
	}

	public void run(){
		
	}
	
	/**
	 * initialize the possible worlds model for a player after choosing his role
	 * A MODIFIER, A COMPLETER, A CE QUE TU VEUX
	 */
	public void createMyKnowledge(){
		
		//roles left for the next players
		ArrayList<String> rolesLeft = (ArrayList<String>) tokensLeftWhenBoxReceived.clone();
		
		//roles taken (or hidden) by previous players
		ArrayList<String> rolesTaken = rules.getTokensFor(numberOfPlayers);
		for(String s : rolesLeft){
			rolesTaken.remove(s);
		}
		if(diamondsLeftWhenBoxReceived < rules.getNumberOfDiamonds()){
			rolesTaken.add(rules.getNameThief());
		}
		
		rolesLeft.remove(player.getRole());
		rolesLeft.add(rules.getNameStreetUpchin());
		if(diamondsLeftWhenBoxReceived != 0){
			rolesLeft.add(rules.getNameThief());
		}
		
		
		
		
//		myKnowledge = new SingleGraph("myKnowledge");
//		Node n= myKnowledge.addNode("test");
//		ArrayList<String> list = new ArrayList<String>();
//		n.addAttribute("roleDistribution", list);
	}
}

