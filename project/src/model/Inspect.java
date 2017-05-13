package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.Map.Entry;

import controller.App;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Inspect {

	private Integer id;
	private HashMap<Integer, InspectView> inspects = new HashMap<>();
	
	public Inspect(int id) {
		this.id = id;
		for(int position:App.gameController.getAllPlayersPosition()){
			if(position != id && position != 1){
				inspects.put(position, new InspectView(new SimpleStringProperty(String.valueOf(id)), new SimpleStringProperty(), 
						new SimpleStringProperty(), new SimpleStringProperty(), 
						new SimpleStringProperty(), new SimpleStringProperty(), new SimpleStringProperty()));
			}
		}
	}
	
	
	public void addAssumedRoleForPlayer(int id, Double loyalHenchman, Double cleaner, Double agent, Double thief, 
			Double streetUrchin, Double driver){
		//probleme là deux fois id à regarder
		this.inspects.put(id, new InspectView(new SimpleStringProperty(String.valueOf(id)), new SimpleStringProperty(loyalHenchman.toString()), 
				new SimpleStringProperty(cleaner.toString()), new SimpleStringProperty(agent.toString()),
				new SimpleStringProperty(thief.toString()), new SimpleStringProperty(streetUrchin.toString()), 
				new SimpleStringProperty(driver.toString())));
	}

	
	public void updateAssumedRoleForPlayer(int id, Double loyalHenchman, Double cleaner, Double agent, Double thief, 
			Double streetUrchin, Double driver){
		this.inspects.get(id).updateAll(loyalHenchman, cleaner, agent, thief, streetUrchin, driver);
	}
	
	
	public ArrayList<Integer> getAllAIID(){
		ArrayList<Integer> result = new ArrayList<>();
		for(InspectView iv : this.inspects.values()){
			result.add(Integer.parseInt(iv.getId().getValue()));
		}
		return result;
	}
	
	public int getId(){
		return this.id;
	}
	
	public ArrayList<Inspect.InspectView> getAllInspectViews(){
		ArrayList<Inspect.InspectView> inspectViews = new ArrayList<>();
		inspectViews.addAll(inspects.values());
		return inspectViews;
	}
	
	/**
	 * retourne une map<Integer, String> avec pour chaque joueur son rôle tiré au sort selon les probas des rôles le concernant
	 */
	public HashMap<Integer, String> rollDice(){
		HashMap<Integer, String> result = new HashMap<>();
		for(Entry<Integer, InspectView> entry: inspects.entrySet()){
			result.put(entry.getKey(), entry.getValue().rollDice());
		}
		return result;
	}
	
	public InspectView getInspectFor(int positionPlayer){
		for(Entry<Integer, InspectView> entry: inspects.entrySet()){
			if(entry.getKey().equals(positionPlayer)){
				return entry.getValue();
			}
		}
		return null;
	}
	
	
	public class InspectView{
		private final SimpleStringProperty player;
		private final SimpleStringProperty loyalHenchman;
		private final SimpleStringProperty cleaner;
		private final SimpleStringProperty agent;
		private final SimpleStringProperty thief;
		private final SimpleStringProperty streetUrchin;
		private final SimpleStringProperty driver;
		public final StringProperty playerProperty() { return player; }
		public final StringProperty loyalHenchmanProperty() { return loyalHenchman; }
		public final StringProperty cleanerProperty() { return cleaner; }
		public final StringProperty agentProperty() { return agent; }
		public final StringProperty thiefProperty() { return thief; }
		public final StringProperty streetUrchinProperty() { return streetUrchin; }
		public final StringProperty driverProperty() { return driver; }
		
		

		public InspectView(SimpleStringProperty id, SimpleStringProperty loyalHenchman, SimpleStringProperty cleaner,
				SimpleStringProperty agent, SimpleStringProperty thief, SimpleStringProperty streetUrchin,
				SimpleStringProperty driver) {
			super();
			this.player = id;
			this.loyalHenchman = loyalHenchman;
			this.cleaner = cleaner;
			this.agent = agent;
			this.thief = thief;
			this.streetUrchin = streetUrchin;
			this.driver = driver;
		}

		public SimpleStringProperty getId() {
			return player;
		}

		public SimpleStringProperty getLoyalHenchman() {
			return loyalHenchman;
		}

		public SimpleStringProperty getCleaner() {
			return cleaner;
		}

		public SimpleStringProperty getAgent() {
			return agent;
		}

		public SimpleStringProperty getThief() {
			return thief;
		}

		public SimpleStringProperty getStreetUrchin() {
			return streetUrchin;
		}

		public SimpleStringProperty getDriver() {
			return driver;
		}
		
		public void setId(Double value){
			this.player.set(String.valueOf(value));
		}
		
		public void setLoyalHenchman(Double value){
			if(value == null){
				value = 0D;
			}
			this.loyalHenchman.set(value.toString());
		}
		
		public void setCleaner(Double value){
			if(value == null){
				value = 0D;
			}
			this.cleaner.set(value.toString());
		}
		
		public void setAgent(Double value){
			if(value == null){
				value = 0D;
			}
			this.agent.set(value.toString());
		}
		
		public void setThief(Double value){
			if(value == null){
				value = 0D;
			}
			this.thief.set(value.toString());
		}
		
		public void setStreetUrchin(Double value){
			if(value == null){
				value = 0D;
			}
			this.streetUrchin.set(value.toString());
		}
		
		public void setDriver(Double value){
			if(value == null){
				value = 0D;
			}
			this.driver.set(value.toString());
		}
		
		public void updateAll(Double loyalHenchman, Double cleaner, Double agent, Double thief, 
				Double streetUrchin, Double driver){
			this.setLoyalHenchman(loyalHenchman);
			this.setCleaner(cleaner);
			this.setAgent(agent);
			this.setThief(thief);
			this.setStreetUrchin(streetUrchin);
			this.setDriver(driver);
		}
		
		public ArrayList<Double> getAllRolesValue(){
			return (ArrayList<Double>) Arrays.asList(Double.parseDouble(loyalHenchman.getValue()), Double.parseDouble(cleaner.getValue()), 
					Double.parseDouble(agent.getValue()), Double.parseDouble(thief.getValue()), 
					Double.parseDouble(streetUrchin.getValue()), Double.parseDouble(driver.getValue()));
		}
		
		public String rollDice(){
			Double rand = new Random().nextDouble();
			Double acc = 0D;
			Double maxValue = 0D;
			//normalize
			for(Double value: getAllRolesValue()){
				maxValue += value;
			}
			if(maxValue != 1){
				rand = new Random().nextDouble() * maxValue;
			}
			
			if(rand < Double.parseDouble(loyalHenchman.getValue())){
				return App.rules.getNameLoyalHenchman();
			}
			acc += Double.parseDouble(loyalHenchman.getValue());
			if(rand < Double.parseDouble(cleaner.getValue())){
				return App.rules.getNameCleaner();
			}
			acc += Double.parseDouble(cleaner.getValue());
			if(rand < Double.parseDouble(agent.getValue())){
				return App.rules.getNameAgentLambda();
			}
			acc += Double.parseDouble(agent.getValue());
			if(rand < Double.parseDouble(thief.getValue())){
				return App.rules.getNameThief();
			}
			acc += Double.parseDouble(thief.getValue());
			if(rand < Double.parseDouble(streetUrchin.getValue())){
				return App.rules.getNameStreetUrchin();
			}
			return App.rules.getNameDriver();

			
		}
		
		
	}
	
	
	
}
