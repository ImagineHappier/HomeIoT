import java.net.*;
import java.io.*;

public class WebThread extends Thread{
	Socket socket;
	//DataInputStream in;
	//DataOutputStream out;
	
	WebThread(Socket socket){
		this.socket=socket;
		try{
			//in = new DataInputStream(socket.getInputStream());
			//out = new DataOutputStream(socket.getOutputStream());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void run(){
		
		try{
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			
			/*
			StringBuilder message = new StringBuilder();
			InputStreamReader reader = new InputStreamReader(in, "ASCII");
			for (int c = reader.read(); c != -1; c = reader.read()) {
				message.append((char) c);
			}
			System.out.println("	WebThread Thread: "+message);
			*/
			
		}catch (IOException ex) {
	        System.err.println(ex);
		} finally {
	        try {
	        	socket.close();
	        }catch (IOException e) {
	          // ignore;	
	        }    
		}
	}
}
