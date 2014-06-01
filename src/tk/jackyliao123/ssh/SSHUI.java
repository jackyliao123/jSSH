package tk.jackyliao123.ssh;

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
		terminal.command = new CommandListener(frame, canvas);

		frame.add(canvas);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.addWindowListener(new WindowListener(ssh));
		frame.setVisible(true);
	}
	public void startReader(){
		reader.start();
	}
	public static void main(String[] args){
		try{
			SSH ssh = new SSH("192.168.2.17", 22, "pi", "raspberry");
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