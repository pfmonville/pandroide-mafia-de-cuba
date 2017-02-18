package controller.ia;

public class IASuspectController extends IAController{
	private ISuspectStrategy strategy;
	
	public IASuspectController(ISuspectStrategy strategy){
		this.strategy = strategy;
	}
}
