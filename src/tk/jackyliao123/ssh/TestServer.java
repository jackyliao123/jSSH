package tk.jackyliao123.ssh;

import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class TestServer {
	public static void main(String[] args){
		try{
			ServerSocket sSocket = new ServerSocket(81);
			Socket socket = sSocket.accept();
			OutputStream output = socket.getOutputStream();
			Scanner scanner = new Scanner(System.in);
			while(scanner.hasNextLine()){
				output.write(scanner.nextLine().replace("\\e", "\u001b").replace("\\n", "\n").replace("\\r", "\r").replace("\\f", "\f").replace("\\b", "\b").getBytes());
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
