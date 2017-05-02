package data;

import java.util.ArrayList;

public class World {
	private ArrayList<Integer> roleDistribution;
	private Integer tokenMovedAside;
	
	public World(Integer tokenMovedAside, ArrayList<Integer> roleDistribution){
		this.roleDistribution = roleDistribution;
		this.tokenMovedAside = tokenMovedAside;
	}

	public World(World w){
		this.roleDistribution = (ArrayList<Integer>) w.getRoleDistribution().clone();
		this.tokenMovedAside = w.getTokenMovedAside();		
	}
	
	public World(){
		this.roleDistribution = new ArrayList<Integer>();
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
		if(roleDistribution == null){
			if(other.getRoleDistribution() != null){
				return false;
			}
		}
		if(roleDistribution.size() != other.getRoleDistribution().size()){
			return false;
		}else{
			for(int i = 0 ; i < roleDistribution.size() ; i++){
				if(roleDistribution.get(i).intValue() != other.getRoleDistribution().get(i).intValue()){
					return false;
				}
			}
		}
		return true;
		
	}
	public ArrayList<Integer> getRoleDistribution() {
		return roleDistribution;
	}

	public void setRoleDistribution(ArrayList<Integer> roleDistribution) {
		this.roleDistribution = roleDistribution;
	}

	public Integer getTokenMovedAside() {
		return tokenMovedAside;
	}

	public void setTokenMovedAside(Integer tokenMovedAside) {
		this.tokenMovedAside = tokenMovedAside;
	}
	
	
}
