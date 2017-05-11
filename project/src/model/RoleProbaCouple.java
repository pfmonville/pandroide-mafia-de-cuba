package model;

public class RoleProbaCouple {
	private String role;
	private Double percentage;
	
	public RoleProbaCouple(String role, Double percentage){
		this.role = role;
		this.percentage = percentage;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Double getPercentage() {
		return percentage;
	}

	public void setPercentage(Double percentage) {
		this.percentage = percentage;
	}
}
