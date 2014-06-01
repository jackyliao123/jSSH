package tk.jackyliao123.ssh;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class WindowListener extends WindowAdapter{
	public SSH ssh;
	public WindowListener(SSH ssh){
		this.ssh = ssh;
	}
	public void windowClosing(WindowEvent e){
		ssh.closeChannel();
	}
}
