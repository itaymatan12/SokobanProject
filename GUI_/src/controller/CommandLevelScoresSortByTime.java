package controller;

import java.util.List;

import model.Model;

public class CommandLevelScoresSortByTime implements Command 
{
	private Model m;
	private String LevelName;
	

	@Override
	public void execute() throws Exception {
		System.out.println("8484848");
		m.sortLevelByTime(LevelName);
		
	}

	@Override
	public void setParams(List<Object> params) {
		this.m = (Model)params.get(0);
		this.LevelName = (String)params.get(1);
		
	}

}
