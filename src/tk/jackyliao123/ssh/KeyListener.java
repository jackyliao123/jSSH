package tk.jackyliao123.ssh;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class KeyListener extends KeyAdapter implements KeyEventPostProcessor{
	public SSH ssh;
	private boolean[] tabPresses = new boolean[2];
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
	public boolean postProcessKeyEvent(KeyEvent e) {
		// For some reason tab sends: 9
		//                            0
		//                            9
		if (e.getKeyCode() == 9 && !tabPresses[1] && !tabPresses[0]) { // First(9)
			tabPresses[0] = true;
		}
		else if (e.getKeyCode() == 0 && tabPresses[0] && !tabPresses[1]) { // Second(0)
			tabPresses[1] = true;
		}
		else if (e.getKeyCode() == 9 && tabPresses[1] && tabPresses[0]) { // Last(9)
			try {
				KeyDecoder.sendKeyStroke(ssh.output, e);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			tabPresses[0] = false;
			tabPresses[1] = false;
		} else {
			tabPresses[0] = false;
			tabPresses[1] = false;
		}
		return true;
	}
}
