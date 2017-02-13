package model;

public class Player {
	
	private Role role;
	private int position;
	
	public Player(Role r, int pos){
		role = r ;
		position = pos ;
	}
	
	public boolean isFirstPlayer(){
		return (position==1) ;
	}
	
	public boolean isLastPlayer(){
		//TODO
		return true;
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
				role = new Driver(new Player(null,position-1));
				break;
			default :
				break;
		}
	}
	
	public SecretID reveal(){
		if(role instanceof Thief)
			return new SecretID(role.getName(),role.getNbDiamondsStolen(),"");
		else {
			if(role instanceof StreetUpchin)
				return new SecretID(role.getName(),role.getNbDiamondsStolen(),"");
		}
		return new SecretID(role.getName(),role.getNbDiamondsStolen(),role.getName());
	}

	public int getPosition(){
		return position ;
	}
	
	public Role getRole(){
		return role ;
	}
}
