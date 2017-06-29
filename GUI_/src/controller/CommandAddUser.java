package controller;

import java.util.List;

import model.Model;

public class CommandAddUser implements Command 
{
	
	private Model m;
	private String UserId;

	@Override
	public void execute() throws Exception
	{
		m.AddUser(UserId);

		
	}

	@Override
	public void setParams(List<Object> params)
	{
		this.m = (Model)params.get(0);
		this.UserId = (String)params.get(1);
	}

}
