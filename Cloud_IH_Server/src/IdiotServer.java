import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;


public class IdiotServer {
	public final static int CONNECTION_PORT = 2016;
	
	static ServerSocket socket=null;
	static Socket homeSocket=null;
	
	static boolean debug=true;

	static DataOutputStream out = null;
	static DataInputStream in = null;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try{
			socket = new ServerSocket(CONNECTION_PORT);
			if(debug) System.out.println("Server is ready");
		}catch (IOException ex) {
			System.err.println(ex);
		}
		
		while(true){
			try {
				Socket connection = socket.accept();
				System.out.println("Log: "+connection.getInetAddress()+":"+connection.getPort()+" is established.");
				
				//Step 1: Read message
				String str=null;
				
				InputStream in = connection.getInputStream();
				StringBuilder strBuilder = new StringBuilder();
				InputStreamReader reader = new InputStreamReader(in, "ASCII");
				for (int c = reader.read(); c != -1; c = reader.read()) {
					strBuilder.append((char) c);
				}
				str=strBuilder.toString();
				System.out.println("Log: message received: "+str);
					
					
					/*
					in = new DataInputStream(connection.getInputStream());
					if(in!=null){
						try{
							str=in.readUTF();
							if(debug) System.out.println("Log: message received: "+str);
						}catch(IOException e){
							if(debug) System.out.println("Log: error 0: "+e.getMessage());
						}
					}
					*/
				
				
				//Step 2-1: If the message is 'connection'
				if(str.equals("Connection")){
					if(debug) System.out.println("Connection is verified");
					homeSocket=connection;
					
					
					try{
						out = new DataOutputStream(homeSocket.getOutputStream());
						if(out!=null){
							try{
								str="Connected to homeReceiver";
								if(debug) System.out.println("sends a message to Pi: "+str);
								out.writeUTF(str);
							}catch(IOException e){
								if(debug) System.out.println("Log error 2: "+e.getMessage());
							}
						}
					}catch(IOException e){
						if(debug) System.out.println("Log error 3: "+e.getMessage());
					}	
					
					
					if(debug) System.out.println("'Connection' finished");
					if(debug) System.out.println("");
				}
				
				//Step 2-2: If the message is 'Hue'
				if(str.equals("Hue")){
					if(debug) System.out.println("'Hue' is verified");

					//write the message
					try{
						out = new DataOutputStream(homeSocket.getOutputStream());
						if(out!=null){
							try{
								if(debug) System.out.println("sends a message to Pi: "+str);
								out.writeUTF(str);
							}catch(IOException e){
								if(debug) System.out.println("Log error 4: "+e.getMessage());
							}
						}
					}catch(IOException e){
						if(debug) System.out.println("Log error 5: "+e.getMessage());
					}
					
					//connection.close();
				}
				//connection.close();
				//////
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

	}

}
