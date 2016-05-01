import java.net.*;
import java.io.*;
import java.util.Date;
 

public class PiClient {
	
	public final static int HOMECONNECTION_PORT = 2017;
	static boolean debug = true;
	
	static class ClientSender extends Thread {
		Socket socket;
		DataOutputStream out;
		String name;
		
		ClientSender(Socket socket){
			
			this.socket=socket;
			
			try{
				out=new DataOutputStream(socket.getOutputStream());
			}catch(Exception e){}
			
			if(debug) System.out.println("ClientSender is started");
		}
		
		public void run(){
			if(debug) System.out.println("ClientSender run is started");
			try{
				if(out!=null){
					if(debug) System.out.println("ClientSender sends a message: Connection");
					out.writeUTF("Connection");
				}
			}catch (IOException ex) {
				System.err.println(ex);
			}
		}
	}
	static class ClientReceiver extends Thread{
		Socket socket;
		DataInputStream in;
		
		ClientReceiver(Socket socket){
			this.socket = socket;
			try{
				in = new DataInputStream(socket.getInputStream());
			}catch(IOException e){
				if(debug) System.out.println("ClientReceiver error 0: "+e.getMessage());
			}
		}
		
		public void run(){
			if(debug) System.out.println("ClientReceiver run is started");
			String str=null;
			String s=null;
			Process oProcess =null;
			
			BufferedReader stdOut=null;
			BufferedReader stdError=null;
			while(in!=null){
				try{
					str = in.readUTF();
					if(debug) System.out.println("ClientReceiver received a message: "+str);
				}catch(IOException e){
					//if(debug) System.out.println("ClientReceiver error 1: "+e.getMessage());
				}
				
				
				if(str.substring(0, 3).equals("hue")){
					try {
						System.out.println("ClientReceiver: "+str);
						str="api/"+str;
						System.out.println("ClientReceiver execute file: "+str);
						oProcess = new ProcessBuilder("/bin/sh","-c",str).start();
						//oProcess = new ProcessBuilder("/bin/sh","-c","api/python 3_turnon.py").start();
						//oProcess = new ProcessBuilder("cmd", "/c", "dir", "/?").start();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					str="";
				}
			}
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		//String hostname = args.length > 0 ? args[0] : "localhost";
		String hostname = args.length > 0 ? args[0] : "www.imaginehappier.com";
		
		Socket socket = null;
		try {
			//connect to cloud server
			socket = new Socket(hostname, HOMECONNECTION_PORT);
			//socket.setSoTimeout(15000);
			if(debug) System.out.println("Pi client is connected to cloud server");
			
			Thread sender = new Thread(new ClientSender(socket));
			sender.start();
			
			Thread receiver = new Thread(new ClientReceiver(socket));
			receiver.start();
			
		}catch(ConnectException ce){ 
			ce.printStackTrace();
		}catch(Exception e){}
	}

}
