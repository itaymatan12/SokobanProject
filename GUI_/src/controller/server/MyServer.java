package controller.server;


import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class MyServer {
	
	//data members
	private int port;
	private ClientHandler ch;
	private volatile boolean stop;
	private ServerSocket server;
	
	//constructor with values
	public MyServer(int port,ClientHandler ch)
	{
		this.port=port;
		this.ch=ch;
		stop=false;
	}
	
	//running the server asa thread
	public void Start()
	{
			new Thread(new Runnable() {
			public void run()
			{
				try
				{
					runServer();
				}
				
				catch (Exception e){}
			}
			}).start();
	}
	
	
	//stoping the thread -> stoping the server
	public void stop()
	{
		stop=true;
	} 
	
	//function that runs the server
	private void runServer()throws Exception {
		
		this.server =new ServerSocket(port);
		//server.setSoTimeout(1000);
		while(!stop)
		{
			try{
				
				 Socket aClient=server.accept(); // blocking call

					{
					  try {
						  	System.out.println(aClient.getLocalPort());
						  	ch.aSyncReadInputsAndSend(aClient.getInputStream(),aClient.getOutputStream());
						  	
						  	aClient.getInputStream().close();
							aClient.getOutputStream().close();
							aClient.close(); 
							
					  	  }
					  
					  catch (IOException e) {/*...*/}
					  
					 
				}
				
				
				 
			}
			catch(SocketTimeoutException e) {/*...*/}
		}
		server.close(); //WOW! we should wait for all threads before closing!
		}

	//Getters and Setters
	public int getPort() {
		return port;
	}


	public void setPort(int port) {
		this.port = port;
	}


	public ServerSocket getServer() {
		return server;
	}

	public void setServer(ServerSocket server) {
		this.server = server;
	}
}