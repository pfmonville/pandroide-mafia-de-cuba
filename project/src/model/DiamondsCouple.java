package model;

public class DiamondsCouple {
	private int diamondsReceived;
	private int diamondsGiven;
	
	public DiamondsCouple(int diamondsReceived, int diamondsGiven){
		this.diamondsReceived = diamondsReceived;
		this.diamondsGiven = diamondsGiven;
	}

	public int getDiamondsReceived() {
		return diamondsReceived;
	}

	public void setDiamondsReceived(int diamondsReceived) {
		this.diamondsReceived = diamondsReceived;
	}

	public int getDiamondsGiven() {
		return diamondsGiven;
	}

	public void setDiamondsGiven(int diamondsGiven) {
		this.diamondsGiven = diamondsGiven;
	}
	
	
}
