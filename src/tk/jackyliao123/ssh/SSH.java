package tk.jackyliao123.ssh;

import java.util.Properties;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

public class SSH {
	public static void main(String args[]) throws Exception{
		String user = "root";
		String pass = "Jacky123+VPS";
		String host = "vps.jackyliao123.tk";
 
		Properties props = new Properties();
		props.setProperty("StrictHostKeyChecking", "false");
		JSch jsch = new JSch();
		Session session = jsch.getSession(user, host, 22);
		session.setUserInfo(new Info());
		session.setPassword(pass);
		session.connect();
		Channel channel = session.openChannel("shell");

		channel.setInputStream(System.in);
		
		channel.setOutputStream(System.out);
		
		channel.connect(3000);
	}
}
class Info implements UserInfo{
	public String getPassphrase(){
		return null;
	}
	public String getPassword(){
		return null;
	}
	public boolean promptPassword(String string){
		return true;
	}
	public boolean promptPassphrase(String string){
		return true;
	}

	public boolean promptYesNo(String string){
		return true;
	}
	public void showMessage(String string){
	}
}