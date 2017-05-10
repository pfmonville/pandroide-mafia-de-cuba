package model;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.beans.property.SimpleStringProperty;

public class Inspect {

	private Integer id;
	private HashMap<Integer, InspectView> inspects = new HashMap<>();
	
	public Inspect(int id) {
		this.id = id;
	}
	
	
	public void addAssumedRoleForPlayer(int id, Double loyalHenchman, Double cleaner, Double agent, Double thief, 
			Double streetUrchin, Double driver){
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
	
	
	
	public class InspectView{
		private final SimpleStringProperty id;
		private final SimpleStringProperty loyalHenchman;
		private final SimpleStringProperty cleaner;
		private final SimpleStringProperty agent;
		private final SimpleStringProperty thief;
		private final SimpleStringProperty streetUrchin;
		private final SimpleStringProperty driver;

		public InspectView(SimpleStringProperty id, SimpleStringProperty loyalHenchman, SimpleStringProperty cleaner,
				SimpleStringProperty agent, SimpleStringProperty thief, SimpleStringProperty streetUrchin,
				SimpleStringProperty driver) {
			super();
			this.id = id;
			this.loyalHenchman = loyalHenchman;
			this.cleaner = cleaner;
			this.agent = agent;
			this.thief = thief;
			this.streetUrchin = streetUrchin;
			this.driver = driver;
		}

		public SimpleStringProperty getId() {
			return id;
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
			this.id.set(String.valueOf(value));
		}
		
		public void setLoyalHenchman(Double value){
			this.loyalHenchman.set(value.toString());
		}
		
		public void setCleaner(Double value){
			this.cleaner.set(value.toString());
		}
		
		public void setAgent(Double value){
			this.agent.set(value.toString());
		}
		
		public void setThief(Double value){
			this.thief.set(value.toString());
		}
		
		public void setStreetUrchin(Double value){
			this.streetUrchin.set(value.toString());
		}
		
		public void setDriver(Double value){
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
		
	}
	
	
	
}
