package model;

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
	private String nameAgent;
	private String nameDriver;
	private String nameThief;
	private String nameStreetUpchin;
	
	
	public Rules(){
		this.nameGodFather = "Parrain";
		this.nameLoyalHenchman = "Fidèle";
		this.nameCleaner = "Nettoyeur";
		this.nameAgent = "Agent";
		this.nameDriver = "chauffeur";
		this.nameThief= "Voleur";
		this.nameStreetUpchin = "Enfant des Rues";
		
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
			tokens.add(nameAgent);
		}
		for(int i = 0; i < this.defaultNumberOfDrivers.get(numberOfPlayer - minimumNumberOfPlayer); i++){
			tokens.add(nameDriver);
		}
		return tokens;
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



	public String getNameAgent() {
		return nameAgent;
	}



	public void setNameAgent(String nameAgent) {
		this.nameAgent = nameAgent;
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



	public String getNameStreetUpchin() {
		return nameStreetUpchin;
	}



	public void setNameStreetUpchin(String nameStreetUpchin) {
		this.nameStreetUpchin = nameStreetUpchin;
	}



	public int getMaximumNumberOfPlayer() {
		return maximumNumberOfPlayer;
	}



	public void setMaximumNumberOfPlayer(int maximumNumberOfPlayer) {
		this.maximumNumberOfPlayer = maximumNumberOfPlayer;
	}


	
	
	
	
	
}
