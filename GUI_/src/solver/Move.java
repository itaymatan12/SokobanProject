package solver;


import java.util.HashMap;
import search.Action;
import search.Position;
import search.Searchable;
import search.State;


public class Move implements Searchable<StateM>{

	char [][] leveldata;
	Position player;
	Position target;
	State <StateM> initialState;
	State <StateM> goalState;
	
	public Move(char[][] leveldata,Position player ,Position target) {
		this.leveldata = leveldata;
		this.player =player;
		
		this.target= target;
		
	}
	
	
	@Override
	public State<StateM> getInitialState() {
		
		StateM initstate = new StateM(player,this.leveldata);
		this.initialState = new State<StateM>(initstate);
		return initialState;		
	}
	

	@Override
	public State<StateM> getGoalState() {
		
		char[][] board =copyBoard(this.leveldata);
		
		board[this.target.getRow()][this.target.getCol()]=board[this.player.getRow()][this.player.getCol()];
		board[this.player.getRow()][this.player.getCol()]=' ' ;
		
		StateM goalstate = new StateM(this.target,board);
		this.goalState = new State<StateM>(goalstate);
		return goalState;
	}
	
	public void Printlevel(char[][] levelstr)
	{
		for(int i=0;i<levelstr.length;i++)
			for(int j=0;j<levelstr[i].length;j++)
			{
				if(j==levelstr[i].length-1)
				{
					System.out.println();
				}
				else
				System.out.print(levelstr[i][j]);
			}
	}
	
	private char[][] copyBoard(char[][] board) {

		char[][] newBoard = new char[board.length][board[0].length];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				newBoard[i][j] = board[i][j];
			}

		}
		return newBoard;
	}

		
	@Override
	public HashMap<Action, State<StateM>> getAllPossibleMoves(State<StateM> state) {
		
		HashMap<Action, State<StateM>> map = new HashMap<>();
		Position playerpos = state.getState().getPlayer();
		char [][]board = state.getState().getLeveldata();
		char[][] copyboard = copyBoard(board);
						
					copyboard = copyBoard(board);
					
					if(copyboard[playerpos.getRow()+1][playerpos.getCol()]==' ' ||copyboard[playerpos.getRow()+1][playerpos.getCol()]=='o')
						{
							
						Position newpos = new Position(playerpos.getRow()+1,playerpos.getCol());
						copyboard[playerpos.getRow()+1][playerpos.getCol()] = copyboard[playerpos.getRow()][playerpos.getCol()];
						copyboard[playerpos.getRow()][playerpos.getCol()]=' ';
						StateM s = new StateM(newpos,copyboard);
						State<StateM> newState = new State<StateM>(s);
						map.put(new Action("move down",null), newState);	
					}
					copyboard = copyBoard(board);
					if(copyboard[playerpos.getRow()-1][playerpos.getCol()]==' ' ||copyboard[playerpos.getRow()-1][playerpos.getCol()]=='o')
					{
						Position newpos = new Position(playerpos.getRow()-1,playerpos.getCol());
						copyboard[playerpos.getRow()-1][playerpos.getCol()] = copyboard[playerpos.getRow()][playerpos.getCol()];
						copyboard[playerpos.getRow()][playerpos.getCol()]=' ';
						StateM s = new StateM(newpos,copyboard);
						State<StateM> newState = new State<StateM>(s);
						map.put(new Action("move up",null), newState);
					}
					copyboard = copyBoard(board);
					if(copyboard[playerpos.getRow()][playerpos.getCol()+1]==' ' ||copyboard[playerpos.getRow()][playerpos.getCol()+1]=='o')
					{
						Position newpos = new Position(playerpos.getRow(),playerpos.getCol()+1);
						copyboard[playerpos.getRow()][playerpos.getCol()+1] = copyboard[playerpos.getRow()][playerpos.getCol()];
						copyboard[playerpos.getRow()][playerpos.getCol()]=' ';
						StateM s = new StateM(newpos,copyboard);
						State<StateM> newState = new State<StateM>(s);
						map.put(new Action("move right",null), newState);
					}
					copyboard = copyBoard(board);
					if(copyboard[playerpos.getRow()][playerpos.getCol()-1]==' ' ||copyboard[playerpos.getRow()][playerpos.getCol()-1]=='o')
					{
						Position newpos = new Position(playerpos.getRow(),playerpos.getCol()-1);
						copyboard[playerpos.getRow()][playerpos.getCol()-1] = copyboard[playerpos.getRow()][playerpos.getCol()];
						copyboard[playerpos.getRow()][playerpos.getCol()]=' ';
						StateM s = new StateM(newpos,copyboard);
						State<StateM> newState = new State<StateM>(s);
						map.put(new Action("move left",null), newState);
					}				
		return map;	
	}
}