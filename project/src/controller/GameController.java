package controller;

import java.io.InputStream;
import java.util.ArrayList;

import org.controlsfx.control.Notifications;

import controller.ia.AgentStrategy;
import controller.ia.CleanerStrategy;
import controller.ia.DriverStrategy;
import controller.ia.GodFatherStrategy;
import controller.ia.IAGodFatherController;
import controller.ia.IASuspectController;
import controller.ia.IGodFatherStrategy;
import controller.ia.LoyalHenchmanStrategy;
import controller.ia.StreetUpchinStrategy;
import controller.ia.ThiefStrategy;
import javafx.application.Platform;
import javafx.util.Duration;
import model.Answer;
import model.Player;
import model.Question;
import model.Talk;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;


public class GameController {
	private int actualPlayer;
	private int actualTurn;
	private int numberOfPlayers;
	private ArrayList<Player> players;
	private ArrayList<PlayerController> playerControllers;
	private ArrayList<Question> questions;
	private ArrayList<Answer> answers;
	private ArrayList<Talk> gameHistory;
	private boolean firstHalf;
	private int numberOfThieves;
	private int numberOfThievesCaught;
	
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
	 * update the actualPlayer attribute to match the next player number for the first part of the game
	 */
	public void nextPlayer(){
		if (this.actualPlayer == this.numberOfPlayers){ 
			this.actualPlayer = 1;
		}else{
			this.actualPlayer += 1;
		}
	}
	
	
	/**
	 * get the player whose turn it is
	 * @return Player
	 */
	public Player getActualPlayer(){
		return players.get(this.actualPlayer);
	}
	
	/**
	 * get the player number whose turn it is
	 * @return int : the index of the player
	 */
	public int getActualPlayerNumber(){
		return this.actualPlayer;
	}
	
	/**
	 * get the number of players
	 * @return the number of players
	 */
	public int getNumberOfPlayer(){
		return numberOfPlayers ;
	}
	
	/**
	 * 
	 */
	public void startGame(){
		this.actualTurn = 1;
		if(!this.isActualPlayerHuman()){
			Thread thread = new Thread((IAController)(this.playerControllers.get(this.actualPlayer)));
			thread.start();
		}
	}
	
	
	/**
	 * to know if a the actual player is human
	 * @param playerNumber (int) : actual player index
	 * @return true if the player is human false otherwise
	 */
	public boolean isActualPlayerHuman(){
		return this.isPlayerHuman(this.actualPlayer);
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
		Thread thread = new Thread((HumanController)(this.playerControllers.get(this.actualPlayer)));
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
					playerControllers.add((PlayerController) new IAGodFatherController(new GodFatherStrategy()));
					break;
				case("LoyalHenchman"):
					playerControllers.add((PlayerController) new IASuspectController(new LoyalHenchmanStrategy()));
					break;
				case("Cleaner"):
					playerControllers.add((PlayerController) new IASuspectController(new CleanerStrategy()));
					break;
				case("Driver"):
					playerControllers.add((PlayerController) new IASuspectController(new DriverStrategy()));
					break;
				case("Thief"):
					playerControllers.add((PlayerController) new IASuspectController(new ThiefStrategy()));
					break;
				case("StreetUpchin"):
					playerControllers.add((PlayerController) new IASuspectController(new StreetUpchinStrategy()));
					break;
				case("Agent"):
					playerControllers.add((PlayerController) new IASuspectController(new AgentStrategy()));
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
		actualPlayer = 0;
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
