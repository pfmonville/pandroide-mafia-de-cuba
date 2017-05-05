package model;

import controller.App;

public class Player {
	
	private Role role;
	private int position;
	private boolean human;
	private boolean lastPlayer;
	private boolean thief;
	private Box box ;
	
	
	public Player(int pos, boolean human, boolean lastPlayer){
		this.position = pos;
		this.human = human;
		this.lastPlayer = lastPlayer;
		this.thief = false;
	}
	
	public Player(Role r, int pos, boolean human,boolean lastPlayer){
		role = r ;
		position = pos ;
		this.setHuman(human) ;
		this.lastPlayer = lastPlayer ;
		if(r.getName() == App.rules.getNameThief()){
			this.thief = true;
		}else{
			this.thief = false;
		}
	}
	
	/**
	 * 
	 * @return true if the player is the first after the godFather false otherwise
	 */
	public boolean isFirstPlayer(){
		return (position==2) ;
	}
	
	public boolean isLastPlayer(){
		return lastPlayer;
	}
	
	public void takeDiamonds(int diamonds){
		role = new Thief(diamonds) ;
		this.thief = true;
	}
	
	public void takeToken(String token){
		switch (token) {
			case "Fid�le" :
				role = new LoyalHenchman();
				break ;
			case "FBI" :
				role = new FBI();
				break;
			case "CIA" :
				role = new CIA();
				break; 
			case "Agent":
				role = new Agent(App.rules.getNameAgentLambda());
			case "Nettoyeur" :
				role = new Cleaner() ;
				break ;
			case "Chauffeur" :
				role = new Driver(position-1);
				break;
			default :
				role = new StreetUrchin() ;
				break;
		}
	}
	
	public SecretID reveal(){
		if(role instanceof Thief)
			return new SecretID(role.getName(),role.getNbDiamondsStolen(),"");
		else {
			if(role instanceof StreetUrchin)
				return new SecretID(role.getName(),role.getNbDiamondsStolen(),"");
		}
		return new SecretID(role.getName(),role.getNbDiamondsStolen(),role.getName());
	}

	public int getPosition(){
		return position ;
	}
	
	public void setPosition(int position) {
		this.position = position;
	}
	
	public Role getRole(){
		return role ;
	}
	
	public void setRole(Role role) {
		this.role = role;
		if(role.getName() == "Voleur"){
			this.thief = true;
		}else{
			this.thief = false;
		}
	}

	public boolean isHuman() {
		return human;
	}

	public void setHuman(boolean human) {
		this.human = human;
	}

	public void setLastPlayer(boolean lastPlayer) {
		this.lastPlayer = lastPlayer;
	}

	public boolean isThief() {
		return thief;
	}
	
	public Box getBox(){
		return box ;
	}
	
	public void setBox(Box box){
		this.box = box ;
	}
	
	
}
