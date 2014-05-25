package tk.jackyliao123.ssh;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

public class SSH {
	static{
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(Exception e){
		}
	}
	public final Session session;
	public ChannelShell channel;
	public OutputStream output;
	public InputStream input;
	public SSH(String host, int port, String user, String pass) throws JSchException{
		Properties props = new Properties();
		props.setProperty("StrictHostKeyChecking", "false");
		JSch jsch = new JSch();
		session = jsch.getSession(user, host, port);
		session.setUserInfo(new Info());
		session.setPassword(pass);
	}
	public void connectSession() throws JSchException{
		session.connect();
	}
	public void openChannel() throws JSchException, IOException{
		channel = (ChannelShell)session.openChannel("shell");

		input = channel.getInputStream();
		
		output = channel.getOutputStream();
		
		channel.connect(3000);
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
		public boolean promptYesNo(String message){
			return JOptionPane.showConfirmDialog(null, message, "jSSH Security Alert", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.OK_OPTION;
		}
		public void showMessage(String message){
			JOptionPane.showMessageDialog(null, message, "jSSH Security Alert", JOptionPane.INFORMATION_MESSAGE);
		}
	}
}