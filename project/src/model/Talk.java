package model;

public class Talk {
	
	private Question question;
	private Answer answer;
	
	public Talk(Question q, Answer a) {
		setQuestion(q) ;
		setAnswer(a) ;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public Answer getAnswer() {
		return answer;
	}

	public void setAnswer(Answer answer) {
		this.answer = answer;
	}
	
}
