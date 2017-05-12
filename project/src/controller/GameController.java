package controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.controlsfx.control.Notifications;

import controller.ai.AIController;
import controller.ai.AIGodFatherController;
import controller.ai.AISuspectController;
import controller.ai.GodFatherStrategy;
import controller.ai.LoyalHenchmanStrategy;
import controller.ai.position.FirstPositionStrategy;
import controller.ai.position.LastPositionStrategy;
import controller.ai.position.MiddlePositionStrategy;
import controller.ai.position.SecondPositionStrategy;
import controller.runnable.AnswerQuestionRunnable;
import controller.runnable.ChooseGodFathersActionRunnable;
import controller.runnable.ChooseQuestionRunnable;
import controller.runnable.EmptyPocketsRunnable;
import controller.runnable.GetBackTheBoxRunnable;
import controller.runnable.PickSomethingRunnable;
import controller.runnable.PrepareBoxRunnable;
import error.PickingStrategyError;
import error.PrepareBoxStrategyError;
import error.RoleError;
import error.StrategyError;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.util.Duration;
import model.Answer;
import model.Box;
import model.Driver;
import model.GodFather;
import model.Inspect;
import model.Player;
import model.PlayersInfo;
import model.Question;
import model.SecretID;
import model.Talk;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import view.InspectView;


public class GameController {

	private int currentPlayer;
	private int currentTurn;
	private int numberOfPlayers; // nombre de joueurs, parrain inclus
	private int humanPosition;
	private HashMap<Integer, Player> players = new HashMap<Integer,Player>();
	private HashMap<Integer, PlayerController> playerControllers = new HashMap<Integer, PlayerController>();
	private PlayersInfo playersInfo = new PlayersInfo();
	
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
	
	private Thread mainThread = null;
	private long startTimer;
	private boolean hasTimerStarted = false;
	private long duration;
	
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
	 * @throws RoleError 
	 */
	private void updateRules() throws RoleError{
		int numberOfThieves = 0;
		for(Player player: players.values()){
			if(player.isThief()){
				numberOfThieves ++;
			}
		}
		this.numberOfThieves = numberOfThieves;
		this.firstHalf = false;
		
		for(Player player : this.players.values()){
			if(player.getRole().getName().equals(App.rules.getNameAgentCIA())){
				this.playersInfo.addCIA(player);
			}else if(player.getRole().getName().equals(App.rules.getNameAgentFBI())){
				this.playersInfo.addFBI(player);
			}else if(player.getRole().getName().equals(App.rules.getNameAgentLambda())){
				this.playersInfo.addAgent(player);
			}else if(player.getRole().getName().equals(App.rules.getNameCleaner())){
				this.playersInfo.addCleaner(player);
			}else if(player.getRole().getName().equals( App.rules.getNameDriver())){
				this.playersInfo.addDriver(player);
			}else if(player.getRole().getName().equals(App.rules.getNameLoyalHenchman())){
				this.playersInfo.addLoyalHenchman(player);
			}else if(player.getRole().getName().equals(App.rules.getNameStreetUrchin())){
				this.playersInfo.addStreetUrchin(player);
			}else if(player.getRole().getName().equals( App.rules.getNameThief())){
				this.playersInfo.addThief(player);
			}else if(player.getRole().getName().equals(App.rules.getNameGodFather())){
				this.playersInfo.setGodFather(player);
			}else{
				throw new RoleError("Wrong role detected either you add a new one or something went wrong");
			}
		}
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
	
	public ArrayList<Integer> getAllPlayersPosition(){
		ArrayList<Integer> result = new ArrayList<>();
		for(Player player: players.values()){
			result.add(player.getPosition());
		}
		
		return result;
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
		startTimer(500);
		this.getCurrentPlayer().setBox(box.clone());
		if(this.isCurrentPlayerHuman()){
			App.gv.godFatherHideDiamondsView() ;
		}else{
			Thread thread = new Thread(new PrepareBoxRunnable(this.box, playerControllers.get(1)));
			this.mainThread = thread;
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
		
		try {
			reachTimer();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.nextTurn();
	}
	
	/**
	 * Start the next turn in the first half of the game
	 */
	private void nextTurn(){
		//Forcing pause (only for testing)
		startTimer(500);
		Platform.runLater( ()-> App.gv.displayBoxAnimation());
		if(this.isCurrentPlayerHuman()){
			Platform.runLater(() -> App.gv.playerPickView());
		}else{
			Thread thread = new Thread(new PickSomethingRunnable(this.currentPlayer, this.box, playerControllers.get(this.currentPlayer)));
			this.mainThread = thread;
			thread.start();
		}
	}
	
	/**
	 * End this turn of the first half
	 * @throws PickingStrategyError 
	 */
	public void endTurn(int position, int diamondsPicked, String tokenPicked, String tokenHidden) throws PickingStrategyError{

		try {
			reachTimer();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		this.getCurrentPlayer().setBox(box.clone()); 

		if(diamondsPicked > 0 && tokenPicked != null){
			throw new PickingStrategyError("you can't take a token and steal diamonds");
		}
		if(tokenPicked != null){
			if(App.rules.isAValidToken(tokenPicked)){
				if(!this.box.removeToken(tokenPicked)){
					throw new PickingStrategyError("this token is not in the box :"+tokenPicked);
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
		}else if(this.box.isEmpty()||players.get(position).isLastPlayer()){
			//since tokenPicked is null takeToken automatically assigns streetUrchin to this player
			players.get(position).takeToken("");
		}else{
			throw new PickingStrategyError("you have to choose either to pick diamond(s) or a token");
		}
		
		if(tokenHidden != null){
			if(players.get(this.currentPlayer).isFirstPlayer() && App.rules.isFirstPlayerCanHide() && App.rules.isAValidToken(tokenHidden)){
				this.setTokenHidden(tokenHidden);
				this.box.removeToken(tokenHidden);
				System.out.println("numero du joueur " + this.getCurrentPlayer().getPosition());
				System.out.println("role =  " + this.getCurrentPlayer().getRole().toString());
				this.getCurrentPlayer().getRole().setHiddenToken(tokenHidden);
			}else{
				throw new PickingStrategyError("either you're not the first player or the token name is not valid");
			}
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
			if(App.rules.isAllAI())
				Platform.runLater(()->App.gv.createInfoBoxAI());
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
		this.mainThread = thread;
		thread.start();
		//Platform.runLater(thread);
	}
	
	public void SelectingGodFathersAction(){
		if(humanPosition == this.currentPlayer && currentTurn==1){ 
			Platform.runLater(() -> App.gv.displayGFQuestions());
		}else if (humanPosition != this.currentPlayer){
			startTimer(500);
			Thread thread = new Thread(new ChooseGodFathersActionRunnable(playerControllers.get(1)));
			this.mainThread = thread;
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
			this.mainThread = thread;
			thread.start();
		}else{
			Thread thread = new Thread(new EmptyPocketsRunnable(playerControllers.get(1)));
			this.mainThread = thread;
			thread.start();
		}
	}
	
	public void askTo(Question questionToAsk){
		try {
			reachTimer();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(questionToAsk.getTargetPlayer()==0) {
			createPopUp("Vous devez sélectionner un joueur.", "Attention", 3);
			return;
		}
		createPopUp("Le Parrain pose au joueur " + questionToAsk.getTargetPlayer() + " la question suivante :\n" + questionToAsk.getContent(), "Action en cours", 3);
		questionToAsk.setNumber(currentTurn);
		if(humanPosition == questionToAsk.getTargetPlayer()){
			App.gv.displayPlayerAnswers();
		}else{
			startTimer(500);
			Thread thread = new Thread(new AnswerQuestionRunnable(playerControllers.get(questionToAsk.getTargetPlayer()), questionToAsk, answers));
			this.mainThread = thread;
			thread.start();
		}
	}
	
	/**
	 * add all drivers that have a winning passenger at the current time
	 */
	public ArrayList<Player> getWinningDrivers(){
		ArrayList<Player> drivers = new ArrayList<>();
		for(Player player: playersInfo.getDrivers()){
			Player passenger = players.get(((Driver) player.getRole()).getProtectedPlayerPosition());
			if (playersInfo.getWinners().contains(passenger)){
				//one iteration is enough because we check in order who has won so a driver of a driver will be caught
				drivers.add(player);
			}
		}
		return drivers;
	}
	
	
	
	
	
	/**
	 * when the Godfather wants to make an announcement
	 * @param announceType : if he wants to share what he gave or got back in the box
	 */
	public void makeAnnouncement(String announceType){
		
		if(announceType == null){
			createPopUp("Sélectionnez le type d'annonce à faire.", "Attention", 3);
			return;
		}
		
		//for gameHistory
		Question q = new Question(-1,"Le Parrain fait une annonce.", new ArrayList<>(),-1);
		q.setTargetPlayer(0); q.setNumber(currentTurn);
		
		if(announceType.equals("Ce que vous avez donné")){
			int nbDiams =App.rules.getNumberOfDiamonds()-((GodFather)getHumanPlayer().getRole()).getNbDiamondsHidden(); 
			createPopUp("J'ai donné "+nbDiams+" diamants.", "Annonce du Parrain", 5);
			// ID = -1 for announcements
			Answer a = new Answer(-1,"J'ai donné "+nbDiams+" diamants." , new ArrayList<>());
			a.setNbDiamondsAnswer(nbDiams);
			gameHistory.add(new Talk(q, a));
			App.gv.displayGameHistory();
		}
		else{
			String content  ="J'ai reçu ";
			Set<String> rolesTypes = new HashSet<String>(box.getTokens());
			for (String role : rolesTypes){
				content += box.getCount(role)+" "+role+", ";
			}
			content+=box.getDiamonds()+" diamants.";
			createPopUp(content, "Annonce du Parrain", 5);
			Answer a = new Answer(-2,content , new ArrayList<>());
			a.setTokensAnswer(box.getTokens());
			a.setNbDiamondsAnswer(box.getDiamonds());
			gameHistory.add(new Talk(q, a));
			App.gv.displayGameHistory();
		} 
		this.currentTurn += 1;
		SelectingGodFathersAction();
	}
	
	
	
	
	public void emptyPocketsTo(int targetPlayer){
		try {
			reachTimer();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		if(targetPlayer==0) {
			createPopUp("Vous devez sélectionner un joueur.", "Attention", 3);
			return;
		}
		//display pop up
		createPopUp("Le Parrain demande au joueur " + targetPlayer + " de vider ses poches.\n", "Accusation", 4);
		//question and answer for the Talk object
		Question q = new Question(0, "Le Parrain demande au joueur " + targetPlayer + " de vider ses poches.", new ArrayList<>(), -1);
		q.setTargetPlayer(targetPlayer);
		q.setNumber(currentTurn);
		Answer a = new Answer(0,"",new ArrayList<>());
		//handle the Cleaner shot
		boolean targetHasBeenShot = false;
		Player cleanerWhoShot = null;
		ArrayList<Player> cleanersWantingToShoot = new ArrayList<>();
		for(Player player: this.playersInfo.getCleaners()){
			try {
				if(((AISuspectController) this.playerControllers.get(player.getPosition())).chooseToShoot(targetPlayer)){
					targetHasBeenShot = true;
					cleanersWantingToShoot.add(player);
				}
			} catch (StrategyError e) {
				e.printStackTrace();
			}
		}
		if(targetHasBeenShot){
			//choose one of the cleaners who wants to shoot (simulate which one was the fastest to speak)
			cleanerWhoShot = cleanersWantingToShoot.get(new Random().nextInt(cleanersWantingToShoot.size()));
		}
	
		App.gv.revealId(players.get(targetPlayer));
		if(cleanerWhoShot!=null) App.gv.revealId(cleanerWhoShot);
		//the list which will be sent to the view to display informations about players
		SecretID secret = players.get(targetPlayer).reveal();
		
		//if the target is an agent
		if(secret.getRole().equals(App.rules.getNameAgentCIA()) || secret.getRole().equals(App.rules.getNameAgentFBI() )|| secret.getRole().equals(App.rules.getNameAgentLambda())){
			//if a cleaner hasn't shot, the agent wins alone
			if(cleanerWhoShot == null){
				createPopUp("Le joueur ciblé est un "+ secret.getRole()+" ! Vous avez perdu... \n", "", 4);	
				a.setContent("Le joueur ciblé est un "+ secret.getRole()+" ! Vous avez perdu...");
				playersInfo.addWinner(this.players.get(targetPlayer));
				if(secret.getRole().equals(App.rules.getNameAgentCIA())){
					playersInfo.setWinningSide(PlayersInfo.CIA);
				}else if (secret.getRole().equals(App.rules.getNameAgentFBI())){
					playersInfo.setWinningSide(PlayersInfo.FBI);
				}else{
					playersInfo.setWinningSide(PlayersInfo.AGENT);
				}
			//else the cleaner wins alone
			}else{
				createPopUp("Le nettoyeur a tué un agent ! Vous avez perdu... \n", "", 4);
				a.setContent("Le nettoyeur a tué un agent ! Vous avez perdu...");
				playersInfo.addWinner(cleanerWhoShot);
				playersInfo.setWinningSide(PlayersInfo.CLEANER);
			}
			
			//add all winning drivers
			playersInfo.addWinners(this.getWinningDrivers());
			//display end banner
			Talk t =new Talk(q,a);
			gameHistory.add(t);
			App.gv.displayGameHistory();
			App.gv.displayEndBanner(playersInfo);
			playersInfo = new PlayersInfo();
			return ;
		}
		//if the target is a thief
		if(secret.getRole().equals(App.rules.getNameThief())){
			this.numberOfThievesCaught += 1;
			this.setDiamondsTakenBack(this.getDiamondsTakenBack() + secret.getDiamondsTaken());
			createPopUp("Le joueur accusé est un voleur ! Vous récupérez "+secret.getDiamondsTaken()+" diamants.\n", "", 4);
			a.setContent("Le joueur accusé est un voleur ! Vous récupérez "+secret.getDiamondsTaken()+" diamants.");
			//update the number of diamonds taken back in display
			App.gv.displayUpdatedInfo(this.getDiamondsTakenBack(),-1) ;
			Talk t = new Talk(q,a);
			gameHistory.add(t);
			App.gv.displayGameHistory();
			if(hasGodFatherWon()){
				//find godFather, loyalHenchmen and all their drivers
				playersInfo.addWinner(playersInfo.getGodFather());
				playersInfo.addWinners(playersInfo.getLoyalHenchmen());
				playersInfo.addWinners(this.getWinningDrivers());
				playersInfo.setWinningSide(PlayersInfo.GODFATHER);
				//display end banner
				App.gv.displayEndBanner(playersInfo); 
				playersInfo = new PlayersInfo();
			}

			this.currentTurn += 1;
			SelectingGodFathersAction();
		}else{	
			if(((GodFather)players.get(1).getRole()).consumeJoker()){
				//display one less joker and everyone knows who is the target
				App.gv.displayUpdatedInfo(-1, ((GodFather)players.get(1).getRole()).getJokersLeft());
				createPopUp("Le joueur accusé est un "+secret.getRole()+" ! Vous perdez un joker.\n", "", 4);
				for(PlayerController pc: playerControllers.values()){
					if(pc instanceof AIController){
					((AIController)pc).updateWorldsVision(secret);
					}
				}
				a.setContent("Le joueur accusé est un "+secret.getRole()+" ! Vous perdez un joker.");
				Talk t = new Talk(q,a);
				gameHistory.add(t);
				App.gv.displayGameHistory();
				this.currentTurn += 1;
				SelectingGodFathersAction();
			}else{
				createPopUp("Le joueur accusé est un "+secret.getRole()+" ! Vous avez perdu...\n", "", 4);
				a.setContent("Le joueur accusé est un "+secret.getRole()+" ! Vous avez perdu...");
				//find the best thief
				Player bestThief = playersInfo.getThieves().get(0);
				for(Player player: playersInfo.getThieves()){
					if (player.getRole().getNbDiamondsStolen() > bestThief.getRole().getNbDiamondsStolen()){
						bestThief = player;
					}
				}
				playersInfo.addWinner(bestThief);
				playersInfo.addWinners(playersInfo.getStreetUrchin());
				playersInfo.addWinners(this.getWinningDrivers());
				playersInfo.setWinningSide(PlayersInfo.THIEVES);
				Talk t = new Talk(q,a);
				gameHistory.add(t);
				App.gv.displayGameHistory();
				App.gv.displayEndBanner(playersInfo);
				playersInfo = new PlayersInfo();
			}
		}
	}
	
	
	public void getAnswerToQuestion(Question question, Answer answer){
		try {
			reachTimer();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Platform.runLater(()->
			createPopUp("Le joueur " + question.getTargetPlayer() + " répond :\n" + answer.getContent(), "Action en cours", 5));
		Talk talk = new Talk(question, answer);
		this.gameHistory.add(talk);
		Platform.runLater(() -> App.gv.displayGameHistory());
		updateAIWorldsVisions();
		this.currentTurn += 1;
		SelectingGodFathersAction();
	}
	
	
	public void updateAIWorldsVisions(){
		for(PlayerController pc: playerControllers.values()){
			if(pc instanceof AIController){
				((AIController)pc).updateWorldsVision(this.gameHistory.get(gameHistory.size()-1));
			}
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
					playerControllers.put(position, new AIGodFatherController(player));
				}else{
					playerControllers.put(position, new AISuspectController(player));
					//set position strategy
					if(position==2){
						((AISuspectController)playerControllers.get(position)).setPosStrategy(new FirstPositionStrategy());
					}
					else if(position == 3){
						((AISuspectController)playerControllers.get(position)).setPosStrategy(new SecondPositionStrategy());
					} 
					else if(position== App.rules.getCurrentNumberOfPlayer()){
						((AISuspectController)playerControllers.get(position)).setPosStrategy(new LastPositionStrategy());
					}
					else {
						((AISuspectController)playerControllers.get(position)).setPosStrategy(new MiddlePositionStrategy());
					}
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

				if(player.getRole().getName().equals(App.rules.getNameGodFather())){
					((AIGodFatherController) playerControllers.get(position)).addStrategy(new GodFatherStrategy());
				}
				if(player.getRole().getName().equals(App.rules.getNameLoyalHenchman())){
					((AISuspectController) playerControllers.get(position)).addStrategy(new LoyalHenchmanStrategy());
				}
				if(player.getRole().getName().equals(App.rules.getNameCleaner())){
					((AISuspectController) playerControllers.get(position)).addStrategy(new LoyalHenchmanStrategy());
				}
				if(player.getRole().getName().equals(App.rules.getNameDriver())){
					((AISuspectController) playerControllers.get(position)).addStrategy(new LoyalHenchmanStrategy());
				}
				if(player.getRole().getName().equals(App.rules.getNameThief())){
					((AISuspectController) playerControllers.get(position)).addStrategy(new LoyalHenchmanStrategy());
					((AISuspectController) playerControllers.get(position)).generateLie();
				}
				if(player.getRole().getName().equals(App.rules.getNameStreetUrchin())){
					((AISuspectController) playerControllers.get(position)).addStrategy(new LoyalHenchmanStrategy());
				}
				if(player.getRole().getName().equals(App.rules.getNameAgentCIA()) | player.getRole().getName().equals(App.rules.getNameAgentFBI()) | player.getRole().getName().equals(App.rules.getNameAgentLambda())){
					((AISuspectController) playerControllers.get(position)).addStrategy(new LoyalHenchmanStrategy());
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
		try {
			this.updateRules();
		} catch (RoleError e) {
			e.printStackTrace();
		}
		this.updateControllers();
		//dégriser le bouton inspect et récupérer tous les inspect des controlleurs IA
		App.gv.enableInspectView();
		ArrayList<Inspect> inspects = new ArrayList<>();
		for(PlayerController pc: this.playerControllers.values()){
			if(pc instanceof AIController){
				inspects.add(((AIController) pc).getInspect());
			}
		}
		App.iv = new InspectView(600, 600, inspects);
		
		//Suite des opérations
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
		//Stop le thread qui serait en plein calcul
		if(this.mainThread.isAlive()){
			this.mainThread.interrupt();
		}
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
	
	
	public void startTimer(long period){
		if(!hasTimerStarted){
			hasTimerStarted = true;
		}
		this.duration = period;
		startTimer = System.currentTimeMillis();		
	}
	
	public void reachTimer() throws InterruptedException{
		if(hasTimerStarted){
			long interval = System.currentTimeMillis() - startTimer;
			if(interval < duration){
				Thread.sleep(interval);
			}
			hasTimerStarted = false;
		}
	}
	
	/**
	 * 
	 * @return true si le jeu respecte les règles de base false si les paramètres sont personnalisés
	 */
	public boolean isGameStandard(){
		return App.rules.isGameStandard();
	}
	
	
	/**
	 * create a pop up 
	 * @param text : pop up text
	 * @param title : pop up title
	 * @param time : time the pop up is shown
	 */
	public void createPopUp(String text, String title, int time){
		Notifications.create()
    	.title(title)
    	.text(text)
    	.position(Pos.CENTER)
    	.owner(App.mainStage)
    	.hideAfter(Duration.seconds(time))
    	.showInformation();
	}
}
