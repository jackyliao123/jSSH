package tk.jackyliao123.ssh;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class KeyListener extends KeyAdapter{
	public SSH ssh;
	public KeyListener(SSH ssh){
		this.ssh = ssh;
	}
	public void keyPressed(KeyEvent e) {
		try {
			KeyDecoder.sendKeyStroke(ssh.output, e);
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
