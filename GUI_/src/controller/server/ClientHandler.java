package controller.server;


import java.io.InputStream;
import java.io.OutputStream;

//interface that defines a way to handel the client and to connect between the client and the server 
public interface ClientHandler {
	
	public void aSyncReadInputsAndSend(InputStream in, OutputStream out);


}