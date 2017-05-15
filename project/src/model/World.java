package model;

import java.util.ArrayList;
import java.util.HashMap;

public class World {
	private ArrayList<Integer> rolesDistribution;
	private Integer tokenMovedAside;
	
	//cle: position du joeur
	// valeur appartient {-1, 0, 1}, indique si ce monde est vrai d'apres la reponse de ce joueur
	private HashMap<Integer, Integer> truthValue; 
	
	public World(Integer tokenMovedAside, ArrayList<Integer> roleDistribution, HashMap<Integer, Integer> truthValue){
		this.rolesDistribution = roleDistribution;
		this.tokenMovedAside = tokenMovedAside;
		this.truthValue = truthValue;
	}
	
	public World(){
		this.rolesDistribution = new ArrayList<Integer>();
		this.tokenMovedAside = -1;
		this.truthValue = new HashMap<Integer, Integer>();
	}

	public boolean equals(Object obj){
		if(this == obj){
			return true;
		}
		if(obj == null){
			return false;
		}
		if(!(obj instanceof World)){
			return false;
		}
		World other = (World) obj;
		if(tokenMovedAside == null){
			if(other.getTokenMovedAside() != null){
				return false;
			}
		}else if(other.getTokenMovedAside().intValue() != this.getTokenMovedAside().intValue()){
			return false;
		}
		if(rolesDistribution == null){
			if(other.getRolesDistribution() != null){
				return false;
			}
		}
		if(rolesDistribution.size() != other.getRolesDistribution().size()){
			return false;
		}else{
			for(int i = 0 ; i < rolesDistribution.size() ; i++){
				if(rolesDistribution.get(i).intValue() != other.getRolesDistribution().get(i).intValue()){
					return false;
				}
			}
		}
		return true;
		
	}
	
	public World clone(){
		ArrayList<Integer> clonedRolesDistribution = new ArrayList<Integer>();
		HashMap<Integer, Integer> clonedTruthValue = new HashMap<Integer, Integer>(truthValue);
		for (Integer i : rolesDistribution){
			clonedRolesDistribution.add(new Integer(i.intValue()));
		}
		return new World(tokenMovedAside, clonedRolesDistribution, clonedTruthValue);
	}
	
	public ArrayList<Integer> getRolesDistribution() {
		return rolesDistribution;
	}

	public void setRoleDistribution(ArrayList<Integer> roleDistribution) {
		this.rolesDistribution = roleDistribution;
	}

	public Integer getTokenMovedAside() {
		return tokenMovedAside;
	}

	public void setTokenMovedAside(Integer tokenMovedAside) {
		this.tokenMovedAside = tokenMovedAside;
	}
	
	public HashMap<Integer, Integer> getTruthValue() {
		return truthValue;
	}

	public void setTruthValue(HashMap<Integer, Integer> truthValue) {
		this.truthValue = truthValue;
	}

	public Double getWeight(HashMap<Integer, Double> fiability){
		Double sum = 0.0;
		for(Integer id : truthValue.keySet()){
			sum += truthValue.get(id)*fiability.get(id);
		}
		return sum;
	}
}
