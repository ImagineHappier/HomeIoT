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
		
		System.out.println("HomeReceiver, error check: "+ str);
		try{
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			if(out!=null){
				try{
					if(debug) System.out.println("	Send a message to "
				+"["+socket.getInetAddress() + ":"+ socket.getPort()+"]: "+str);
					
					
					
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
	
	private String getBulbStatus(String file){
		String str="";
		String line = null;
		
		File fp=null;;
		FileReader fileReader=null;
		BufferedReader reader=null;
		
		try {
			fp = new File(file);
			fileReader = new FileReader(fp);	
			reader = new BufferedReader(fileReader);
			
			if((line = reader.readLine()) != null){
				str+=line;
			}
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return str;
		
	}
	
	private String getHueStatus(Socket socket){
		//socket is not used yet.
		String str="";
		str+="hue_1:"+getBulbStatus("hue_1")+",";
		str+="hue_2:"+getBulbStatus("hue_2")+",";
		str+="hue_3:"+getBulbStatus("hue_3");
		return str;
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				//if a connection comes
				Socket connection = socket.accept();
				String str=null;
				
				//read a message from the connection 
				str=receiveMessage(connection);
				System.out.println("HomeReceiver, str check: " + str);
				
				if(debug) System.out.println("HomeReceiver: "+str + " ["+connection.getInetAddress() + ":"+ connection.getPort()+"]");
				
				//if string is null, it might be in a problem situation. Hope not to see it
				if(str==null){
					System.out.print("str is null!!!!");
					continue;
				}
				if(str.length()<=1){
					System.out.print("str is <=1!!!!");
					continue;
				}

				//hue_3_turn_off
				//0~3:hue, 0~5:hue_#, 11~13:on/off
			if(str.length()>1){
				//if the message starts from "hue"
				if(str.substring(0, 3).equals("hue")){

					//if operation in a message is status
					if(str.substring(6, 10).equals("stat")){
						//read status of each bulb from status file, then generate a message. 
						String strHueStatus=getHueStatus(homeSocket);
						System.out.println("	Hue Status: "+strHueStatus);
						
						//send status message to the connection.
						if(connection!=null)sendMessage(connection, strHueStatus);
						
						//function test
						//if(connection!=null)sendMessage(this.homeSocket, strHueStatus);
					}
					
					//if operation in a message is turn on/off
					if(str.substring(6, 10).equals("turn")){
						//update the bulb status file.
						updateStatus(str.substring(0, 5), str.substring(11, 13));
						
						//send message to Pi
						if(this.homeSocket!=null)sendMessage(this.homeSocket, str);
					}
				}
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
