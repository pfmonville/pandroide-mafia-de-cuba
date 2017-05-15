package controller.ai.strategy;

import model.Inspect;

public class GodFatherBaseStrategy {

	protected Inspect inspect;
	private boolean inspectSet = false;
	
	public void setInspect(Inspect inspect){
		this.inspect = inspect;
		inspectSet = true;
	}
	
	protected boolean isInspectSet(){
		return inspectSet;
	}
	
}
