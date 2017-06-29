package controller;

import java.util.List;

import model.Model;

public class CommandAddLevel implements Command 
{
	private Model m;
	private String LevelName;

	@Override
	public void execute() throws Exception {
		m.AddLevel(LevelName);
		
	}

	@Override
	public void setParams(List<Object> params) {
		this.m = (Model)params.get(0);
		this.LevelName = (String)params.get(1);
		
	}

}
