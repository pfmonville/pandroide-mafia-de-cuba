package model;

public class Player {
	
	private Role role;
	private int position;
	private boolean human;
	private boolean lastPlayer;
	
	public Player(Role r, int pos, boolean human,boolean lastPlayer){
		role = r ;
		position = pos ;
		this.setHuman(human) ;
		this.lastPlayer = lastPlayer ;
	}
	
	public boolean isFirstPlayer(){
		return (position==1) ;
	}
	
	public boolean isLastPlayer(){
		return lastPlayer;
	}
	
	public void takeDiamonds(int diamonds){
		role = new Thief(diamonds) ;
	}
	
	public void takeToken(String token){
		switch (token) {
			case "LoyalHenchman" :
				role = new LoyalHenchman();
				break ;
			case "FBIAgent" :
				role = new FBI();
				break;
			case "CIAAgent" :
				role = new CIA();
				break; 
			case "Cleaner" :
				role = new Cleaner() ;
				break ;
			case "Driver" :
				role = new Driver(new Player(null,position-1,false,false));
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
	}

	public boolean isHuman() {
		return human;
	}

	public void setHuman(boolean human) {
		this.human = human;
	}
}
