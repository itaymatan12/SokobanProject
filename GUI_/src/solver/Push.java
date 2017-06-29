package solver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import search.Action;
import search.BFS;
import search.Position;
import search.PositionAndActions;
import search.Searchable;
import search.Solution;
import search.State;


public class Push implements Searchable<StateM> {

	char[][] level;
	Position player;
	Position box;
	Position target;
	
	public Push(char[][] level,Position player, Position box,Position target) {
		this.level = level;
		this.player =player;
		this.box=box;
		this.target= target;
	}

	@Override
	public State<StateM> getInitialState() {
		StateM s = new StateM(this.box,this.level);
		State<StateM> initialState = new State<StateM>(s);
		return initialState;
	}

	@Override
	public State<StateM> getGoalState() {
	
		char[][] board =copyBoard(level);
		

		char[][]testboard =copyBoard(level);
		testboard[player.getRow()][player.getCol()]=' ';
		Move adapter = new Move(testboard, this.box, this.target);
		BFS<StateM> bfs = new BFS<StateM>();
		Solution sol = bfs.search(adapter);

		int rowb =0;
		int colb=0;
		if(sol!=null)
		{
			switch(sol.getActions().get(sol.getActions().size()-1).getName())
			{
			case "move down":
				rowb -= 1;
				break;
			case "move left":
				colb += 1;
				break;
			case "move up":
				rowb += 1;
				break;
			case "move right":
				colb -= 1;
				break;
			}
		}
		
		board[this.target.getRow()][this.target.getCol()]=board[this.box.getRow()][this.box.getCol()];
		board[this.box.getRow()][this.box.getCol()]=' ';
		board[this.player.getRow()][this.player.getCol()]=' ';
		board[this.target.getRow()+rowb][this.target.getCol()+colb]='A';
		
		StateM  s = new StateM(this.target,board);
		State<StateM> goalState = new State<StateM>(s);
		return goalState;
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
	
	
	private List<PositionAndActions> getEmptyCell(char [][]l , Position player,Position box) 
	{
		char [][]level = copyBoard(l);

		List<PositionAndActions> positions= new ArrayList<PositionAndActions>(); 
		Solution sol=ExistsPath (copyBoard(l) , player , new Position (box.getRow()-1 ,box.getCol() ) );
		
		level = copyBoard(l);
		if( (level[box.getRow()+1][box.getCol()]!='#') &&  
				(sol != null || (box.getRow()-1==player.getRow() && box.getCol()==player.getCol() ) ))
			
		{
			if(sol!=null){
				positions.add(new PositionAndActions(new Position(box.getRow()+1,box.getCol()),sol.getActions()) );
			}
			else
				positions.add(new PositionAndActions(new Position(box.getRow()+1,box.getCol()),null) );
		}
		level = copyBoard(l);
		 sol =ExistsPath (copyBoard(l) ,player , new Position (box.getRow()+1 ,box.getCol() ) );
		if(level[box.getRow()-1][box.getCol()]!='#'&& 
				(sol != null || (box.getRow()+1==player.getRow() && box.getCol()==player.getCol()) ))
		{
			if(sol!=null){
				
			positions.add(new PositionAndActions(new Position(box.getRow()-1,box.getCol()),sol.getActions()) );

			}

			else
				positions.add(new PositionAndActions(new Position(box.getRow()-1,box.getCol()),null) );
		}
		level = copyBoard(l);
		sol=ExistsPath (copyBoard(l) ,player , new Position (box.getRow(),box.getCol()-1 ));
		if(level[box.getRow()][box.getCol()+1]!='#'  && 
				(sol != null || (box.getRow()==player.getRow()&& box.getCol()-1==player.getCol() ) ))
		{
			if(sol!=null){
				positions.add(new PositionAndActions(new Position(box.getRow(),box.getCol()+1),sol.getActions()) );

				}

			else
				positions.add(new PositionAndActions(new Position(box.getRow(),box.getCol()+1),null) );
		}
		level = copyBoard(l);
		sol=ExistsPath (copyBoard(l),player , new Position (box.getRow() ,box.getCol()+1 ));
		if( level[box.getRow()][box.getCol()-1]!='#' && 
				(sol != null || (box.getRow()==player.getRow()&& box.getCol()+1==player.getCol() ) ))
		{
			
			if(sol!=null){
				positions.add(new PositionAndActions(new Position(box.getRow(),box.getCol()-1),sol.getActions()) );

				}

			else
				positions.add(new PositionAndActions(new Position(box.getRow(),box.getCol()-1),null) );
		}
		
		return positions;
		
	}
	
	public Solution ExistsPath (char[][] level ,Position soko ,Position target)
	{
		Move SM = new Move(copyBoard(level), soko,target);
		BFS<StateM> bfs = new BFS<StateM>();
		Solution sol = bfs.search(SM);
		
		if(soko.getRow()==target.getRow() && soko.getCol()==target.getCol())
			return null;
		
		if (sol!=null){
			return sol;
		}
		else
			return null;
	}
	
	public Position playerPos(char[][] levelboard){
		
		for(int i=0; i<levelboard.length;i++)
			for(int j=0;j<levelboard[i].length;j++)
			{
				if(levelboard[i][j]=='A')
					return new Position(i,j);
			}
		return null;
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

	@Override
	public HashMap<Action, State<StateM>> getAllPossibleMoves(State<StateM> state) {
		HashMap<Action, State<StateM>> map = new HashMap<>();
		Position boxpos = state.getState().getPlayer();
		char [][]board = state.getState().getLeveldata();
		char[][] copyboard = copyBoard(board);
		Position playerpos = playerPos(copyboard);
		
		
		List<PositionAndActions> pos = getEmptyCell(copyBoard(board),playerpos,boxpos);
			
			if(pos!=null)
			{
				for(int i=0; i<pos.size();i++)
				{
					copyboard = copyBoard(board);
					
					if(pos.get(i).getPos().getRow()==boxpos.getRow()+1 && pos.get(i).getPos().getCol() == boxpos.getCol())
					{
											
						Position newbox = new Position(pos.get(i).getPos().getRow(),pos.get(i).getPos().getCol());
						
						copyboard[pos.get(i).getPos().getRow()][pos.get(i).getPos().getCol()] = copyboard[boxpos.getRow()][boxpos.getCol()];
						copyboard[boxpos.getRow()][boxpos.getCol()]='A';
						copyboard[playerpos.getRow()][playerpos.getCol()]=' ';
						StateM s = new StateM(newbox,copyboard);
						State<StateM> newState = new State<StateM>(s);
						map.put(new Action("move down",pos.get(i).getAct()), newState);	
					}
					else if(pos.get(i).getPos().getRow()==boxpos.getRow()-1 && pos.get(i).getPos().getCol() == boxpos.getCol())
					{
						Position newbox = new Position(pos.get(i).getPos().getRow(),pos.get(i).getPos().getCol());

						copyboard[pos.get(i).getPos().getRow()][pos.get(i).getPos().getCol()] =copyboard[boxpos.getRow()][boxpos.getCol()];
						copyboard[boxpos.getRow()][boxpos.getCol()]='A';
						copyboard[playerpos.getRow()][playerpos.getCol()]=' ';
						StateM s = new StateM(newbox,copyboard);
						State<StateM> newState = new State<StateM>(s);
						map.put(new Action("move up",pos.get(i).getAct()), newState);
					}
					else if(pos.get(i).getPos().getRow()==boxpos.getRow() && pos.get(i).getPos().getCol() == boxpos.getCol()+1)
					{
						Position newbox = new Position(pos.get(i).getPos().getRow(),pos.get(i).getPos().getCol());
						copyboard[pos.get(i).getPos().getRow()][pos.get(i).getPos().getCol()] = copyboard[boxpos.getRow()][boxpos.getCol()];
						copyboard[boxpos.getRow()][boxpos.getCol()]='A';
						copyboard[playerpos.getRow()][playerpos.getCol()]=' ';
						StateM s = new StateM(newbox,copyboard);
						State<StateM> newState = new State<StateM>(s);
						map.put(new Action("move right",pos.get(i).getAct()), newState);
					}
					else if(pos.get(i).getPos().getRow()==boxpos.getRow() && pos.get(i).getPos().getCol() == boxpos.getCol()-1)
					{
						Position newbox = new Position(pos.get(i).getPos().getRow(),pos.get(i).getPos().getCol());

						copyboard[pos.get(i).getPos().getRow()][pos.get(i).getPos().getCol()] = copyboard[boxpos.getRow()][boxpos.getCol()];
						copyboard[boxpos.getRow()][boxpos.getCol()]='A';
						copyboard[playerpos.getRow()][playerpos.getCol()]=' ';
						StateM s = new StateM(newbox,copyboard);
						State<StateM> newState = new State<StateM>(s);
						map.put(new Action("move left",pos.get(i).getAct()), newState);
					}
					
				}	
			}
			return map;
		}	
}