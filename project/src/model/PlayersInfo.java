package model;

import java.util.ArrayList;

public class PlayersInfo {
	private Player godFather;
	private ArrayList<Player> loyalHenchmen;
	private ArrayList<Player> cleaners;
	private ArrayList<Player> thieves;
	private ArrayList<Player> streetUrchin;
	private ArrayList<Player> drivers;
	private ArrayList<Player> agents;
	private Player fbi;
	private Player cia;
	
	private ArrayList<Player> winners;
	
	private String winningSide;
	
	
	public static final String AGENT = "agent";
	public static final String FBI = "FBI";
	public static final String CIA = "CIA";
	public static final String GODFATHER = "godFather";
	public static final String CLEANER = "cleaner";
	public static final String THIEVES = "thieves";
	
	public PlayersInfo(){
		super();
		loyalHenchmen = new ArrayList<>();
		cleaners = new ArrayList<>();
		thieves = new ArrayList<>();
		streetUrchin = new ArrayList<>();
		drivers = new ArrayList<>();
		agents = new ArrayList<>();
		
		winners = new ArrayList<>();
	}


	public Player getGodFather() {
		return godFather;
	}


	public void setGodFather(Player godFather) {
		this.godFather = godFather;
	}


	public ArrayList<Player> getLoyalHenchmen() {
		return loyalHenchmen;
	}


	public void setLoyalHenchmen(ArrayList<Player> loyalHenchmen) {
		this.loyalHenchmen = loyalHenchmen;
	}
	
	public void addLoyalHenchman(Player player){
		this.loyalHenchmen.add(player);
	}

	public ArrayList<Player> getCleaners() {
		return cleaners;
	}


	public void setCleaners(ArrayList<Player> cleaners) {
		this.cleaners = cleaners;
	}

	
	public void addCleaner(Player player){
		this.cleaners.add(player);
	}

	public ArrayList<Player> getThieves() {
		return thieves;
	}


	public void setThieves(ArrayList<Player> thieves) {
		this.thieves = thieves;
	}

	
	public void addThief(Player player){
		this.thieves.add(player);
	}

	public ArrayList<Player> getStreetUrchin() {
		return streetUrchin;
	}


	public void setStreetUrchin(ArrayList<Player> streetUrchin) {
		this.streetUrchin = streetUrchin;
	}
	
	public void addStreetUrchin(Player player){
		this.streetUrchin.add(player);
	}


	public ArrayList<Player> getDrivers() {
		return drivers;
	}


	public void setDrivers(ArrayList<Player> drivers) {
		this.drivers = drivers;
	}
	
	public void addDriver(Player player){
		this.drivers.add(player);
	}


	public ArrayList<Player> getAgents() {
		return agents;
	}


	public void setAgents(ArrayList<Player> agents) {
		this.agents = agents;
	}
	
	public void addAgent(Player player){
		this.agents.add(player);
	}


	public Player getFBI() {
		return fbi;
	}


	public void setFBI(Player fbi) {
		this.fbi = fbi;
	}

	
	public void addFBI(Player player){
		this.fbi = player;
	}

	public Player getCIA() {
		return cia;
	}


	public void setCIA(Player cia) {
		this.cia = cia;
	}

	
	public void addCIA(Player player){
		this.cia = player;
	}

	public ArrayList<Player> getWinners() {
		return winners;
	}


	public void setWinners(ArrayList<Player> winners) {
		this.winners = winners;
	}
	
	public void addWinner(Player player){
		this.winners.add(player);
	}

	public void addWinners(ArrayList<Player> players){
		this.winners.addAll(players);
	}

	public String getWinningSide() {
		return winningSide;
	}


	public void setWinningSide(String winningSide) {
		this.winningSide = winningSide;
	}

	
	
	
}
