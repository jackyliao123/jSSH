package tk.jackyliao123.ssh;

import java.awt.Canvas;
import java.awt.Component;
import java.awt.Frame;

public class CommandListener {
	public Frame frame;
	public Component observer;
	public CommandListener(Frame frame, Component observer){
		this.frame = frame;
		this.observer = observer;
	}
	public void repaint(){
		observer.repaint();
	}
	public void setWindowTitle(String title){
		frame.setTitle(title);
	}
}
