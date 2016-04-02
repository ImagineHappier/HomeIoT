import java.net.*;
import java.io.*;

public class DaytimeClientOld {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String hostname = args.length > 0 ? args[0] : "localhost";
	    Socket socket = null;
	    try {
	      socket = new Socket(hostname, 2016);
	      System.out.println("established");
	     
	      try {
	    	  String str="Hue!!";
	          Writer out = new OutputStreamWriter(socket.getOutputStream());
	          out.write(str);
	          out.flush(); 
	          
	          System.out.println("str: "+str);
	          
	        } catch (IOException ex) {
	          System.err.println(ex);
	        }
	      
	      
	    } catch (IOException ex) {
	      System.err.println(ex);
	    } finally {
	      if (socket != null) {
	        try {
	          socket.close();
	        } catch (IOException ex) {
	          // ignore
	        }
	      }
	    }

	}

}
