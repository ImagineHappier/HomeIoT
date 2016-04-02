import java.net.*;
import java.io.*;
import java.util.Date;
 

public class DaytimeServer {
	  public final static int PORT = 2335;
	  public static void main(String[] args) {  
		   try (ServerSocket server = new ServerSocket(PORT)) {
			   
		     while (true) {
		    	 
		       try (Socket connection = server.accept()) {
		         Writer out = new OutputStreamWriter(connection.getOutputStream());
		         Date now = new Date();
		         System.out.println("Server: Connected from: "+connection.getInetAddress());
		         out.write("Connected from: "+connection.getInetAddress()+"\r\n");
		         out.flush();      
		         connection.close();
		         //break;
		       } catch (IOException ex) {}       
		     }
		   } catch (IOException ex) {
		     System.err.println(ex);
		   } 
		  }
}
