import java.net.*;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.*;

//Receive a connection from Pi then save the IP of Pi.

public class HomeReceiver extends Thread{
	public final static int HOMECONNECTION_PORT = 2017;
	public final static int HOMETHREAD_PORT = 2018;
	
	public final static int NUM_THREADS = 50;
	
	ServerSocket socket=null;

	//HomeThread homeThread = null;
	Runnable homeThread = null;
	Socket homeSocket=null;
	
	boolean debug=true;
	
	//ExecutorService pool=null;
	
	HomeReceiver(Socket homeSocket){
		this.homeSocket=homeSocket;
	    //pool = Executors.newFixedThreadPool(NUM_THREADS);
		try{
			socket = new ServerSocket(HOMECONNECTION_PORT);
			if(debug) System.out.println("HomeReceiver is ready");
		}catch (IOException ex) {
			System.err.println(ex);
		}
	}
	
	public ServerSocket getSocket(Socket socket){
		return this.socket;
	}
	
	private String receiveMessage(Socket socket){
		String str="";
	
		try{
			DataInputStream in = new DataInputStream(socket.getInputStream());
			
			if(in!=null){
				try{
					str=in.readUTF();
				}catch(IOException e){
					if(debug) System.out.println("HomeReceiver error 0: "+e.getMessage());
				}
			}
		}catch(IOException e){
			if(debug) System.out.println("HomeReceiver error 1: "+e.getMessage());
		}
		return str;
	}
	
	private int sendMessage(Socket socket, String str){
		
		try{
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			if(out!=null){
				try{
					if(debug) System.out.println("	HomeReceiver, sends a message to Pi: "+str);
					out.writeUTF(str);
				}catch(IOException e){
					if(debug) System.out.println("	HomeReceiver error 2: "+e.getMessage());
				}
			}
		}catch(IOException e){
			if(debug) System.out.println("	HomeReceiver error 3: "+e.getMessage());
		}
		
		return 0;
	}
	
	private int updateStatus(String file, String msg){
		
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(msg);
			writer.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return 0;
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				Socket connection = socket.accept();
				String str=null;
				
				//socket read
				str=receiveMessage(connection);
				if(debug) System.out.println("HomeReceiver: "+str + " ["+connection.getInetAddress() + ":"+ connection.getPort()+"]");
				
				if(str==null){
					System.out.print("str is null!!!!");
				}
				
				//0~3:hue, 0~5:hue_#, 11~13:on/off
				if(str.substring(0, 3).equals("hue")){
					updateStatus(str.substring(0, 5), str.substring(11, 13));
					if(this.homeSocket!=null)sendMessage(this.homeSocket, str);
				}
				
				//Todo: check status of hue 1,2,3
				if(str.equals("status")){
					if(this.homeSocket!=null) sendMessage(this.homeSocket, str);
				}
				
				//try keep connection from Pi. 
				//Todo: make a thread whenever a new pi is arrived.
				if(str.equals("Connection")){
					//change to PiConnection
					if(debug) System.out.println("HomeReceiver, Connection is verified");
					
					// keep the Pi socket information.
					this.homeSocket=connection;

					//homeThread = new HomeThread(homeSocket, HOMETHREAD_PORT);
					//pool.submit(homeThread);
					
					if(this.homeSocket!=null) sendMessage(homeSocket, "ACK, Connected to homeReceiver");
				}
				//connection.close();
			} catch (IOException ex) {
				if(debug) System.out.println("HomeReceiver error 4: "+ex.getMessage());
			}
		}
	}
}
