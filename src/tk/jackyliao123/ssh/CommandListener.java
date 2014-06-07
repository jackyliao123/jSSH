package tk.jackyliao123.ssh;

import java.awt.Dimension;
import java.awt.Frame;

public class CommandListener {
	public Frame frame;
	public SSHCanvas observer;
	public Terminal terminal;
	public CommandListener(Frame frame, SSHCanvas observer, Terminal terminal){
		this.frame = frame;
		this.observer = observer;
		this.terminal = terminal;
	}
	public void repaint(){
		if(observer != null)
			observer.repaint();
	}
	public void setWindowTitle(String title){
		if(frame != null)
			frame.setTitle(title);
	}
	public void setWindowWidth(int width){
		terminal.consoleWidth = width;
		if(frame != null && observer != null){
			observer.setPreferredSize(new Dimension(width * observer.fontWidth, terminal.consoleHeight * observer.fontHeight));
			observer.setSize(observer.getPreferredSize());
			frame.pack();
			frame.setLocationRelativeTo(null);
		}
	}
}
