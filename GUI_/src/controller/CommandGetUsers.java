package controller;

import java.util.List;

import model.Model;
import view.View;

public class CommandGetUsers implements Command {
	
	private Model m;
	private View v;

	@Override
	public void execute() throws Exception 
	{
		v.setPlayersList(m.getUsers());
		
	}

	@Override
	public void setParams(List<Object> params) 
	{
		this.m = (Model)params.get(0);
		this.v = (View)params.get(1);
		
	}

}
