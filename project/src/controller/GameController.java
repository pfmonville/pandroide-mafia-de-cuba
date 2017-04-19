package controller;

import java.io.InputStream;
import java.util.ArrayList;

import controller.ia.AgentStrategy;
import controller.ia.CleanerStrategy;
import controller.ia.DriverStrategy;
import controller.ia.GodFatherStrategy;
import controller.ia.IAGodFatherController;
import controller.ia.IASuspectController;
import controller.ia.LoyalHenchmanStrategy;
import controller.ia.StreetUrchinStrategy;
import controller.ia.ThiefStrategy;
import model.Answer;
import model.Box;
import model.Player;
import model.Question;
import model.Rules;
import model.Talk;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;


public class GameController {
	private static int currentPlayer;
	private static int currentTurn;
	/*
	 * AJOUT DE LA VALEUR POUR LE DEBUG DE LA CLASSE IACONTROLLER VIA LA CLASSE MAINTESTIACONTROLLER
	 */
	private static int numberOfPlayers = 10;
	private ArrayList<Player> players;
	private ArrayList<PlayerController> playerControllers;
	private ArrayList<Question> questions;
	private ArrayList<Answer> answers;
	private ArrayList<Talk> gameHistory;
	private boolean firstHalf;
	private int numberOfThieves;
	private static int numberOfThievesCaught;
	
	private Rules rules = new Rules();
	private Box box /*= new Box(rules.getNumberOfDiamonds(), rules.getTokensFor(numberOfPlayers))*/;
	
	public GameController(){
	}	
	
	/**
	 * get the updated rules by the the user after the optionview panel
	 */
	private void getRules(){
		//TODO :
	}
	
	
	/**
	 * 
	 * @param player
	 */
	public void gameOverForPlayer(Player player){
		
		
	}
	
	
	/**
	 * update the currentPlayer attribute to match the next player number for the first part of the game
	 */
	public void nextPlayer(){
		if (this.currentPlayer == this.numberOfPlayers){ 
			this.currentPlayer = 1;
		}else{
			this.currentPlayer += 1;
		}
	}
	
	
	/**
	 * get the player whose turn it is
	 * @return Player
	 */
	public Player getActualPlayer(){
		return players.get(this.currentPlayer);
	}
	
	/**
	 * get the player number whose turn it is
	 * @return int : the index of the player
	 */
	public int getActualPlayerNumber(){
		return this.currentPlayer;
	}
	
	/**
	 * get the number of players
	 * @return the number of players
	 */
	public static int getNumberOfPlayers(){
		return numberOfPlayers ;
	}
	
	/**
	 * 
	 */
	public void startGame(){
		this.currentTurn = 1;
		if(!this.isActualPlayerHuman()){
			Thread thread = new Thread((IAController)(this.playerControllers.get(this.currentPlayer)));
			thread.start();
		}
	}
	
	
	/**
	 * to know if a the actual player is human
	 * @param playerNumber (int) : actual player index
	 * @return true if the player is human false otherwise
	 */
	public boolean isActualPlayerHuman(){
		return this.isPlayerHuman(this.currentPlayer);
	}
	
	
	/**
	 * to know if a player in particular is human
	 * @param playerNumber (int) : player index
	 * @return true if the player is human, false otherwise
	 */
	public boolean isPlayerHuman(int playerNumber){
		if (players.get(playerNumber).isHuman()){
			return true;
		}
		return false;
	}
	

	
	/**
	 * gets informations from optionview and creates corresponding players
	 */
	private void getPlayers(){
		//TODO
//		String iahuJ1 = App.ov.getIahuJ1().getSelectedToggle().getUserData().toString();
//		String iahuJ2 = App.ov.getIahuJ2().getSelectedToggle().getUserData().toString();
//		String nvj1 = App.ov.getNvj1().getSelectedToggle().getUserData().toString();
//		String nvj2 = App.ov.getNvj2().getSelectedToggle().getUserData().toString();
//		boolean isHuman;
//		isHuman = (iahuJ1 == "ia") ? false: true;
//		players.add(new Player(true, isHuman, Double.valueOf(nvj1).intValue(), 9));
//		isHuman = (iahuJ2 == "ia") ? false: true;
//		players.add(new Player(false, isHuman, Double.valueOf(nvj2).intValue(), 9));
	}
	
	
	/**
	 * called if the human player clicked on a valid button to ask something or answer something
	 * @param int : ID the id of the question or answer selected
	 */
	public void validClick(int ID){
		//TODO
		Thread thread = new Thread((HumanController)(this.playerControllers.get(this.currentPlayer)));
		thread.start();
	}
	
	/**
	 * to know if the godFather's side has won
	 * @return true if all the thieves got caught, false if the game continues
	 */
	private boolean hasGodFatherWon(){
		if(this.numberOfThievesCaught == this.numberOfThieves){
			return true;
		}
		return false;
	}

	
	/**
	 * create all the controllers for players
	 */
	private void launchControllers(){
		
		for(Player player : players){
			if (player.isHuman()){
				playerControllers.add(new HumanController(player));
			}
			else{
				switch(player.getRole().getName()){
				case("GodFather"):
					playerControllers.add((PlayerController) new IAGodFatherController(player, box, rules, numberOfPlayers, new GodFatherStrategy()));
					break;
				case("LoyalHenchman"):
					playerControllers.add((PlayerController) new IASuspectController(player, box, rules, numberOfPlayers, new LoyalHenchmanStrategy()));
					break;
				case("Cleaner"):
					playerControllers.add((PlayerController) new IASuspectController(player, box, rules, numberOfPlayers, new CleanerStrategy()));
					break;
				case("Driver"):
					playerControllers.add((PlayerController) new IASuspectController(player, box, rules, numberOfPlayers, new DriverStrategy()));
					break;
				case("Thief"):
					playerControllers.add((PlayerController) new IASuspectController(player, box, rules, numberOfPlayers, new ThiefStrategy()));
					break;
				case("StreetUrchin"):
					playerControllers.add((PlayerController) new IASuspectController(player, box, rules, numberOfPlayers, new StreetUrchinStrategy()));
					break;
				case("Agent"):
					playerControllers.add((PlayerController) new IASuspectController(player, box, rules, numberOfPlayers, new AgentStrategy()));
					break;
				}
			}
		}
	}
	
	
	/**
	 * play a sound
	 * @param sound path to the sound to play
	 */
	public void playSound(String sound){
	    try{
		    InputStream in = getClass().getResourceAsStream("/"+sound);
		 
		    // create an audiostream from the inputstream
		    AudioStream audioStream = new AudioStream(in);
		 
		    // play the audio clip with the audioplayer class
		    AudioPlayer.player.start(audioStream);
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
	}
	
	/**
	 * main operation that launches the controller
	 */
	public void begin(){
		this.getRules();
		this.getPlayers();
		this.launchControllers();
		this.startGame();
	}
	
	
	public void restart(){
		this.finish();
		this.begin();
	}
	
	/**
	 * fonction de fin de partie ou lors du retour 
	 */
	public void finish(){
		playerControllers = new ArrayList<>();
		players = new ArrayList<>();
		currentPlayer = 0;
		//App.pv.resetCursor();
//		if(App.gv.getPanel().getChildren().contains(App.gv.getCursor())){
//			App.gv.removeWaitingCursor();
//		}
	}


	public ArrayList<Question> getQuestions() {
		return questions;
	}
	
	public int getNbThiefsCaught(){
		return numberOfThievesCaught;
	}

}
