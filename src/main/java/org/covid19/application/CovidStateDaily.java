package org.covid19.application;

public class CovidStateDaily {
	private String state;
	private String date;
	private Integer caseNum;
	private Integer stateFIPS;
	
	
	
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Integer getCaseNum() {
		return caseNum;
	}
	public void setCaseNum(Integer caseNum) {
		this.caseNum = caseNum;
	}
	public Integer getStateFIPS() {
		return stateFIPS;
	}
	public void setStateFIPS(Integer stateFIPS) {
		this.stateFIPS = stateFIPS;
	}
	
	
}
