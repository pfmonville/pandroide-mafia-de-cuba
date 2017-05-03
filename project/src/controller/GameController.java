package controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.controlsfx.control.Notifications;

import controller.ia.AgentStrategy;
import controller.ia.CleanerStrategy;
import controller.ia.DriverStrategy;
import controller.ia.GodFatherStrategy;
import controller.ia.IAController;
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
import controller.runnable.AnswerQuestionRunnable;
import controller.runnable.ChooseGodFathersActionRunnable;
import controller.runnable.ChooseQuestionRunnable;
import controller.runnable.EmptyPocketsRunnable;
import controller.runnable.GetBackTheBoxRunnable;
import controller.runnable.PickSomethingRunnable;
import controller.runnable.PrepareBoxRunnable;
import error.PickingStrategyError;
import error.PrepareBoxStrategyError;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.util.Duration;
import model.GodFather;
import model.SecretID;
import model.Talk;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;


public class GameController {

	private int currentPlayer;
	private int currentTurn;
	private int numberOfPlayers; // nombre de joueurs, parrain inclus
	private int humanPosition;
	private HashMap<Integer, Player> players = new HashMap<Integer,Player>();
	private HashMap<Integer, PlayerController> playerControllers = new HashMap<Integer, PlayerController>();

	private ArrayList<Question> questions;
	private ArrayList<Answer> answers;
	private ArrayList<Talk> gameHistory;
	private boolean firstHalf;
	private int numberOfThieves;
	private int numberOfThievesCaught;
	private int diamondsTakenBack;
	private Box box;
	private String tokenHidden;
	private int diamondsHidden;
	
	private Rules rules = new Rules();
	public GameController(){
	}	
	
	/**
	 * get the updated rules by the the user after the optionview panel
	 */
	private void getRules(){
		//for the first part of the game, the godFather doesn't play
		this.setTokenHidden(null);
		setDiamondsHidden(0);
		this.currentPlayer = 1;
		this.currentTurn = 0;
		this.firstHalf = true;
		this.numberOfThievesCaught = 0;
		this.setDiamondsTakenBack(0);
		this.numberOfPlayers = App.rules.getCurrentNumberOfPlayer();
		this.gameHistory = new ArrayList<>();
		this.humanPosition = App.rules.getHumanPosition();
		this.box = App.rules.getBox();
		this.questions = App.rules.getQuestions();
		this.answers = App.rules.getAnswers();
	}
	
	
	/**
	 * after the first half, call this method to update the number of thieves
	 */
	private void updateRules(){
		int numberOfThieves = 0;
		for(Player player: players.values()){
			if(player.isThief()){
				numberOfThieves ++;
			}
		}
		this.numberOfThieves = numberOfThieves;
		this.firstHalf = false;
	}
	
	
	/**
	 * 
	 * @param player
	 */
	public void gameOverForPlayer(Player player){
		
		
	}
	
	
	/**
	 * update the currentPlayer attribute to match the next player number for the first half of the game
	 * @return the next index of the player or -1 if all players have played the first half
	 */
	public int nextPlayer(){
		if(!this.firstHalf){
			return -1;
		}
		if (this.currentPlayer == this.numberOfPlayers){ 
			this.currentPlayer = 0;
			return -1;
		}else{
			this.currentPlayer += 1;
			return this.currentPlayer;

		}
	}
	
	
	/**
	 * get the player whose turn it is
	 * @return Player
	 */
	public Player getCurrentPlayer(){
		return players.get(this.currentPlayer);
	}
	
	/**
	 * get the player number whose turn it is
	 * @return int : the index of the player
	 */
	public int getCurrentPlayerNumber(){
		return this.currentPlayer;
	}
	
	/**
	 * get the number of players
	 * @return the number of players
	 */
	public int getNumberOfPlayers(){
		return numberOfPlayers ;
	}
	
	public int getActualTurn() {
		return currentTurn;
	}


	public ArrayList<Talk> getGameHistory() {
		return gameHistory;
	}


	public boolean isFirstHalf() {
		return firstHalf;
	}


	public int getNumberOfThievesCaught() {
		return numberOfThievesCaught;
	}


	/**
	 * 
	 */
	public void startGame(){
		this.currentTurn = 1;
		this.prepareBox();
	}
	
	
	//*********************************************************************
	//First Half functions
	//*********************************************************************
	
	/**
	 * ask to the godFather how many diamonds he wants to hide
	 */
	private void prepareBox(){
		Platform.runLater( ()-> App.gv.displayBoxAnimation());
		//PAUSE
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.getCurrentPlayer().setBox(box.clone());
		if(this.isCurrentPlayerHuman()){
			App.gv.godFatherHideDiamondsView() ;
		}else{
			Thread thread = new Thread(new PrepareBoxRunnable(this.box, playerControllers.get(1)));
			thread.start();
		}
	}
	
	/**
	 * receive the number of diamonds hidden by the godFather and start passing the box
	 * @param numberOfDiamondsHidden
	 * @throws PrepareBoxStrategyError 
	 */
	public void responsePrepareBox(int numberOfDiamondsHidden) throws PrepareBoxStrategyError{
		if(numberOfDiamondsHidden > App.rules.getMaxHiddenDiamonds() || App.rules.getNumberOfDiamonds() - numberOfDiamondsHidden < 0){
			throw new PrepareBoxStrategyError("you can't hide this much of diamonds");
		}else{
			this.box.removeDiamonds(numberOfDiamondsHidden);
			this.setDiamondsHidden(numberOfDiamondsHidden);
			((GodFather)players.get(1).getRole()).hideDiamonds(numberOfDiamondsHidden);
		}
		this.currentPlayer = 2;
		this.nextTurn();
	}
	
	/**
	 * Start the next turn in the first half of the game
	 */
	private void nextTurn(){
		//Forcing pause (only for testing)
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Platform.runLater( ()-> App.gv.displayBoxAnimation());
		if(this.isCurrentPlayerHuman()){
			Platform.runLater(() -> App.gv.playerPickView());
		}else{
			Thread thread = new Thread(new PickSomethingRunnable(this.currentPlayer, this.box, playerControllers.get(this.currentPlayer)));
			thread.start();
		}
	}
	
	/**
	 * End this turn of the first half
	 * @throws PickingStrategyError 
	 */
	public void endTurn(int position, int diamondsPicked, String tokenPicked, String tokenHidden) throws PickingStrategyError{

		this.getCurrentPlayer().setBox(box.clone()); 
		if(tokenHidden != null){
			if(players.get(this.currentPlayer).isFirstPlayer() && App.rules.isFirstPlayerCanHide() && App.rules.isAValidToken(tokenHidden)){
				this.setTokenHidden(tokenHidden);
				this.box.removeToken(tokenHidden);
			}else{
				throw new PickingStrategyError("either you're not the first player or the token name is not valid");
			}
		}
		if(diamondsPicked > 0 && tokenPicked != null){
			throw new PickingStrategyError("you can't take a token and steal diamonds");
		}
		if(tokenPicked != null){
			if(App.rules.isAValidToken(tokenPicked)){
				if(!this.box.removeToken(tokenPicked)){
					throw new PickingStrategyError("this token is not in the box");
				}
				players.get(position).takeToken(tokenPicked);
				
			}else{
				throw new PickingStrategyError("not a valid token");
			}
		}else if(diamondsPicked > 0){
			if(!this.box.removeDiamonds(diamondsPicked)){
				throw new PickingStrategyError("there is not enough diamonds in the box");
			}
			players.get(position).takeDiamonds(diamondsPicked);
		}else if(this.box.isEmpty()){
			//since tokenPicked is null takeToken automatically assigns streetUrchin to this player
			players.get(position).takeToken("");
		}else{
			throw new PickingStrategyError("you have to choose either to pick diamond(s) or a token");
		}
		
		// if the current player is an AI
		if(!this.isCurrentPlayerHuman()){
			// the AI creates all the possible worlds for the players after him, based on the box content
			((IAController) playerControllers.get(this.currentPlayer)).createWorldsAfterVision(this.box);
		}
		
		//if this is the last player then start the second half
		if(players.get(this.currentPlayer).isLastPlayer()){
			//Forcing pause (only for testing)
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Platform.runLater(() ->App.gv.displayBoxAnimation());
			this.players.get(1).setBox(box.clone());
			this.currentPlayer = 1;
			this.currentTurn = 1;
			if(App.rules.isAllIA())
				Platform.runLater(()->App.gv.createInfoBoxIA());
			beginSecondHalf();
		}else{
			this.currentPlayer ++;
			this.currentTurn ++;
			this.nextTurn();
		}
		
	}
	
	
	//*********************************************************************
	//Second Half functions
	//*********************************************************************
	
	private void giveTheBoxToGodFather(){
		Thread thread = new Thread(new GetBackTheBoxRunnable(playerControllers.get(1), this.box));
		thread.start();
		//Platform.runLater(thread);
	}
	
	public void SelectingGodFathersAction(){
		if(humanPosition == this.currentPlayer){
			Platform.runLater(() -> App.gv.displayGFQuestions());
		}else{
			Thread thread = new Thread(new ChooseGodFathersActionRunnable(playerControllers.get(1)));
			thread.start();
		}
	}
	
	/**
	 * only called from the AI
	 * @param action a 0 means it wants to ask questions, a 1 means it wants to empty someone's pockets
	 */
	public void getGodFathersAction(int action){
		// if it's 0 then 
		if (action == 0){
			Thread thread = new Thread(new ChooseQuestionRunnable(playerControllers.get(1), questions));
			thread.start();
		}else{
			Thread thread = new Thread(new EmptyPocketsRunnable(playerControllers.get(1)));
			thread.start();
		}
	}
	
	public void askTo(Question questionToAsk){
		//TODO display pop up informing everyone on the question asked
		Notifications.create()
        	.title("Action en cours")
        	.text("Le Parrain pose au joueur " + questionToAsk.getTargetPlayer() + " la question suivante :\n" + questionToAsk.getContent())
        	.position(Pos.CENTER)
        	.owner(App.mainStage)
        	.hideAfter(Duration.seconds(3))
        	.showInformation();
		if(humanPosition == questionToAsk.getTargetPlayer()){
			App.gv.displayPlayerAnswers();
		}else{
			Thread thread = new Thread(new AnswerQuestionRunnable(playerControllers.get(questionToAsk.getTargetPlayer()), questionToAsk, answers));
			thread.start();
		}
	}
	
	public void emptyPocketsTo(int targetPlayer){
		//TODO handle the Cleaner shot
		//TODO reveal the identity of the targetPlayer
		SecretID secret = players.get(targetPlayer).reveal();
		if(secret.getRole() == App.rules.getNameAgentCIA() || secret.getRole() == App.rules.getNameAgentFBI() || secret.getRole() == App.rules.getNameAgentLambda()){
			//TODO display losing : the agent alone has won
		}
		if(secret.getRole() == App.rules.getNameThief()){
			this.numberOfThievesCaught += 1;
			this.setDiamondsTakenBack(this.getDiamondsTakenBack() + secret.getDiamondsTaken());
			//update the number of diamonds taken back in display
			if(hasGodFatherWon()){
				//TODO display winning 
			}
		}else{
			System.out.println("role du joueur ciblé " + secret.getRole());
			if(((GodFather)players.get(1).getRole()).consumeJoker()){
				//TODO : display one less joker and everyone knows who is the target
				System.out.println("on sait qui est cette personne");
				for(PlayerController pc: playerControllers.values()){
					((IAController)pc).updateWorldsVision(secret);
				}
				SelectingGodFathersAction();
			}else{
				//TODO : display losing : thieves have won
				System.out.println("le parrain a perdu");
			}
		}
	}
	
	public void getAnswerToQuestion(Question question, Answer answer){
		//TODO display pop up informing everyone on the answer
		Platform.runLater(()->
			Notifications.create()
	    		.title("Action en cours")
	    		.text("Le joueur " + question.getTargetPlayer() + " répond :\n" + answer.getContent())
	    		.position(Pos.CENTER)
	    		.owner(App.mainStage)
	        	.hideAfter(Duration.seconds(5))
	    		.showInformation());
		//TODO update log creating a Talk
		Talk talk = new Talk(question, answer);
		this.gameHistory.add(talk);
		Platform.runLater(() -> App.gv.displayGameHistory());
		updateIAWorldsVisions();
		SelectingGodFathersAction();
	}
	
	
	public void updateIAWorldsVisions(){
		for(PlayerController pc: playerControllers.values()){
			((IAController)pc).updateWorldsVision(this.gameHistory.get(gameHistory.size()-1));
		}
	}
	
	
	/**
	 * to know if a the current player is human
	 * @return true if the player is human false otherwise
	 */
	public boolean isCurrentPlayerHuman(){
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
	 * gets informations from the updating rules after the option view and creates corresponding players
	 */
	private void getPlayers(){
		for(int i = 1; i < this.numberOfPlayers+1; i++){
			if(this.humanPosition == i){
				System.out.println("joueur humain en position " + i);
				this.players.put(i, new Player(i, true, false));
			}else{
				this.players.put(i, new Player(i, false, false));
			}
		}
		//set the last player boolean "lastPlayer" to true
		this.players.get(this.numberOfPlayers).setLastPlayer(true);
		//set the role for the first player to GodFather
		this.players.get(1).setRole(new GodFather(App.rules.getNumberOfJokers()));
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
	private void getControllers(){
		
		for(Map.Entry<Integer, Player> entry : players.entrySet()){
			Player player = entry.getValue();
			int position = entry.getKey();
			if (player.isHuman()){
				playerControllers.put(position, new HumanController(player));
			}
			else{
				if(position == 1){
					playerControllers.put(position, new IAGodFatherController(player));
				}else{
					playerControllers.put(position, new IASuspectController(player));
				}
			}
		}
	}
	
	
	/**
	 * update all controllers to add the strategy
	 * call this method after the first part of the game
	 */
	private void updateControllers(){
		for(Map.Entry<Integer, Player> entry : players.entrySet()){
			Player player = entry.getValue();
			int position = entry.getKey();
			if (!player.isHuman()){
				System.out.println("player = " + player.toString() +" position "+ player.getPosition() + " isHuman " + player.isHuman());
				System.out.println(" role = " + player.getRole().toString());
				System.out.println( " roleName = " + player.getRole().getName());
				switch(player.getRole().getName()){
				case("Parrain"):
					((IAGodFatherController) playerControllers.get(position)).addStrategy(new GodFatherStrategy());
					break;
				case("Fidèle"):
					((IASuspectController) playerControllers.get(position)).addStrategy(new LoyalHenchmanStrategy());
					break;
				case("Nettoyeur"):
					((IASuspectController) playerControllers.get(position)).addStrategy(new CleanerStrategy());
					break;
				case("Chauffeur"):
					((IASuspectController) playerControllers.get(position)).addStrategy(new DriverStrategy());
					break;
				case("Voleur"):
					((IASuspectController) playerControllers.get(position)).addStrategy(new LoyalHenchmanStrategy());
					break;
				case("Enfant des rues"):
					((IASuspectController) playerControllers.get(position)).addStrategy(new StreetUrchinStrategy());
					break;
				case("Agent"):
					((IASuspectController) playerControllers.get(position)).addStrategy(new AgentStrategy());
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
	 * to start a game
	 */
	public void beginFirstHalf(){
		this.getRules();
		this.getPlayers();
		this.getControllers();
		this.startGame();
	}
	
	
	/**
	 * start second half of the game
	 */
	public void beginSecondHalf(){
		this.updateRules();
		this.updateControllers();
		this.giveTheBoxToGodFather();
	}
	
	
	/**
	 * restart the game with the same settings
	 */
	public void restart(){
		this.finish();
		this.beginFirstHalf();
	}
	
	/**
	 * end game function
	 */
	public void finish(){
		playerControllers = new HashMap<>();
		players = new HashMap<>();
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
	
	public Box getBox(){
		return box ;
	}
	
	public ArrayList<Answer> getAnswers(){
		return answers;
	}


	public int getDiamondsHidden() {
		return diamondsHidden;
	}


	public void setDiamondsHidden(int diamondsHidden) {
		this.diamondsHidden = diamondsHidden;
	}
	
	public Player getHumanPlayer(){
		return players.get(humanPosition);
	}


	public String getTokenHidden() {
		return tokenHidden;
	}


	public void setTokenHidden(String tokenHidden) {
		this.tokenHidden = tokenHidden;
	}


	public int getDiamondsTakenBack() {
		return diamondsTakenBack;
	}


	public void setDiamondsTakenBack(int diamondsTakenBack) {
		this.diamondsTakenBack = diamondsTakenBack;
	}
}
