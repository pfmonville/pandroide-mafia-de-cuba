package controller.ia;

public class IAGodFatherController extends IAController{

	private IGodFatherStrategy strategy;
	
	public IAGodFatherController(IGodFatherStrategy strategy){
		this.strategy = strategy;
	}
	
}
