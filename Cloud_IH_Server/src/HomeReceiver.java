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
	
	public ServerSocket getSocket(){
		return this.socket;
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				Socket connection = socket.accept();
				System.out.println("HomeReceiver: "+connection.getInetAddress()+":"+connection.getPort()+" is established.");
				
				/*/
				String str=null;
				InputStream in = connection.getInputStream();
				StringBuilder strBuilder = new StringBuilder();
				InputStreamReader reader = new InputStreamReader(in, "ASCII");
				for (int c = reader.read(); c != -1; c = reader.read()) {
					strBuilder.append((char) c);
				}
				str=strBuilder.toString();
				System.out.println("Log: message received: "+str);
				/*/
				//socket read
				String str=null;
				
				try{
					DataInputStream in = new DataInputStream(connection.getInputStream());
					
					if(in!=null){
						try{
							str=in.readUTF();
							if(debug) System.out.println("HomeReceiver, message received: "+str);
						}catch(IOException e){
							if(debug) System.out.println("HomeReceiver error 0: "+e.getMessage());
						}
					}
				}catch(IOException e){
					if(debug) System.out.println("HomeReceiver error: "+e.getMessage());
				}
				//*/
				
				if(str==null){
					System.out.print("str is null!!!!");
				}
				
				if(str.equals("Hue")){
					if(debug) System.out.println("	HomeReceiver, Hue is verified");

					/*
					try (Socket socketToHomeThread = new Socket("localhost", HOMETHREAD_PORT)) {
						socketToHomeThread.setSoTimeout(15000);
						try{
							DataOutputStream out = new DataOutputStream(socketToHomeThread.getOutputStream());
							if(out!=null){
								try{
									if(debug) System.out.println("HomeReceiver, sends a message to HomeThread: "+str);
									out.writeUTF(str);
								}catch(IOException e){
									if(debug) System.out.println("HomeReceiver error: "+e.getMessage());
								}
							}
						}catch(IOException e){
							if(debug) System.out.println("HomeReceiver error: "+e.getMessage());
						}
					} catch (IOException ex) {
						if(debug) System.out.println("HomeReceiver error: "+ex.getMessage());
					}
					*/
					
					//write the message
					try{
						DataOutputStream out = new DataOutputStream(this.homeSocket.getOutputStream());
						if(out!=null){
							try{
								if(debug) System.out.println("	HomeReceiver, sends a message to Pi: "+str);
								out.writeUTF(str);
							}catch(IOException e){
								if(debug) System.out.println("	HomeReceiver error: "+e.getMessage());
							}
						}
					}catch(IOException e){
						if(debug) System.out.println("	HomeReceiver error 1: "+e.getMessage());
					}
				}
				
				if(str.equals("Connection")){
					if(debug) System.out.println("HomeReceiver, Connection is verified");
					this.homeSocket=connection;

					//homeThread = new HomeThread(homeSocket, HOMETHREAD_PORT);
					//pool.submit(homeThread);
					
					/*
					try{
						Writer out = new OutputStreamWriter(homeSocket.getOutputStream());
				        out.write("Connected to homeReceiver");
				        out.flush();
				        out.close();
				        if(debug) System.out.println("HomeReceiver, sends a message to Pi: "+str);
					}catch (IOException ex) {
						System.err.println(ex);
					} 
					*/
					
					
					try{
						DataOutputStream out = new DataOutputStream(homeSocket.getOutputStream());
						if(out!=null){
							try{
								str="Connected to homeReceiver";
								if(debug) System.out.println("HomeReceiver, sends a message to Pi: "+str);
								out.writeUTF(str);
							}catch(IOException e){
								if(debug) System.out.println("HomeReceiver error 2: "+e.getMessage());
							}
						}
					}catch(IOException e){
						if(debug) System.out.println("HomeReceiver error 3: "+e.getMessage());
					}	
					if(debug) System.out.println("Connection finished");
				}
				//connection.close();
			} catch (IOException ex) {
				if(debug) System.out.println("HomeReceiver error 4: "+ex.getMessage());
			}
		}
	}
}
