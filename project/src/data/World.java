package data;

import java.util.ArrayList;

public class World {
	private ArrayList<Integer> roleDistribution;
	private Integer tokenMovedAside;
	
	public World(ArrayList<Integer> roleDistribution, Integer tokenMovedAside){
		this.roleDistribution = roleDistribution;
		this.tokenMovedAside = tokenMovedAside;
	}

	public ArrayList<Integer> getRoleDistribution() {
		return roleDistribution;
	}

	public void setRoleDistribution(ArrayList<Integer> roleDistribution) {
		this.roleDistribution = roleDistribution;
	}

	public Integer isTokenMovedAside() {
		return tokenMovedAside;
	}

	public void setTokenMovedAside(Integer tokenMovedAside) {
		this.tokenMovedAside = tokenMovedAside;
	}
	
	
}
