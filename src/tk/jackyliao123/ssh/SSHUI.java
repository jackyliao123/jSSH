package tk.jackyliao123.ssh;

import java.awt.Frame;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import java.awt.*;

public class SSHUI{
	static{
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(Exception e){
		}
	}
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
			PromptUI ui = new PromptUI();
			ui.show();
			SSH ssh = new SSH(ui.getHost(), ui.getPort(), ui.getUsername(), ui.getPassword());
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