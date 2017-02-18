package model;

public class Player {
	private Role role;
	private int position;
	private boolean human;
	
	Player(){
		//TODO
	}
	
	public boolean isFirstPlayer(){
		//TODO
		return true;
	}
	
	public boolean isLastPlayer(){
		//TODO
		return true;
	}
	
	public void takeDiamonds(int diamonds){
		//TODO
	}
	
	public void takeToken(String token){
		//TODO
	}
	
	public boolean isHuman(){
		return human;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public void setHuman(boolean human) {
		this.human = human;
	}

	
}
