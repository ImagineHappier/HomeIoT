import java.net.*;
import java.io.*;
import java.util.Date;
import java.util.concurrent.BlockingQueue;

//03.12.2016
//Receive messages from web pages then send the messages to pi.
//Message examples: 
//	Hue_turnOn
//	Hue_turnoff

public class WebReceiver extends Thread {
	public final static int WEBCONNECTION_PORT = 2016;
	public final static int HOMECONNECTION_PORT = 2017;
	ServerSocket socket = null;
	Socket homeSocket = null;
	boolean debug = true;
	
	WebReceiver() {
		try {
			this.sleep(300);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			socket = new ServerSocket(WEBCONNECTION_PORT);
			if (debug)System.out.println("WebReceiver is ready");
		} catch (IOException ex) {
			System.err.println(ex);
		}
	}
	
	WebReceiver(ServerSocket homeSocket) {
		try {
			socket = new ServerSocket(WEBCONNECTION_PORT);
			
			if (debug)
				System.out.println("WebReceiver is ready");
		} catch (IOException ex) {
			System.err.println(ex);
		}
	}

	@Override
	public void run() {
		while (true) {
			try (Socket connection = socket.accept()) {
				String str=null;
				int intTemp=0;
				if (debug)System.out.println("WebReceiver: "+ connection.getInetAddress() + ":"+ connection.getPort() + " is established.");

				// read a message from web
				/*/
				InputStream in = connection.getInputStream();
				StringBuilder strBuilder = new StringBuilder();
				InputStreamReader reader = new InputStreamReader(in, "ASCII");
				for (int c = reader.read(); c != -1; c = reader.read()) {
					strBuilder.append((char) c);
				}
				str=strBuilder.toString();
				System.out.println("Log: message received: "+str);
				/*/
				try{
					DataInputStream in = new DataInputStream(connection.getInputStream());
					if(in!=null){
						try{
							str=in.readUTF();
							//intTemp=in.readInt();
							if(debug) System.out.println("WebReceiver, message received [str]: "+str);
							//if(debug) System.out.println("WebReceiver, message received [int]: "+intTemp);
						}catch(IOException e){}
					}
				}catch(IOException e){
				}
				//*/
								
				try (Socket socketToHomeReceiver = new Socket("www.imaginehappier.com", HOMECONNECTION_PORT)) {
					socketToHomeReceiver.setSoTimeout(15000);
					try{
						DataOutputStream out = new DataOutputStream(socketToHomeReceiver.getOutputStream());
						if(out!=null){
							try{
								if(debug) System.out.println("WebReceiver, sends a message to HomeReceiver: "+str);
								out.writeUTF(str);
							}catch(IOException e){}
						}
					}catch(IOException e){
					}
				} catch (IOException ex) {
				}
				connection.close();
			} catch (IOException ex) {
			}
		}

	}

}
