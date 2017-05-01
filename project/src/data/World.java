package data;

import java.util.ArrayList;

public class World {
	private ArrayList<Integer> roleDistribution;
	private boolean tokenMovedAside;
	
	public World(ArrayList<Integer> roleDistribution, boolean tokenMovedAside){
		this.roleDistribution = roleDistribution;
		this.tokenMovedAside = tokenMovedAside;
	}

	public ArrayList<Integer> getRoleDistribution() {
		return roleDistribution;
	}

	public void setRoleDistribution(ArrayList<Integer> roleDistribution) {
		this.roleDistribution = roleDistribution;
	}

	public boolean isTokenMovedAside() {
		return tokenMovedAside;
	}

	public void setTokenMovedAside(boolean tokenMovedAside) {
		this.tokenMovedAside = tokenMovedAside;
	}
	
	
}
