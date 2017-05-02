package data;

import java.util.ArrayList;

public class World {
	private ArrayList<Integer> rolesDistribution;
	private Integer tokenMovedAside;
	
	public World(Integer tokenMovedAside, ArrayList<Integer> roleDistribution){
		this.rolesDistribution = roleDistribution;
		this.tokenMovedAside = tokenMovedAside;
	}

	public World(World w){
		this.rolesDistribution = (ArrayList<Integer>) w.getRolesDistribution().clone();
		this.tokenMovedAside = w.getTokenMovedAside();		
	}
	
	public World(){
		this.rolesDistribution = new ArrayList<Integer>();
		this.tokenMovedAside = -1;
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
	
	
}
