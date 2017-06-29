package controller.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

import entities.User;
import entities.UserRecord;
import model.data.Level;
import view.Display;
import view.View;;

public class RegularClientHandler extends Observable implements ClientHandler ,View{

	
	private BufferedReader reader;
	private DataOutputStream writer;
	private boolean stop = false;

	
	public RegularClientHandler ()
	{
		
	
		
	}

	
		
		
		
		public void aSyncReadInputsAndSend(InputStream in, OutputStream out)
		{ 

		    reader =new BufferedReader(new InputStreamReader(in));
		    writer = new DataOutputStream(out);
		    String menu ="Choose a command that you want to execute: \n" +
			"1. enter \"loadfile\" if you want to load Level from a file \n"+
			"2. enter \"savefile\" if you want to save Level in a file\n"+
			"3. enter \"display\" if you want to display the Level \n"+
			"4. enter \"move\" if you want to move the figure \n"+
			"5. enter \"exit\" if you want to exit the game \n";
		    try {
				
				writer.writeBytes(menu);
				System.out.println(menu);
			} 
		    catch (IOException e1) {
				
			}
		    

					String commandLine = "";
					while(!stop) {
						//writer.write("Enter command: " );
						//writer.flush();
						try {
							commandLine=reader.readLine();
							System.out.println(commandLine);
							String[] arr = commandLine.split(" ");
							LinkedList<String> params = new LinkedList<String>();
							for (String param : arr) {
								params.add(param);
							}
							if(params.get(0).equals("exit")) {
								stop=true;
								
								
							}
							if (!stop) {
								setChanged();
								notifyObservers(params);
							}
						} 
						catch (IOException e) {
							// TODO Auto-generated catch block
							//e.printStackTrace();
							
							
							
						}
						
					} 			
				stop = false;
		}
	


			
	

			
		
		@Override
		public void displayLevel(Level l,Display d)
		{
		
			try {
				String arr[] =d.display(l).split("\n");
				writer.write(arr.length);
				
				writer.writeBytes(d.display(l));
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

			@Override
			public void displayMessage(String string) {
				
				try {
					writer.writeBytes(string);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void stop() {
				stop=true;
				
			}

			@Override
			public void setcontrols(String up, String down, String left, String right) {
				// TODO Auto-generated method stub
				
			}





			@Override
			public void start() {
				// TODO Auto-generated method stub
				
			}





			@Override
			public void setPlayersList(List<User> p) {
				// TODO Auto-generated method stub
				
			}





			@Override
			public void setLevelScores(List<UserRecord> p) {
				// TODO Auto-generated method stub
				
			}





			@Override
			public void setUserScores(List<UserRecord> p) {
				// TODO Auto-generated method stub
				
			}





			
			
}



