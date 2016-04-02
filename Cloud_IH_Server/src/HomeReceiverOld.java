import java.net.*;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.*;

//Receive a connection from Pi then save the IP of Pi.

public class HomeReceiverOld extends Thread{
	public final static int HOMECONNECTION_PORT = 2017;
	public final static int HOMETHREAD_PORT = 2018;
	
	public final static int NUM_THREADS = 50;
	
	ServerSocket socket=null;

	HomeThread homeThread = null;
	public static Socket homeSocket=null;
	
	boolean debug=true;
	
	DataInputStream in;
	DataOutputStream out;
	
	ExecutorService pool=null;
	
	HomeReceiverOld(){
	    pool = Executors.newFixedThreadPool(NUM_THREADS);
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
			try (Socket connection = socket.accept()) {
				System.out.println("HomeReceiver: "+connection.getInetAddress()+":"+connection.getPort()+" is established.");
				
				/*
				//read a message from web
				InputStream in = connection.getInputStream();
				StringBuilder str = new StringBuilder();
				InputStreamReader reader = new InputStreamReader(in, "ASCII");
				
				//if(debug) System.out.println("HomeReceiver, checks str characters");
				
				for (int c = reader.read(); c != -1; c = reader.read()) {
					//if(debug) System.out.print("'"+(char)c+"'");
					str.append((char)c);
				}
				//if(debug) System.out.println();
								
				if(debug) System.out.println("HomeReceiver, received a message: "+ str);
				*/
				String str=null;
				try{
					DataInputStream in = new DataInputStream(connection.getInputStream());
					if(in!=null){
						try{
							str=in.readUTF();
							if(debug) System.out.println("HomeReceiver, message received: "+str);
						}catch(IOException e){
							if(debug) System.out.println("HomeReceiver error: "+e.getMessage());
						}
					}
				}catch(IOException e){
					if(debug) System.out.println("HomeReceiver error: "+e.getMessage());
				}
				//Problem solved. StringBuilder str; str.equals("Hue"); not working. need to use
				//	str.toString().equals("Hue")
				
				//Problem solved.
				//Do not use 
				//	InputStream in = connection.getInputStream();
				//	StringBuilder str = new StringBuilder();
				//	InputStreamReader reader = new InputStreamReader(in, "ASCII");
				//because 
				//	Writer out = new OutputStreamWriter(socket.getOutputStream());
				//flush() is not working until socket closed.
				
				//Problem solved
				//	DataOutputStream
				//	out.writeUTF("dkasl");
				//are not working without try catch
				
				if(str==null){
					System.out.print("str is null!!!!");
				}
				
				if(str.equals("Hue")){
					if(debug) System.out.println("HomeReceiver, Hue is verified");
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
					connection.close();
				}
				
				if(str.equals("Connection")){
					if(debug) System.out.println("HomeReceiver, Connection is verified");
					homeSocket=connection;
					
					//homeThread = new HomeThread(homeSocket, HOMETHREAD_PORT);
					//homeThread.start();
					
					////
					try{
						DataOutputStream out = new DataOutputStream(homeSocket.getOutputStream());
						if(debug) System.out.println("HomeThread, prepares to send a message to Pi: "+str);
						if(out!=null){
							try{
								str="Connected to homeReceiver";
								if(debug) System.out.println("HomeThread, sends a message to Pi: "+str);
								out.writeUTF(str);
							}catch(IOException e){
								if(debug) System.out.println("HomeReceiver error: "+e.getMessage());
							}
						}
					}catch(IOException e){
						if(debug) System.out.println("HomeReceiver error: "+e.getMessage());
					}	
				}
			} catch (IOException ex) {
				if(debug) System.out.println("HomeReceiver error: "+ex.getMessage());
			}
		}
	}
}
