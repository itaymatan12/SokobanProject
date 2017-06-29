package solver;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import controller.CommandLoadFileName;
import search.BFS;
import search.Position;
import search.Solution;
import strips.Action;
import strips.Strips;
import model.Utilities;
import model.data.Box;
import model.data.DestofBox;
import model.data.Figure;
import model.data.GameObject;
import model.data.LevelLoader;
import model.data.Transperent;
import model.data.Wall;

public class SokobanSolver {

	static Position PS;
	static Position PD;

	public static void main(String[] args) throws IOException 
	{
		//local variables
		BFS<StateM> bfs = new BFS<StateM>();
		Utilities u = new Utilities();
		CommandLoadFileName c =new CommandLoadFileName();
		List<String> r = new ArrayList<>();
		Position d;
		Move se;
		int col = 0;
		String val;
		model.data.Level l= new model.data.Level(); 
		Position b;
		Solution sol = null;
		int row = 0;
		
		//if we have loading address and saving address
		if (args.length==2) {
			String strloadingpath = args[0];
			String strsavingpath = args[1];
			
			//try catch for file opening and loading errors
			try 
			{
				LevelLoader ldr = c.getFilecreators().get(u.end_of_file(strloadingpath));
				ldr.loadLevel(new FileInputStream("levels for example/" +strloadingpath), l);
				
			} 
			
			catch (Exception e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//casting ArrayList<ArrayList<GameObject>> to char[][]
			
			//getting the biggest row in the level
			int max =0;
			for(ArrayList<GameObject> t : l.getLevelim())
			{
				if(t.size()>max)
				{
					max = t.size();
				}
			}
			
			//creating the level matrix
			char [][] level = new char [l.getLevelim().size()][max];
			
			//filling the level char matrix from the rrayList<ArrayList<GameObject>> data
			for (int i = 0; i < l.getLevelim().size(); i++) {
				for (int j = 0; j < l.getLevelim().get(i).size(); j++) 
				{
					if(l.getLevelim().get(i).get(j) instanceof Wall)
					{
						level[i][j] = '#';
					}
					
					else if(l.getLevelim().get(i).get(j) instanceof Box)
					{
						level[i][j] = '@';
					}
					
					if(l.getLevelim().get(i).get(j) instanceof DestofBox)
					{
						level[i][j] = 'o';
					}
					
					if(l.getLevelim().get(i).get(j) instanceof Transperent)
					{
						level[i][j] = ' ';
					}
					
					if(l.getLevelim().get(i).get(j) instanceof Figure)
					{
						level[i][j] = 'A';
					}		
				}
			}
			
			//creating the plannable problem
			SokobanPlannable sokoplan = new SokobanPlannable(level);
			Strips s = new Strips();

			List<Action> list = s.plan(sokoplan.readLevel());
			
			for (int i = 0; i < list.size(); i++)
			{
				 if (list.get(i).toString().startsWith("push")) 
				{

					val = list.get(i).toString().substring(list.get(i).toString().indexOf('=') + 1,
							list.get(i).toString().length());
					row = Integer.parseInt(val.substring(0, val.indexOf(',')));
					col = Integer.parseInt(val.substring(val.indexOf(',') + 1, val.length()));
					d = new Position(row, col);

					val = list.get(i).toString().substring(list.get(i).toString().indexOf('_') + 1,
							list.get(i).toString().indexOf('='));
					row = Integer.parseInt(val.substring(0, val.indexOf(',')));
					col = Integer.parseInt(val.substring(val.indexOf(',') + 1, val.length()));
					b = new Position(row, col);

					Push se2 = new Push(level, PD, b, d);
					sol = bfs.search(se2);

					if (sol != null) 
					{

						for (search.Action a : sol.getActions()) {
							if (a.getMiniaction() != null) {
								for (search.Action bl : a.getMiniaction()) {
									r.add(bl.getName());
								}
							}
							r.add(a.getName());
						}
					}
					
					if (list.size() > i + 1) 
					{
						level[b.getRow()][b.getCol()] = ' ';
						level[PD.getRow()][PD.getCol()] = ' ';
						level[d.getRow()][d.getCol()] = '@';

						val = list.get(i + 1).toString().substring(list.get(i + 1).toString().indexOf('_') + 1,
								list.get(i + 1).toString().indexOf('='));
						row = Integer.parseInt(val.substring(0, val.indexOf(',')));
						col = Integer.parseInt(val.substring(val.indexOf(',') + 1, val.length()));

						level[row][col] = 'A';
					}
				}

				 else if (list.get(i).toString().startsWith("move"))
				{
					val = list.get(i).toString().substring(list.get(i).toString().indexOf('=') + 1,
							list.get(i).toString().length());
					row = Integer.parseInt(val.substring(0, val.indexOf(',')));
					col = Integer.parseInt(val.substring(val.indexOf(',') + 1, val.length()));
					PD = new Position(row, col);

					val = list.get(i).toString().substring(list.get(i).toString().indexOf('_') + 1,
							list.get(i).toString().indexOf('='));
					row = Integer.parseInt(val.substring(0, val.indexOf(',')));
					col = Integer.parseInt(val.substring(val.indexOf(',') + 1, val.length()));

					PS = new Position(row, col);

					se = new Move(level, PS, PD);

					sol = bfs.search(se);

					level[PS.getRow()][PS.getCol()] = ' ';
					level[PD.getRow()][PD.getCol()] = 'A';
					for (search.Action a : sol.getActions())
					{
						r.add(a.getName());
					}
				} 	
			}
			PrintWriter outputStream = new PrintWriter(new FileOutputStream("levels for example/" + strsavingpath));
			for (String move : r) 
			{
				outputStream.println(move);
				outputStream.flush();
				System.out.println(move);
			}
			outputStream.close();
		}
		else
		{
			System.out.println("Error in user file (load and save) information - cant solve");	
		}
	}
}