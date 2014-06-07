import java.io.InputStream;
import java.net.Socket;

public class TestClient {
	public static void main(String[] args){
		try{
			Socket socket = new Socket("jackyliao123.tk", 81);
			InputStream input = socket.getInputStream();
			while(true){
				System.out.print((char)input.read());
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
