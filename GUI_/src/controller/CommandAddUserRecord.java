package controller;

import java.util.List;

import model.Model;

public class CommandAddUserRecord implements Command
{
	private Model m;
	private String LevelName;
	private String UserId;
	private String Time;
	private String Steps;
	
	

	@Override
	public void execute() throws Exception
	{
		m.AddUserRecord(LevelName, UserId, Steps, Time);
		
	}

	@Override
	public void setParams(List<Object> params) 
	{
		this.m = (Model)params.get(0);
		this.LevelName = (String)params.get(1);
		this.UserId = (String)params.get(2);
		this.Steps = (String)params.get(3);
		this.Time = (String)params.get(4);
	}

}
