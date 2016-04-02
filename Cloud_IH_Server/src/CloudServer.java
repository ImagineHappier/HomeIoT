import java.net.*;
import java.io.*;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class CloudServer {
	
	public static void main(String[] args) {
		Socket homeSocket=null;
		
		HomeReceiver home = new HomeReceiver(homeSocket);
		home.start();

		WebReceiver web = new WebReceiver();
		web.start();

		System.out.println("!---server started---!");
		
		
	}
}
