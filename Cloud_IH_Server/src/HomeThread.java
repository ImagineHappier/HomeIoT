import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class HomeThread implements Runnable {
	// DataInputStream in;
	// DataOutputStream out;

	ServerSocket socket = null;

	public static Socket homeSocket = null;
	int homePort;

	DataOutputStream out = null;
	String str = null;
	
	boolean debug = true;

	HomeThread(Socket homeSocket, int port) {
		this.homePort = port;
		this.homeSocket = homeSocket;
		
		try {
			socket = new ServerSocket(port);
			if (debug)System.out.println("HomeThread(" + socket.getInetAddress()+ ":" + port + ") is ready");
		} catch (IOException ex) {
			if (debug) System.out.println("HomeThread error 1: " + ex.getMessage());
		}
	}

	@Override
	public void run() {
		
		while (true) {
			// connection from homeReceiver
			// read message from web
			try (Socket connection = socket.accept()){
				System.out.println("	HomeThread: "+connection.getInetAddress()+":"+connection.getPort()+" is established.");
				
				//get input
				String str=null;
				try{
					DataInputStream in = new DataInputStream(connection.getInputStream());
					if(in!=null){
						try{
							str=in.readUTF();
							if(debug) System.out.println("	HomeThread, message received: "+str);
						}catch(IOException e){
							if(debug) System.out.println("	HomeThread error: "+e.getMessage());
						}
					}
				}catch(IOException e){
					if(debug) System.out.println("	HomeThread error 2: "+e.getMessage());
				}
				
				//write data to Pi
				try{
					DataOutputStream out = new DataOutputStream(homeSocket.getOutputStream()); //error
					System.out.println("	!! 1");
					if(out!=null){
						try{
							//str="Connected to homeReceiver";
							System.out.println("	!! 2");
							if(debug) System.out.println("	HomeThread, sends a message to Pi: "+str);
							out.writeUTF(str);
							System.out.println("	!! 3");
						}catch(IOException e){
							System.out.println("	!!!! 5 " + this.homeSocket.isConnected());
							if(debug) System.out.println("	HomeThread error 3: "+e.getMessage());
						}
					}
				}catch(IOException e){
					if(debug) System.out.println("	HomeThread error 4: "+e.getMessage());
				}
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
					
		}
	}
}
