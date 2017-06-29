package controller;

import java.util.List;

import model.Model;
import view.View;

public class CommandGetChosenUserScores implements Command {
	
	private Model m;
	private View v;
	private String userid;

	@Override
	public void execute() throws Exception {
		
		v.setUserScores(m.getUserScores(userid));
		
	}

	@Override
	public void setParams(List<Object> params) {
		this.m = (Model)params.get(0);
		this.userid = (String)params.get(1);
		this.v = (View)params.get(2);
		
	}

}
