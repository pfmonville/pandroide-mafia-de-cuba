package model;

public class Phrase {
	
	private int id;
	private String content;
	
	public Phrase(int id, String content){
		this.setId(id) ;
		this.setContent(content) ;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
