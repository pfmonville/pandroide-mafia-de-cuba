package model;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Rules {
	
	private int maxHiddenDiamonds;
	private int maxHiddenTokens;
	private boolean lastPlayerMustTake;
	private boolean firstPlayerCanHide;
	private int minimumNumberOfPlayer;
	private int maximumNumberOfPlayer;
	
	//contain the standard number of tokens
	private List<Integer> defaultNumberOfLoyalHenchmen;
	private List<Integer> defaultNumberOfCleaners;
	private List<Integer> defaultNumberOfAgents;
	private List<Integer> defaultNumberOfDrivers;
	private List<Integer> defaultNumberOfJoker;
	
	
	//String declarations
	private String nameGodFather;
	private String nameLoyalHenchman;
	private String nameCleaner;
	private String nameAgentFBI;
	private String nameAgentCIA;
	private String nameAgentLambda;
	private String nameDriver;
	private String nameThief;
	private String nameStreetUrchin;
	
	// Corresponding value of each role
	private final Integer numberGodfather = 0;
	private final Integer numberLoyalHenchman = 1;
	private final Integer numberCleaner = 6;
	private final Integer numberAgent = 3;
	private final Integer numberDriver = 2;
	private final Integer numberThief = 4;
	private final Integer numberStreetUrchin = 5;
	
	
	//Value for the game initialize in the OptionView
	private int numberOfLoyalHenchmen;
	private int numberOfCleaners;
	private int numberOfAgents;
	private int numberOfDrivers;
	private int numberOfJokers;
	private int numberOfDiamonds;
	private boolean allIA;
	private int humanPosition;
	private int currentNumberOfPlayer;

	
	public Rules() {
		this.nameGodFather = "GodFather";
		this.nameLoyalHenchman = "LoyalHenchman";
		this.nameCleaner = "Cleaner";
		this.nameAgentFBI = "FBI";
		this.nameAgentCIA = "CIA";
		this.nameAgentLambda = "Agent";
		this.nameDriver = "Driver";
		this.nameThief= "Thief";
		this.nameStreetUrchin = "StreetUrchin";
		
		this.defaultNumberOfLoyalHenchmen = Arrays.asList(1,2,3,4,4,4,5);
		this.defaultNumberOfCleaners = Arrays.asList(0,0,0,0,0,0,0);
		this.defaultNumberOfAgents = Arrays.asList(1,1,1,1,2,2,2);
		this.defaultNumberOfDrivers = Arrays.asList(1,1,1,1,1,2,2);
		this.defaultNumberOfJoker = Arrays.asList(0,0,1,1,1,2,2);
		
		
		this.maxHiddenDiamonds = 5;
		this.maxHiddenTokens = 1;
		this.lastPlayerMustTake = false;
		this.firstPlayerCanHide = true;
		this.minimumNumberOfPlayer = 6;
		this.maximumNumberOfPlayer = 12;
		this.currentNumberOfPlayer = 6;
		
		this.numberOfLoyalHenchmen = 1;
		this.numberOfCleaners = 0;
		this.numberOfAgents = 1;
		this.numberOfDrivers = 1;
		this.numberOfJokers = 0;
		this.numberOfDiamonds = 15;
		this.allIA = false;
		this.humanPosition = 1;
	}
	
	
	/**
	 * this method resets the rule set for a new game
	 */
	public void reset(){
		this.maxHiddenDiamonds = 5;
		this.maxHiddenTokens = 1;
		this.lastPlayerMustTake = false;
		this.firstPlayerCanHide = true;
		this.minimumNumberOfPlayer = 6;
		this.maximumNumberOfPlayer = 12;
		
		this.numberOfLoyalHenchmen = 1;
		this.numberOfCleaners = 0;
		this.numberOfAgents = 1;
		this.numberOfDrivers = 1;
		this.numberOfJokers = 0;
		this.numberOfDiamonds = 15;
		this.allIA = false;
	}
	
	public ArrayList<String> getTokensFor(int numberOfPlayer){
		
		ArrayList<String> tokens = new ArrayList<>();
		for(int i = 0; i < this.defaultNumberOfLoyalHenchmen.get(numberOfPlayer - minimumNumberOfPlayer); i++){
			tokens.add(nameLoyalHenchman);
		}
		for(int i = 0; i < this.defaultNumberOfCleaners.get(numberOfPlayer - minimumNumberOfPlayer); i++){
			tokens.add(nameCleaner);
		}
		for(int i = 0; i < this.defaultNumberOfAgents.get(numberOfPlayer - minimumNumberOfPlayer); i++){
			if(i ==0){
				tokens.add(nameAgentFBI);
			}else if(i == 1){
				tokens.add(nameAgentCIA);
			}else{
				tokens.add(nameAgentLambda);
			}
		}
		for(int i = 0; i < this.defaultNumberOfDrivers.get(numberOfPlayer - minimumNumberOfPlayer); i++){
			tokens.add(nameDriver);
		}
		return tokens;
	}


	public boolean isAValidToken(String token){
		if(token.equals(nameDriver)||token.equals(nameLoyalHenchman)||token.equals(nameAgentFBI)|| token.equals(nameAgentCIA)|| token.equals(nameAgentLambda)){
			return true;
		}else if(this.numberOfCleaners > 0 && token.equals(nameCleaner)){
			return true;
		}
		return false;
	}
	
	public ArrayList<String> getTokens(){
		ArrayList<String> tokens = new ArrayList<>();
		for(int i = 0; i < this.numberOfLoyalHenchmen; i++){
			tokens.add(nameLoyalHenchman);
		}
		for(int i = 0; i < this.numberOfCleaners; i++){
			tokens.add(nameCleaner);
		}
		for(int i = 0; i < this.numberOfAgents; i++){
			if(i ==0){
				tokens.add(nameAgentFBI);
			}else if(i == 1){
				tokens.add(nameAgentCIA);
			}else{
				tokens.add(nameAgentLambda);
			}
		}
		for(int i = 0; i < this.numberOfDrivers; i++){
			tokens.add(nameDriver);
		}
		return tokens;
	}
	
	public Box getBox(){
		return new Box(this.numberOfDiamonds, this.getTokens());
	}
	
	public int getDefaultNumberOfLoyalHenchmenFor(int numberOfPlayer) {
		return defaultNumberOfLoyalHenchmen.get(numberOfPlayer - minimumNumberOfPlayer);
	}



	public int getDefaultNumberOfCleanersFor(int numberOfPlayer) {
		return defaultNumberOfCleaners.get(numberOfPlayer - minimumNumberOfPlayer);
	}



	public int getDefaultNumberOfAgentsFor(int numberOfPlayer) {
		return defaultNumberOfAgents.get(numberOfPlayer - minimumNumberOfPlayer);
	}



	public int getDefaultNumberOfDriversFor(int numberOfPlayer) {
		return defaultNumberOfDrivers.get(numberOfPlayer - minimumNumberOfPlayer);
	}



	public int getDefaultNumberOfJokerFor(int numberOfPlayer) {
		return defaultNumberOfJoker.get(numberOfPlayer - minimumNumberOfPlayer);
	}



	public int getMaxHiddenDiamonds() {
		return maxHiddenDiamonds;
	}



	public void setMaxHiddenDiamonds(int maxHiddenDiamonds) {
		this.maxHiddenDiamonds = maxHiddenDiamonds;
	}



	public int getMaxHiddenTokens() {
		return maxHiddenTokens;
	}



	public void setMaxHiddenTokens(int maxHiddenTokens) {
		this.maxHiddenTokens = maxHiddenTokens;
	}



	public boolean isLastPlayerMustTake() {
		return lastPlayerMustTake;
	}



	public void setLastPlayerMustTake(boolean lastPlayerMustTake) {
		this.lastPlayerMustTake = lastPlayerMustTake;
	}



	public boolean isFirstPlayerCanHide() {
		return firstPlayerCanHide;
	}



	public void setFirstPlayerCanHide(boolean firstPlayerCanHide) {
		this.firstPlayerCanHide = firstPlayerCanHide;
	}



	public int getMinimumNumberOfPlayer() {
		return minimumNumberOfPlayer;
	}



	public void setMinimumNumberOfPlayer(int minimumNumberOfPlayer) {
		this.minimumNumberOfPlayer = minimumNumberOfPlayer;
	}


	public int getMaximumNumberOfPlayer() {
		return maximumNumberOfPlayer;
	}


	public void setMaximumNumberOfPlayer(int maximumNumberOfPlayer) {
		this.maximumNumberOfPlayer = maximumNumberOfPlayer;
	}


	public List<Integer> getDefaultNumberOfLoyalHenchmen() {
		return defaultNumberOfLoyalHenchmen;
	}



	public void setDefaultNumberOfLoyalHenchmen(List<Integer> defaultNumberOfLoyalHenchmen) {
		this.defaultNumberOfLoyalHenchmen = defaultNumberOfLoyalHenchmen;
	}



	public List<Integer> getDefaultNumberOfCleaners() {
		return defaultNumberOfCleaners;
	}



	public void setDefaultNumberOfCleaners(List<Integer> defaultNumberOfCleaners) {
		this.defaultNumberOfCleaners = defaultNumberOfCleaners;
	}



	public List<Integer> getDefaultNumberOfAgents() {
		return defaultNumberOfAgents;
	}



	public void setDefaultNumberOfAgents(List<Integer> defaultNumberOfAgents) {
		this.defaultNumberOfAgents = defaultNumberOfAgents;
	}



	public List<Integer> getDefaultNumberOfDrivers() {
		return defaultNumberOfDrivers;
	}



	public void setDefaultNumberOfDrivers(List<Integer> defaultNumberOfDrivers) {
		this.defaultNumberOfDrivers = defaultNumberOfDrivers;
	}



	public List<Integer> getDefaultNumberOfJoker() {
		return defaultNumberOfJoker;
	}



	public void setDefaultNumberOfJoker(List<Integer> defaultNumberOfJoker) {
		this.defaultNumberOfJoker = defaultNumberOfJoker;
	}



	public String getNameGodFather() {
		return nameGodFather;
	}



	public void setNameGodFather(String nameGodFather) {
		this.nameGodFather = nameGodFather;
	}



	public String getNameLoyalHenchman() {
		return nameLoyalHenchman;
	}



	public void setNameLoyalHenchman(String nameLoyalHenchman) {
		this.nameLoyalHenchman = nameLoyalHenchman;
	}



	public String getNameCleaner() {
		return nameCleaner;
	}



	public void setNameCleaner(String nameCleaner) {
		this.nameCleaner = nameCleaner;
	}



	public String getNameAgentLambda() {
		return nameAgentLambda;
	} 


	public void setNameAgentLambda(String nameAgent) {
		this.nameAgentLambda = nameAgent;
	}
	
	public String getNameAgentFBI() {
		return nameAgentFBI;
	} 


	public void setNameAgentFBI(String nameAgent) {
		this.nameAgentFBI = nameAgent;
	}
	
	public String getNameAgentCIA() {
		return nameAgentCIA;
	} 


	public void setNameAgentCIA(String nameAgent) {
		this.nameAgentCIA = nameAgent;
	}



	public String getNameDriver() {
		return nameDriver;
	}



	public void setNameDriver(String nameDriver) {
		this.nameDriver = nameDriver;
	}



	public String getNameThief() {
		return nameThief;
	}



	public void setNameThief(String nameThief) {
		this.nameThief = nameThief;
	}



	public String getNameStreetUrchin() {
		return nameStreetUrchin;
	}



	public void setNameStreetUrchin(String nameStreetUrchin) {
		this.nameStreetUrchin = nameStreetUrchin;
	}



	public int getNumberOfLoyalHenchmen() {
		return numberOfLoyalHenchmen;
	}



	public void setNumberOfLoyalHenchmen(int numberOfLoyalHenchmen) {
		this.numberOfLoyalHenchmen = numberOfLoyalHenchmen;
	}


	public void setNumberOfLoyalHenchmenFor(int numberOfPlayer) {
		this.numberOfLoyalHenchmen = this.getDefaultNumberOfLoyalHenchmenFor(numberOfPlayer);
	}
	

	public int getNumberOfCleaners() {
		return numberOfCleaners;
	}



	public void setNumberOfCleaners(int numberOfCleaners) {
		this.numberOfCleaners = numberOfCleaners;
	}

	public void setNumberOfCleanersFor(int numberOfPlayer) {
		this.numberOfCleaners = this.getDefaultNumberOfCleanersFor(numberOfPlayer);
	}


	public int getNumberOfAgents() {
		return numberOfAgents;
	}



	public void setNumberOfAgents(int numberOfAgents) {
		this.numberOfAgents = numberOfAgents;
	}

	public void setNumberOfAgentsFor(int numberOfPlayer) {
		this.numberOfAgents = this.getDefaultNumberOfAgentsFor(numberOfPlayer);
	}
	

	public int getNumberOfDrivers() {
		return numberOfDrivers;
	}



	public void setNumberOfDrivers(int numberOfDrivers) {
		this.numberOfDrivers = numberOfDrivers;
	}


	public void setNumberOfDriversFor(int numberOfPlayer) {
		this.numberOfDrivers = this.getDefaultNumberOfDriversFor(numberOfPlayer);
	}
	

	public int getNumberOfJokers() {
		return numberOfJokers;
	}



	public void setNumberOfJokers(int numberOfJoker) {
		this.numberOfJokers = numberOfJoker;
	}
	
	public void setNumberOfJokersFor(int numberOfPlayer) {
		this.numberOfJokers = this.getDefaultNumberOfJokerFor(numberOfPlayer);
	}



	public int getNumberOfDiamonds() {
		return numberOfDiamonds;
	}



	public void setNumberOfDiamonds(int numberOfDiamonds) {
		this.numberOfDiamonds = numberOfDiamonds;
	}



	public boolean isAllIA() {
		return allIA;
	}



	public void setAllIA(boolean allIA) {
		this.allIA = allIA;
	}



	public int getHumanPosition() {
		return humanPosition;
	}


	public void setHumanPosition(int humanPosition) {
		this.humanPosition = humanPosition;
	}
	
	public boolean getLastPlayerMustTake(){
		return lastPlayerMustTake ;
	}
	
	public void setLastPlayerMustTke(boolean b){
		lastPlayerMustTake = b ;
	}
	
	public boolean getFirstPlayerCanHide(){
		return firstPlayerCanHide ;
	}

	public Integer getNumberGodfather() {
		return numberGodfather;
	}


	public Integer getNumberLoyalHenchman() {
		return numberLoyalHenchman;
	}


	public Integer getNumberCleaner() {
		return numberCleaner;
	}


	public Integer getNumberAgent() {
		return numberAgent;
	}


	public Integer getNumberDriver() {
		return numberDriver;
	}


	public Integer getNumberThief() {
		return numberThief;
	}


	public Integer getNumberStreetUrchin() {
		return numberStreetUrchin;
	}

	public int getCurrentNumberOfPlayer() {
		return currentNumberOfPlayer;
	}


	public void setCurrentNumberOfPlayer(int currentNumberOfPlayer) {
		this.currentNumberOfPlayer = currentNumberOfPlayer;
	}
	

	/**
	 * Questions' instanciation
	 * @return a list of questions
	 */
	public ArrayList<Question> getQuestions(){
		ArrayList<Question> questList = new ArrayList<>();
		//lire le fichier des questions 
		BufferedReader file=null;
		String line ;
		
		try {
			file = new BufferedReader(new FileReader(new File(Theme.pathToQuestions)));
			// on saute les lignes d'introduction
			do {
				line = file.readLine();
			}while(!line.contains("*"));
			
			line = file.readLine();
			while(line != null){
				//identifier les différents éléments de la ligne
				String[] elem = line.split(":");
				int index = Integer.parseInt(elem[0].trim());
				int category = Integer.parseInt(elem[2].trim());
				//  le dernier élément est la liste des ids des réponses
				String[] idsTab = elem[3].trim().split(",");
				ArrayList<Integer> ids = new ArrayList<Integer>();
				for(int i=0 ; i<idsTab.length;i++){
					ids.add(new Integer(Integer.parseInt(idsTab[i])));
				}
				
				if(elem.length == 4)
					questList.add(new Question(index,elem[1].trim(),ids,category));
				else 
					questList.add(new Question(index, elem[1].trim(), ids, category, Integer.parseInt(elem[4].trim())));
				
				line = file.readLine();
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				file.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return questList;
	}
	
	
	/**
	 * Answers' instanciation
	 * @return a list of Answer
	 */
	public ArrayList<Answer> getAnswers(){
		ArrayList<Answer> answerList = new ArrayList<Answer>();
		//lire le fichier des réponses
				BufferedReader file=null;
				String line ;
				
				try {
					file = new BufferedReader(new FileReader(new File(Theme.pathToAnswers)));
					// on saute les lignes d'introduction
					do {
						line = file.readLine();
					}while(!line.contains("*"));
					
					line = file.readLine();
					while(line != null){
						//identifier les différents éléments de la ligne
						String[] elem = line.split(":");
						int index = Integer.parseInt(elem[0].trim());
						//  le dernier élément est la liste des ids des questions
						String[] idsTab = elem[elem.length-1].trim().split(",");
						ArrayList<Integer> ids = new ArrayList<Integer>();
						for(int i=0 ; i<idsTab.length;i++){
							ids.add(new Integer(Integer.parseInt(idsTab[i])));
						}
						
						answerList.add(new Answer(index,elem[1].trim(),ids));
						
						line = file.readLine();
					}
					
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally{
					try {
						file.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
		return answerList;
	}
	
	public Integer convertRoleNameIntoNumber(String role){

		if(role.equals(nameAgentLambda) || role.equals(nameAgentCIA) || role.equals(nameAgentFBI)){
			return numberAgent;
		}
		else if(role.equals(nameLoyalHenchman)){
			return numberLoyalHenchman;
		}
		else if(role.equals(nameCleaner)){
			return numberCleaner;
		}
		else if(role.equals(nameDriver)){
			return numberDriver;
		}
		else if(role.equals(nameStreetUrchin)){
			return numberStreetUrchin;
		}
		else if(role.equals(nameThief)){
			return numberThief;
		}
		else if(role.equals(nameGodFather)){
			return numberGodfather;
		}
		
		return null;
	}
	
	public String convertNumberIntoRoleName(Integer number){
		
		if(number == numberAgent){
			return nameAgentLambda;
		}
		else if(number == numberCleaner){
			return nameCleaner;
		}
		else if(number == numberDriver){
			return nameDriver;
		}
		else if(number == numberGodfather){
			return nameGodFather;
		}
		else if(number == numberLoyalHenchman){
			return nameLoyalHenchman;
		}
		else if(number == numberStreetUrchin){
			return nameStreetUrchin;
		}
		else if(number == numberThief){
			return nameThief;
		}
		return null;
	}
}
