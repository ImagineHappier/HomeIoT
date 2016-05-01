import java.net.*;
import java.util.Date;
import java.io.*;

public class DaytimeClient {

	public final static int WEBCONNECTION_PORT = 2016;
	static boolean debug = true;
	
	static class ClientSender extends Thread {
		Socket socket;
		DataOutputStream out;
		DataInputStream in;
		ClientSender(Socket socket){
			
			this.socket=socket;
			
			try{
				out=new DataOutputStream(socket.getOutputStream());
				in = new DataInputStream(socket.getInputStream());
				
			}catch(Exception e){}
			
			if(debug) System.out.println("DaytimeClient ClientSender is started");
		}
		
		public void run(){
			if(debug) System.out.println("DaytimeClient run is started");
			try{
				if(out!=null){
					out.writeUTF("hue_3_turn_off");
					//out.writeUTF("hue_0_stat_00");
				}
			}catch (IOException ex) {
				System.err.println(ex);
			}
						
		}
	}
	
	public static void main(String[] args) {
		//String hostname = args.length > 0 ? args[0] : "localhost";
		String hostname = args.length > 0 ? args[0] : "www.imaginehappier.com";
		
		Socket socket = null;
		try {
			//connect to cloud server
			socket = new Socket(hostname, WEBCONNECTION_PORT);
			socket.setSoTimeout(15000);
			if(debug) System.out.println("web client is connected to web receiver");
			
			Thread sender = new Thread(new ClientSender(socket));
			sender.start();
			
			
		}catch(ConnectException ce){ 
			ce.printStackTrace();
		}catch(Exception e){}
		
	}

}
