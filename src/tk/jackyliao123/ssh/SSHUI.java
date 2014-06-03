package tk.jackyliao123.ssh;

import java.awt.Frame;

import javax.swing.JOptionPane;
import java.awt.*;

public class SSHUI{
	public SSH ssh;
	public Terminal terminal;
	public SSHCanvas canvas;
	private Frame frame;
	public DataReaderThread reader;
	public SSHUI(SSH ssh){
		this.ssh = ssh;
		terminal = new Terminal(ssh);
		reader = new DataReaderThread(terminal);
	}
	public void showWindow(){
		frame = new Frame("jSSH");
		canvas = new SSHCanvas(terminal);
		terminal.command = new CommandListener(frame, canvas, terminal);

		frame.add(canvas);
		frame.addWindowListener(new WindowListener(ssh));
		frame.setResizable(false);
		frame.setVisible(true);
		frame.pack();
		frame.setLocationRelativeTo(null);
		canvas.requestFocus();
	}
	public void startReader(){
		reader.start();
	}
	public static void main(String[] args){
		try{
			SSH ssh = new SSH(JOptionPane.showInputDialog("ip", "192.168.2.17"), Integer.parseInt(JOptionPane.showInputDialog("port", "22")), JOptionPane.showInputDialog("user", "pi"), JOptionPane.showInputDialog("password", "raspberry"));
			ssh.connectSession();
			ssh.openChannel();
			SSHUI sshui = new SSHUI(ssh);
			sshui.showWindow();
			sshui.startReader();
		}
		catch(Exception e){
			e.printStackTrace();
			System.exit(1);
		}
	}
}